package com.fiap.pedido.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;

import java.util.List;
import java.util.stream.Collectors;

@EnableMethodSecurity(prePostEnabled = true)
@EnableWebSecurity
@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(
                                "/actuator/health",
                                "/api/actuator/health",
                                "/api/health",
                                "/health",
                                "/api/cliente/**",
                                "/cliente/**",
                                "/api/webhook/**",
                                "/webhook/**",
                                "/swagger-ui.html",
                                "/api/swagger-ui/**",
                                "/swagger-ui/**",
                                "/v3/api-docs/**",
                                "/api/v3/api-docs/**",
                                "/swagger-resources/**",
                                "/api/swagger-resources/**",
                                "/webjars/**",
                                "/api/webjars/**"
                        ).permitAll()
                        .anyRequest().authenticated()
                ).oauth2ResourceServer(oauth2 ->
                        oauth2.jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter()))
                ).build();
    }

    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(jwt -> {
            List<String> groups = jwt.getClaimAsStringList("cognito:groups");
            if (groups != null) {
                return groups.stream()
                        .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                        .collect(Collectors.toList());
            }

            String role = jwt.getClaimAsString("custom:role");
            if (role != null) {
                return List.of(new SimpleGrantedAuthority("ROLE_" + role));
            }

            return List.of();
        });
        return converter;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

}