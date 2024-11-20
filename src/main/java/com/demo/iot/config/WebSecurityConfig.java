package com.demo.iot.config;

import com.demo.iot.security.JwtTokenFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

import static org.springframework.http.HttpMethod.*;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {

    @Value("${api.prefix}")
    private String apiPrefix;

    private final JwtTokenFilter jwtTokenFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(GET, "/v3/api-docs", "/v3/api-docs/**", "/swagger-resources", "/swagger-resources/**",
                                "/configuration/ui", "/configuration/security", "/swagger-ui/**", "/swagger-ui.html",
                                "/webjars/swagger-ui/**", "/swagger-ui/index.html", "/webjars/**", "/swagger-ui/api-docs/swagger-config")
                        .permitAll()
                        .requestMatchers(POST, String.format("%s/auth/login", apiPrefix)).permitAll()
                        .requestMatchers(POST, String.format("%s/auth/logout", apiPrefix)).permitAll()
                        .requestMatchers(POST, String.format("%s/auth/refresh-token", apiPrefix)).permitAll()
                        .requestMatchers(GET, String.format("%s/auth/check-account", apiPrefix)).permitAll()
                        .requestMatchers(POST, String.format("%s/attendance", apiPrefix)).permitAll()
                        .requestMatchers(POST, String.format("%s/rfid/**", apiPrefix)).permitAll()
                        .requestMatchers(DELETE, String.format("%s/rfid/**", apiPrefix)).permitAll()
                        .requestMatchers(GET, String.format("%s/shifts", apiPrefix)).permitAll()
                        .requestMatchers(GET, String.format("%s/check", apiPrefix)).permitAll()
                        .requestMatchers(GET, String.format("%s/heartbeat", apiPrefix)).permitAll()
                        .anyRequest().authenticated()
                )
                .csrf(AbstractHttpConfigurer::disable)
                .cors(Customizer.withDefaults())
                .exceptionHandling(exceptionHandling -> exceptionHandling
                        .authenticationEntryPoint(new CustomAuthenticationEntryPoint())
                );
        return http.build();
    }

    @Bean
    public UrlBasedCorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowedOrigins(List.of("http://localhost:5173"));
        corsConfiguration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
        corsConfiguration.setAllowedHeaders(List.of("authorization", "content-type", "x-auth-token"));
        corsConfiguration.setExposedHeaders(List.of("x-auth-token"));
        corsConfiguration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);
        return source;
    }
}
