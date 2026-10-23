package com.ibigboss.crm.config;

import com.ibigboss.crm.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private final UserRepository userRepository;

    public JwtAuthFilter(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");
        
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            
            // Simple token: "token_<username>_<role>"
            if (token.startsWith("token_")) {
                String payload = token.substring(6);
                String[] parts = payload.split("_");
                
                if (parts.length >= 2) {
                    String username = parts[0];
                    String role = parts[1];
                    
                    try {
                        var user = userRepository.findByUsername(username)
                                .orElseThrow(() -> new UsernameNotFoundException("Пользователь не найден: " + username));
                        
                        String roleName = "ROLE_" + role.toUpperCase();
                        List<GrantedAuthority> authorities = List.of(
                                new SimpleGrantedAuthority(roleName)
                        );
                        
                        UsernamePasswordAuthenticationToken authToken = 
                                new UsernamePasswordAuthenticationToken(user, null, authorities);
                        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(authToken);
                    } catch (UsernameNotFoundException e) {
                        logger.debug("User not found for token: " + username);
                    }
                }
            }
        }
        
        filterChain.doFilter(request, response);
    }
}
