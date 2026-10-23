package com.ibigboss.crm.controller;

import com.ibigboss.crm.dto.request.AuthRequest;
import com.ibigboss.crm.dto.response.AuthResponse;
import com.ibigboss.crm.dto.response.UserResponse;
import com.ibigboss.crm.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserService userService;

    public AuthController(AuthenticationManager authenticationManager, UserService userService) {
        this.authenticationManager = authenticationManager;
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        UserResponse user = userService.getByUsername(request.getUsername());

        String token = generateToken(authentication);

        AuthResponse response = new AuthResponse();
        response.setToken(token);
        response.setUser(user);
        return ResponseEntity.ok(response);
    }

    private String generateToken(Authentication authentication) {
        String roles = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        long expiresAt = System.currentTimeMillis() + 86400000L; // 24 hours

        return "eyJalgOiJIUzI1NiJ9." +
                String.format("{\"sub\":\"%s\",\"roles\":\"%s\",\"exp\":%d}",
                        authentication.getName(), roles, expiresAt) +
                ".signature";
    }

    @PostMapping("/refresh")
    public ResponseEntity<Map<String, String>> refresh() {
        return ResponseEntity.ok(Map.of("message", "Token refreshed"));
    }
}
