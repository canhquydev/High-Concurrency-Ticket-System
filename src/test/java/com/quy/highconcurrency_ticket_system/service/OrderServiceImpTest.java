package com.quy.highconcurrency_ticket_system.service; // Chú ý đổi package cho khớp

import com.quy.highconcurrency_ticket_system.dto.request.OrderRequest;
import com.quy.highconcurrency_ticket_system.exception.InsufficientTicketsException;
import com.quy.highconcurrency_ticket_system.exception.ResourceNotFoundException;
import com.quy.highconcurrency_ticket_system.model.Order;
import com.quy.highconcurrency_ticket_system.model.Ticket;
import com.quy.highconcurrency_ticket_system.model.User;
import com.quy.highconcurrency_ticket_system.repository.OrderItemRepository;
import com.quy.highconcurrency_ticket_system.repository.OrderRepository;
import com.quy.highconcurrency_ticket_system.repository.TicketRepository;
import com.quy.highconcurrency_ticket_system.repository.UserRepository;
import com.quy.highconcurrency_ticket_system.service.Imp.OrderServiceImp;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.math.BigDecimal;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrderServiceImpTest {

    // @Mock: Dùng để làm giả các Repository và Template (Không gọi vào DB thật)
    @Mock
    private UserRepository userRepository;
    @Mock
    private TicketRepository ticketRepository;
    @Mock
    private OrderRepository orderRepository;
    @Mock
    private OrderItemRepository orderItemRepository;
    @Mock
    private StringRedisTemplate redisTemplate;
    @Mock
    private RabbitTemplate rabbitTemplate;
    @Mock
    private ValueOperations<String, String> valueOperations;

    // @InjectMocks: Bơm các Mock ở trên vào Service này để test
    @InjectMocks
    private OrderServiceImp orderService;

    private User testUser;
    private Ticket testTicket;
    private OrderRequest request;

    @BeforeEach
    void setUp() {
        // 1. Làm giả Spring Security (Giả vờ đang có người đăng nhập tên là test@gmail.com)
        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        lenient().when(authentication.getName()).thenReturn("test@gmail.com");
        SecurityContextHolder.setContext(securityContext);

        // 2. Dữ liệu mồi
        testUser = new User();
        testUser.setId(1L);
        testUser.setEmail("test@gmail.com");

        testTicket = new Ticket();
        testTicket.setId(100L);
        testTicket.setPrice(BigDecimal.valueOf(50000));

        request = new OrderRequest();
        request.setTicketId(100L);
        request.setQuantity(2);

        // 3. Chuẩn bị sẵn opsForValue cho Redis
        lenient().when(redisTemplate.opsForValue()).thenReturn(valueOperations);
    }

    // ☑️ Test Case 1: Mua vé THÀNH CÔNG
    @Test
    void create_Success_ReturnsOrderPending() {
        // Giả lập: Redis trừ vé vẫn còn dư (Trả về 10 vé)
        when(valueOperations.decrement(anyString(), anyLong())).thenReturn(10L);

        // Giả lập: DB tìm thấy User và Ticket
        when(userRepository.findByEmail("test@gmail.com")).thenReturn(Optional.of(testUser));
        when(ticketRepository.findById(100L)).thenReturn(Optional.of(testTicket));

        // Giả lập: Đơn hàng lưu xong được cấp ID là 999
        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> {
            Order order = invocation.getArgument(0);
            order.setId(999L);
            return order;
        });

        // HÀNH ĐỘNG THỰC TẾ
        String result = orderService.create(request);

        // KIỂM TRA KẾT QUẢ
        assertEquals("Your order is being processed.", result);

        // Đảm bảo các hàm này đã được chạy đúng 1 lần
        verify(orderRepository, times(1)).save(any());
        verify(orderItemRepository, times(1)).save(any());
        verify(rabbitTemplate, times(1)).convertAndSend(anyString(), any(Object.class));
    }

    // ☑️ Test Case 2: HẾT VÉ -> Báo lỗi
    @Test
    void create_Fail_OutOfTickets_ThrowsException() {
        // Giả lập: Redis trừ xong bị âm (Trả về -1)
        when(valueOperations.decrement(anyString(), anyLong())).thenReturn(-1L);

        // Hành động & Kiểm tra (Phải văng ra lỗi InsufficientTicketsException)
        assertThrows(InsufficientTicketsException.class, () -> {
            orderService.create(request);
        });

        // Kiểm tra xem hệ thống có biết tự hoàn lại số lượng vé vào Redis (increment) không
        verify(valueOperations, times(1)).increment(anyString(), eq(2L));

        // Tuyệt đối không được phép gọi lưu DB hay bắn RabbitMQ
        verify(orderRepository, never()).save(any());
        verify(rabbitTemplate, never()).convertAndSend(anyString(), Optional.ofNullable(any()));
    }

    // ☑️ Test Case 3: KHÔNG TÌM THẤY USER -> Báo lỗi
    @Test
    void create_Fail_UserNotFound_ThrowsException() {
        // Giả lập: Vé vẫn còn
        when(valueOperations.decrement(anyString(), anyLong())).thenReturn(10L);

        // Giả lập: Không tìm thấy ông khách nào mang mail này
        when(userRepository.findByEmail("test@gmail.com")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            orderService.create(request);
        });
    }

}
