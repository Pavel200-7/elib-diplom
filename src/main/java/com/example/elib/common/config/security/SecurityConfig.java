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

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http)
            throws Exception {
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))  // ← добавить
                .csrf(csrf -> csrf.disable())
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

                        .requestMatchers(HttpMethod.GET, "/api/v1/authors", "/api/v1/authors/**").permitAll()

                        .requestMatchers(HttpMethod.GET, "/api/v1/books", "/api/v1/books/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/v1/books/page").permitAll()

                        .requestMatchers(HttpMethod.GET, "/api/v1/bookings", "/api/v1/bookings/**").authenticated()
                        .requestMatchers(HttpMethod.POST, "/api/v1/bookings/user", "/api/v1/bookings/user/**").authenticated()

                        .requestMatchers(HttpMethod.GET, "/api/v1/countries", "/api/v1/countries/**").permitAll()

                        .requestMatchers(HttpMethod.GET, "/api/v1/genres", "/api/v1/genres/**").permitAll()

                        .requestMatchers(HttpMethod.GET, "/api/v1/holders", "/api/v1/holders/**").permitAll()

                        .requestMatchers(HttpMethod.GET, "/api/v1/languages", "/api/v1/languages/**").permitAll()

                        .requestMatchers(HttpMethod.GET, "/api/v1/literature-groups", "/api/v1/literature-groups/**").permitAll()

                        .requestMatchers(HttpMethod.GET, "/api/v1/publishings", "/api/v1/publishings/**").permitAll()

                        .requestMatchers(HttpMethod.GET, "/api/v1/literature-groups", "/api/v1/literature-groups**").permitAll()

                        .requestMatchers(HttpMethod.GET,"/api/v1/users/**").authenticated()
                        .requestMatchers(HttpMethod.PUT,"/api/v1/users/**").authenticated()



                        .anyRequest().hasAnyRole(UserRoles.ADMIN.getName())
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
        configuration.setAllowedOrigins(List.of("http://localhost:5173"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

}