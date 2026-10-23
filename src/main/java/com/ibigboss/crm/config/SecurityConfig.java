package com.ibigboss.crm.config;

import com.ibigboss.crm.repository.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final UserRepository userRepository;
    private final JwtAuthFilter jwtAuthFilter;

    public SecurityConfig(UserRepository userRepository, JwtAuthFilter jwtAuthFilter) {
        this.userRepository = userRepository;
        this.jwtAuthFilter = jwtAuthFilter;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .csrf(AbstractHttpConfigurer::disable)
            .headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin))
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(
                    "/api/health",
                    "/swagger-ui/**", "/swagger-ui.html", "/v3/api-docs/**",
                    "/swagger-resources/**", "/webjars/**",
                    "/h2-console/**", "/h2-console"
                ).permitAll()
                .requestMatchers("/api/v1/auth/login").permitAll()
                .requestMatchers("/api/v1/auth/refresh").permitAll()
                .requestMatchers("/api/v1/locations/**").permitAll()
                .requestMatchers("/api/v1/parts/**").hasAnyRole("RECEIVER", "MASTER", "ADMIN", "DIRECTOR")
                .requestMatchers("/api/v1/clients/**").hasAnyRole("RECEIVER", "MASTER", "ADMIN", "DIRECTOR")
                .requestMatchers("/api/v1/orders/**").hasAnyRole("RECEIVER", "MASTER", "ADMIN", "DIRECTOR")
                .requestMatchers("/api/v1/payments/**").hasAnyRole("RECEIVER", "MASTER", "ADMIN", "DIRECTOR")
                .requestMatchers("/api/v1/users/**").hasAnyRole("ADMIN", "DIRECTOR")
                .requestMatchers("/api/v1/service-centers/**").hasAnyRole("ADMIN", "DIRECTOR")
                .anyRequest().authenticated()
            );
        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return new UserDetailsService() {
            @Override
            public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
                var user = userRepository.findByUsername(username)
                        .orElseThrow(() -> new UsernameNotFoundException("Пользователь не найден: " + username));
                
                String roleName = "ROLE_" + user.getRole().name();
                
                return new UserDetails() {
                    @Override
                    public List<GrantedAuthority> getAuthorities() {
                        return List.of(new SimpleGrantedAuthority(roleName));
                    }
                    @Override
                    public String getPassword() {
                        return user.getPasswordHash();
                    }
                    @Override
                    public String getUsername() {
                        return user.getUsername();
                    }
                    @Override
                    public boolean isAccountNonExpired() {
                        return true;
                    }
                    @Override
                    public boolean isAccountNonLocked() {
                        return user.isActive();
                    }
                    @Override
                    public boolean isCredentialsNonExpired() {
                        return true;
                    }
                    @Override
                    public boolean isEnabled() {
                        return user.isActive();
                    }
                };
            }
        };
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of(
            "http://localhost:5173",
            "http://localhost:3000"
        ));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);
        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
