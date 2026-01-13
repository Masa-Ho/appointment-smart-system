package com.masa.appointment.security;

import com.masa.appointment.user.entity.UserEntity;
import com.masa.appointment.security.dto.RegisterRequest;
import com.masa.appointment.user.service.UserService;
import com.masa.appointment.security.dto.LoginRequest;
import com.masa.appointment.security.dto.LoginResponse;
import com.masa.appointment.security.JwtUtil;
import jakarta.validation.Valid;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UserService userService;

    public AuthController(AuthenticationManager authenticationManager,
                          JwtUtil jwtUtil, UserService userService) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.userService = userService;
    }

@PostMapping("/register")
@ResponseStatus(HttpStatus.CREATED)
public UserEntity register(@Valid @RequestBody RegisterRequest request) {
    return userService.registerCustomer(
            request.getFullName(),
            request.getEmail(),
            request.getPassword()
    );
}



    @PostMapping("/login")
    public LoginResponse login(@Valid @RequestBody LoginRequest request) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        String token = jwtUtil.generateToken(request.getEmail());
        return new LoginResponse(token);
    }
}
