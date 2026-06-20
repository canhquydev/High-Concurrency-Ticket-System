# High-Concurrency Ticket System

## 📖 Giới thiệu dự án
High-Concurrency Ticket System là một hệ thống đặt vé được thiết kế đặc biệt để xử lý lượng lớn truy cập và giao dịch đồng thời (high concurrency). Dự án tập trung vào việc giải quyết các bài toán kinh điển trong hệ thống phân tán và ứng dụng web tải cao như: đảm bảo tính toàn vẹn dữ liệu khi có nhiều người cùng tranh mua một vé (tránh tình trạng over-selling hay bán vượt mức), xử lý hàng đợi bất đồng bộ để giảm tải cho hệ thống chính, và tối ưu hóa hiệu suất truy xuất dữ liệu.

## 🚀 Tính năng nổi bật
*   **Xử lý đồng thời cao:** Tận dụng Redis để thao tác trừ tồn kho nguyên tử (atomic decrement) kết hợp với hàng đợi RabbitMQ giúp tránh nghẽn Database và loại bỏ rủi ro over-selling mà không cần khóa Database phức tạp.
*   **Kiến trúc bất đồng bộ:** Tích hợp RabbitMQ để đưa các tác vụ tạo và xử lý đơn hàng vào hàng đợi, giúp tăng thông lượng (throughput) và phản hồi nhanh (low latency) cho người dùng.
*   **Quản lý tồn kho tốc độ cao:** Sử dụng Redis để lưu trữ, đồng bộ hóa và trừ số lượng vé tồn kho. Hỗ trợ logic bù trừ (compensation logic) - rollback lại số lượng vé trên Redis nếu quá trình lưu đơn hàng xuống Database thất bại.
*   **Bảo mật:** Áp dụng Spring Security và OAuth2 Resource Server để quản lý xác thực và phân quyền truy cập.
*   **Tài liệu API:** Tích hợp sẵn Swagger UI (SpringDoc OpenAPI) để dễ dàng theo dõi, kiểm thử và tích hợp API.
*   **Đã qua kiểm thử chịu tải (Stress Test):** Hệ thống được thiết kế để ổn định dưới áp lực cao, đã được kiểm chứng qua JMeter.

## 🛠 Tech Stack
*   **Ngôn ngữ lập trình:** Java 17
*   **Core Framework:** Spring Boot
*   **Cơ sở dữ liệu (Relational Database):** 
    *   Microsoft SQL Server
    *   Spring Data JPA (Hibernate)
*   **Caching & In-memory Data Store:** Redis (Spring Data Redis, Spring Cache)
*   **Message Broker (Hàng đợi tin nhắn):** RabbitMQ (Spring AMQP)
*   **Bảo mật (Security):** Spring Security (Crypto, OAuth2 Resource Server)
*   **Thư viện hỗ trợ:**
    *   Lombok (Giảm boilerplate code)
    *   Jakarta Validation (Kiểm tra và xác thực dữ liệu đầu vào)
    *   SpringDoc OpenAPI (Giao diện Swagger UI)
*   **Testing:** JUnit/Mockito (Unit & Integration Testing), JMeter (Performance/Stress Testing)

## 🏗 Luồng xử lý đặt vé cơ bản
1.  **Tiếp nhận Request:** Người dùng gửi yêu cầu mua vé.
2.  **Kiểm tra & Trừ tồn kho (Redis):** Hệ thống kiểm tra số lượng vé còn lại trên Redis. Nếu đủ, tiến hành trừ (decrement) ngay trên Redis và cho phép đi tiếp. Nếu hết vé, lập tức trả về lỗi cho người dùng (Fast-fail).
3.  **Đẩy vào Queue (RabbitMQ):** Thông tin đặt vé hợp lệ được đẩy vào Message Queue để xử lý bất đồng bộ. Phản hồi tức thì cho người dùng là "Đơn hàng đang được xử lý".
4.  **Xử lý đơn hàng (Consumer):** Worker (Consumer) đọc tin nhắn từ RabbitMQ, tiến hành tạo đơn hàng và lưu xuống CSDL (SQL Server) một cách tuần tự.
5.  **Xử lý lỗi & Bù trừ (Compensation):** Nếu có lỗi xảy ra trong quá trình lưu Database (ví dụ: DB sập, timeout...), hệ thống catch exception và thực hiện rollback lại số lượng vé đã trừ trên Redis để đảm bảo tính nhất quán.

## ⚙️ Cài đặt & Chạy dự án
1.  **Yêu cầu môi trường:**
    *   JDK 17
    *   Maven 3.x
    *   SQL Server
    *   Redis Server
    *   RabbitMQ
2.  **Cấu hình:** Cập nhật các thông tin kết nối (Database, Redis, RabbitMQ) trong file `src/main/resources/application.properties` (hoặc `application.yml`).
3.  **Khởi chạy ứng dụng:**
    ```bash
    ./mvnw spring-boot:run
    ```
    *(Hoặc chạy thông qua IDE như IntelliJ IDEA, Eclipse)*
4.  **Truy cập Swagger UI:**
    Sau khi ứng dụng chạy thành công, mở trình duyệt và truy cập:
    ```
    http://localhost:8080/swagger-ui/index.html
    ```
*(Lưu ý: Thay đổi port 8080 nếu bạn có thiết lập port khác trong file cấu hình).*
