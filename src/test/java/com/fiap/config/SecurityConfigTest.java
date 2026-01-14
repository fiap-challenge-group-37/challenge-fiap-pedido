package com.fiap.config;

import com.fiap.pedido.config.SecurityConfig;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

class SecurityConfigTest {

    private final SecurityConfig securityConfig = new SecurityConfig();

    @Test
    void jwtAuthenticationConverter_shouldReturnGrantedAuthorities_fromCognitoGroups() {
        JwtAuthenticationConverter converter = securityConfig.jwtAuthenticationConverter();

        Jwt jwt = Jwt.withTokenValue("token")
                .header("alg", "none")
                .claim("cognito:groups", List.of("ADMIN", "USER"))
                .build();

        AbstractAuthenticationToken token = (AbstractAuthenticationToken) converter.convert(jwt);
        Collection<GrantedAuthority> authorities = token.getAuthorities();

        assertThat(authorities).extracting(GrantedAuthority::getAuthority)
                .containsExactlyInAnyOrder("ROLE_ADMIN", "ROLE_USER");
    }

    @Test
    void jwtAuthenticationConverter_shouldReturnGrantedAuthority_fromCustomRole() {
        JwtAuthenticationConverter converter = securityConfig.jwtAuthenticationConverter();

        Jwt jwt = Jwt.withTokenValue("token")
                .header("alg", "none")
                .claim("custom:role", "MANAGER")
                .build();

        AbstractAuthenticationToken token = (AbstractAuthenticationToken) converter.convert(jwt);
        Collection<GrantedAuthority> authorities = token.getAuthorities();

        assertThat(authorities).extracting(GrantedAuthority::getAuthority)
                .containsExactly("ROLE_MANAGER");
    }

    @Test
    void jwtAuthenticationConverter_shouldReturnEmpty_whenNoRoleClaimsPresent() {
        JwtAuthenticationConverter converter = securityConfig.jwtAuthenticationConverter();

        Jwt jwt = Jwt.withTokenValue("token")
                .header("alg", "none")
                .claim("sub", "test")
                .build();

        AbstractAuthenticationToken token = (AbstractAuthenticationToken) converter.convert(jwt);
        Collection<GrantedAuthority> authorities = token.getAuthorities();

        assertThat(authorities).isEmpty();
    }

    @Test
    void authenticationManagerBeanShouldReturnFromConfiguration() throws Exception {
        AuthenticationConfiguration authConfig = Mockito.mock(AuthenticationConfiguration.class);
        AuthenticationManager authManager = Mockito.mock(AuthenticationManager.class);

        Mockito.when(authConfig.getAuthenticationManager()).thenReturn(authManager);

        assertThat(securityConfig.authenticationManager(authConfig)).isSameAs(authManager);
    }
}