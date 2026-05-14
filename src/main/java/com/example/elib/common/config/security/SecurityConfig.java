package com.example.elib.common.config.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtValidators;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;
import java.util.stream.Stream;

@Slf4j
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig {

    private final SecurityConfigProperties properties;

//    @Bean
//    public ClientRegistrationRepository clientRegistrationRepository() {
//        return new InMemoryClientRegistrationRepository(
//                ClientRegistration.withRegistrationId("keycloak")
//                        .clientId(properties.getClientId())
//                        .clientSecret(properties.getClientSecret())
//                        .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
//                        .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
//                        .redirectUri("http://localhost:8085/login/oauth2/code/keycloak")
//                        .scope("openid", "profile", "email")
//                        .authorizationUri(properties.getOutUri().concat("/protocol/openid-connect/auth"))
//                        .tokenUri(properties.getDockerUri().concat("/protocol/openid-connect/token"))
//                        .jwkSetUri(properties.getDockerUri().concat("/protocol/openid-connect/certs"))
//                        .userInfoUri(properties.getDockerUri().concat("/protocol/openid-connect/userinfo"))
//                        .userNameAttributeName("preferred_username")
//                        .clientName("Keycloak")
//                        .build()
//        );
//    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http)
            throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .cors(cors -> cors.disable())
                .authorizeHttpRequests((authorize) -> authorize

                        .requestMatchers(
                                "/swagger-ui.html",
                                "/swagger-ui/**",
                                "/v3/api-docs/**",
                                "/api-docs/**",
                                "/api-docs",
                                "/swagger-resources/**",
                                "/swagger-resources",
                                "/webjars/**"
                        ).permitAll()

                        .requestMatchers(request -> !request.getRequestURI().startsWith("/api")).permitAll()


                        .requestMatchers("/actuator/health", "/actuator/info", "/health").permitAll()

                        .requestMatchers("/api/v1/auth/**").permitAll()

                        .anyRequest().permitAll()
                )
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt
                                .decoder(jwtDecoder())
                                .jwtAuthenticationConverter(jwtAuthenticationConverter())
                        )
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                        .permitAll()
                );;
        return http.build();
    }

    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        jwtAuthenticationConverter.setPrincipalClaimName("preferred_username");
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwt -> {
            var authorities = jwtGrantedAuthoritiesConverter.convert(jwt);
            var roles =jwt.getClaimAsStringList("spring_sec_roles");

            return Stream.concat(
                    authorities.stream(),
                    roles.stream()
                            .filter(role -> role != null && !role.isEmpty())
                            .map(role -> "ROLE_" + role.toUpperCase())
                            .map(SimpleGrantedAuthority::new)
                            .map(GrantedAuthority.class::cast)
            ).toList();
        });
        return jwtAuthenticationConverter;
    }

    @Bean
    public JwtDecoder jwtDecoder() {
        log.info("Creating JwtDecoder...");
        log.info("  - JWK Set URI: {}", properties.getDockerUri().concat("/protocol/openid-connect/certs"));
        log.info("  - Expected issuer: {}", properties.getOutUri());

        NimbusJwtDecoder decoder = NimbusJwtDecoder
                .withJwkSetUri(properties.getDockerUri().concat("/protocol/openid-connect/certs"))
                .build();

        decoder.setJwtValidator(
                JwtValidators.createDefaultWithIssuer(properties.getOutUri())
        );

        return decoder;
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("*"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

}