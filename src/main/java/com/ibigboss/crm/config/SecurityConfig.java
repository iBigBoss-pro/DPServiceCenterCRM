package com.ibigboss.crm.config;

import com.ibigboss.crm.repository.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final UserRepository userRepository;

    public SecurityConfig(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin))
            .csrf(AbstractHttpConfigurer::disable)
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/health").permitAll()
                .requestMatchers("/swagger-ui/**", "/swagger-ui.html", "/v3/api-docs/**",
                    "/swagger-resources/**", "/webjars/**").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/v1/auth/**").permitAll()
                .requestMatchers("/h2-console/**", "/h2-console").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/v1/locations/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/v1/parts/**").hasAnyRole("RECEIVER", "MASTER", "ADMIN", "DIRECTOR")
                .requestMatchers(HttpMethod.GET, "/api/v1/clients/**").hasAnyRole("RECEIVER", "MASTER", "ADMIN", "DIRECTOR")
                .requestMatchers(HttpMethod.GET, "/api/v1/orders/**").hasAnyRole("RECEIVER", "MASTER", "ADMIN", "DIRECTOR")
                .requestMatchers(HttpMethod.GET, "/api/v1/payments/**").hasAnyRole("RECEIVER", "MASTER", "ADMIN", "DIRECTOR")
                .requestMatchers(HttpMethod.GET, "/api/v1/users/**").hasAnyRole("ADMIN", "DIRECTOR")
                .requestMatchers(HttpMethod.POST, "/api/v1/clients/**").hasAnyRole("RECEIVER", "ADMIN")
                .requestMatchers(HttpMethod.PUT, "/api/v1/clients/**").hasAnyRole("RECEIVER", "ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/api/v1/clients/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.POST, "/api/v1/orders/**").hasAnyRole("RECEIVER", "ADMIN")
                .requestMatchers(HttpMethod.POST, "/api/v1/orders/**/status").hasAnyRole("RECEIVER", "MASTER", "ADMIN")
                .requestMatchers(HttpMethod.POST, "/api/v1/orders/**/approve").hasAnyRole("MASTER", "ADMIN")
                .requestMatchers(HttpMethod.POST, "/api/v1/orders/**/finalize-pricing").hasAnyRole("MASTER", "ADMIN")
                .requestMatchers(HttpMethod.POST, "/api/v1/payments/**").hasAnyRole("RECEIVER", "MASTER", "ADMIN", "DIRECTOR")
                .requestMatchers(HttpMethod.POST, "/api/v1/users/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PUT, "/api/v1/users/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/api/v1/users/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.POST, "/api/v1/parts/**").hasAnyRole("ADMIN", "DIRECTOR")
                .requestMatchers(HttpMethod.PUT, "/api/v1/parts/**").hasAnyRole("ADMIN", "DIRECTOR")
                .requestMatchers(HttpMethod.DELETE, "/api/v1/parts/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.POST, "/api/v1/locations/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.GET, "/api/v1/service-centers/**").hasAnyRole("ADMIN", "DIRECTOR")
                .anyRequest().authenticated()
            );
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
