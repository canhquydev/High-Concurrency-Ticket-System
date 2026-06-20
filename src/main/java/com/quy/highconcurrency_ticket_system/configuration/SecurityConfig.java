package com.quy.highconcurrency_ticket_system.configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.web.SecurityFilterChain;

import javax.crypto.spec.SecretKeySpec;

@Configuration
@EnableWebSecurity
@SecurityScheme(
        name = "bearerAuth",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        scheme = "bearer"
)
@OpenAPIDefinition(
        security = @SecurityRequirement(name = "bearerAuth")
)
public class SecurityConfig {

    @Value("${jwt.secretKey}")
    private String secretKey;

    private final String[] PUBLIC_ENDPOINT = {
      "/api/v1/auth/**"
    };

    private final String[] SWAGGER_WHITELIST = {
            "/swagger-ui/**",
            "/v3/api-docs/**",
            "/swagger-resources/**",
            "/webjars/**",
            "/swagger-ui.html"
    };

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) {
        httpSecurity.authorizeHttpRequests(request ->
                // Cho phép tất cả mọi người vào trang Đăng ký/Đăng nhập
                request.requestMatchers(HttpMethod.POST, PUBLIC_ENDPOINT).permitAll()

                // Cho phép Swagger UI và OpenAPI Docs truy cập không cần auth
                .requestMatchers(SWAGGER_WHITELIST).permitAll()

                // Cho phép tất cả mọi người xem danh sách Sự kiện
                .requestMatchers(HttpMethod.GET, "/api/v1/events/**").permitAll()

                // Admin thì mới được vào các hàm quản lý
                .requestMatchers("/api/v1/admin/**").hasAuthority("SCOPE_ADMIN")

                // Chỉ những ai là USER thì mới được gọi API mua vé
                .requestMatchers(HttpMethod.POST,"/api/v1/orders").hasAuthority("SCOPE_USER")

                // Admin được quyền xem tất cả order
                .requestMatchers("/api/v1/orders/**").hasAuthority("SCOPE_ADMIN")

                .anyRequest().authenticated());

        httpSecurity.oauth2ResourceServer(oauth2 ->
                oauth2.jwt(jwtConfigurer -> jwtConfigurer.decoder(jwtDecoder()))
        );

        httpSecurity.csrf(AbstractHttpConfigurer::disable);
        return httpSecurity.build();
    }

    @Bean
    JwtDecoder jwtDecoder(){
        SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getBytes(), "HS512");
        return NimbusJwtDecoder
                .withSecretKey(secretKeySpec)
                .macAlgorithm(MacAlgorithm.HS512)
                .build();
    }
}
