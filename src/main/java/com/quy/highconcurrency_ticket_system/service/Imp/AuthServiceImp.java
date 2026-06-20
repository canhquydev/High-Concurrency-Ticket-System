package com.quy.highconcurrency_ticket_system.service.Imp;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.quy.highconcurrency_ticket_system.dto.request.IntrospectTokenRq;
import com.quy.highconcurrency_ticket_system.dto.request.LoginRequest;
import com.quy.highconcurrency_ticket_system.dto.request.RegisterRequest;
import com.quy.highconcurrency_ticket_system.dto.response.IntrospectTokenRp;
import com.quy.highconcurrency_ticket_system.dto.response.LoginResponse;
import com.quy.highconcurrency_ticket_system.dto.response.UserResponse;
import com.quy.highconcurrency_ticket_system.enums.Role;
import com.quy.highconcurrency_ticket_system.exception.DuplicateResourceException;
import com.quy.highconcurrency_ticket_system.exception.ResourceNotFoundException;
import com.quy.highconcurrency_ticket_system.model.User;
import com.quy.highconcurrency_ticket_system.repository.UserRepository;
import com.quy.highconcurrency_ticket_system.service.AuthService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Service
public class AuthServiceImp implements AuthService {

    @Value("${jwt.secretKey}")
    private String secretKey;

    private final UserRepository userRepository;

    public AuthServiceImp(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserResponse register(RegisterRequest request) {
        if(userRepository.existsByEmail(request.getEmail())){
            throw new DuplicateResourceException("Email", request.getEmail());
        }
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);

        User user = User.builder()
                .fullName(request.getFullName())
                .email(request.getEmail())
                .role(Role.USER)
                .password(passwordEncoder.encode(request.getPassword()))
                .build();
        userRepository.save(user);
        return new UserResponse(user);
    }

    @Override
    public LoginResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail()).orElseThrow(() ->
                new ResourceNotFoundException("User", "Email", request.getEmail()));
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        if(!passwordEncoder.matches(request.getPassword(), user.getPassword())){
            throw new BadCredentialsException("Login failed: Invalid username or password.");
        }
        String token = generateToken(user);
        return LoginResponse.builder()
                .email(request.getEmail())
                .token(token)
                .authenticated(true)
                .build();
    }

    @Override
    public IntrospectTokenRp verify(IntrospectTokenRq request) throws ParseException, JOSEException {

        JWSVerifier jwsVerifier = new MACVerifier(secretKey.getBytes());

        SignedJWT signedJWT = SignedJWT.parse(request.getToken());

        Date expired = signedJWT.getJWTClaimsSet().getExpirationTime();

        boolean verify = signedJWT.verify(jwsVerifier);

        return IntrospectTokenRp.builder()
                .valid(verify && expired.after(new Date())).build();
    }

    public String generateToken(User user) {
        JWSHeader jwsHeader = new JWSHeader(JWSAlgorithm.HS512);
        new Date().toInstant();
        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(user.getEmail())
                .issuer("Quys")
                .issueTime(new Date())
                .expirationTime(Date.from(Instant.now().plus(30, ChronoUnit.MINUTES)))
                .claim("scope", user.getRole())
                .build();
        Payload payload = new Payload(jwtClaimsSet.toJSONObject());
        JWSObject jwsObject = new JWSObject(jwsHeader, payload);
        try {
            jwsObject.sign(new MACSigner(secretKey.getBytes()));
            return jwsObject.serialize();
        } catch (JOSEException e) {
            throw new RuntimeException(e);
        }
    }
}
