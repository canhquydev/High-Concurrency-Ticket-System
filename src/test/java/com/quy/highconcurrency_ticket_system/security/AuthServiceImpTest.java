package com.quy.highconcurrency_ticket_system.service;

import com.nimbusds.jose.JOSEException;
import com.quy.highconcurrency_ticket_system.dto.request.IntrospectTokenRq;
import com.quy.highconcurrency_ticket_system.dto.response.IntrospectTokenRp;
import com.quy.highconcurrency_ticket_system.enums.Role;
import com.quy.highconcurrency_ticket_system.model.User;
import com.quy.highconcurrency_ticket_system.repository.UserRepository;
import com.quy.highconcurrency_ticket_system.service.Imp.AuthServiceImp;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.text.ParseException;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class AuthServiceImpTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private AuthServiceImp authService;

    @BeforeEach
    void setUp() {
        // Thuật toán HS512 của Nimbus yêu cầu secretKey phải đủ dài
        String secretKey = "DayLaMotCaiChuoiSecretRatDaiVaBaoMatChoJWT_PhaiDuDaiDeKhongBiLoi123456789";
        ReflectionTestUtils.setField(authService, "secretKey", secretKey);
    }

    @Test
    void testGenerateAndVerifyToken_Success() throws ParseException, JOSEException {
        // 1. Chuẩn bị User giả
        User testUser = new User();
        testUser.setEmail("test@gmail.com");
        testUser.setRole(Role.USER);

        // 2. Chạy hàm tạo Token
        String token = authService.generateToken(testUser);

        // Kiểm tra Token không được rỗng
        assertNotNull(token);
        assertFalse(token.isEmpty());

        // 3. Chạy hàm Verify (Xác thực) Token
        IntrospectTokenRq introspectRq = new IntrospectTokenRq();
        introspectRq.setToken(token);

        IntrospectTokenRp verifyResult = authService.verify(introspectRq);

        // Kiểm tra xem verify có trả về true không
        // Nếu Lombok sinh hàm getter là getValid() thì bạn đổi isValid() thành getValid() nhé
        assertTrue(verifyResult.isValid());
    }
}
