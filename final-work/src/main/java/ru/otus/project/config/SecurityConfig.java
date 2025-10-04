package ru.otus.project.config;

import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandlerImpl;
import ru.otus.project.security.AuthenticationEntryPointImpl;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

@Configuration
public class SecurityConfig {

    @Value("${jwt.public.key}")
    private RSAPublicKey key;

    @Value("${jwt.private.key}")
    private RSAPrivateKey priv;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .cors(AbstractHttpConfigurer::disable)
                .sessionManagement(manager -> manager.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/account", "/lectures/**", "/groups/**", "/homeworks/**").authenticated()

                        .requestMatchers("/api/courses/new", "/api/courses/*/lector").hasRole("LECTOR")

                        .requestMatchers("/api/homeworks/*/lector", "/api/homeworks/*/new").hasRole("LECTOR")
                        .requestMatchers("/api/homeworks/**").authenticated()

                        .requestMatchers("/api/messages/**").authenticated()

                        .requestMatchers("/api/homework-statuses/**").authenticated()

                        .requestMatchers("/api/lectors/new", "/api/lectors/courses/*").permitAll()
                        .requestMatchers("/api/lectors/accounts/*/new").hasRole("LECTOR")
                        .requestMatchers("/api/lectors/**").authenticated()

                        .requestMatchers("/api/lectures/*/lector", "/api/lectures/*/new").hasRole("LECTOR")
                        .requestMatchers("/api/lectures/**").authenticated()

                        .requestMatchers("/api/lecture-materials/*").hasRole("LECTOR")
                        .requestMatchers("/api/lecture-materials/**").authenticated()

                        .requestMatchers("/api/lecture-statuses/**").authenticated()

                        .requestMatchers("/api/lecture-topics/new").hasRole("LECTOR")
                        .requestMatchers("/api/lecture-topics/**").permitAll()

                        .requestMatchers("/api/students/new").permitAll()
                        .requestMatchers("/api/students/accounts/*/new").hasRole("STUDENT")
                        .requestMatchers("/api/students/**").authenticated()

                        .requestMatchers("/api/groups/new").hasRole("LECTOR")
                        .requestMatchers("/api/groups/*/student").hasRole("STUDENT")
                        .requestMatchers("/api/groups/courses/*").permitAll()
                        .requestMatchers("/api/groups/**").authenticated()
                        .anyRequest().permitAll()
                ).anonymous(AbstractHttpConfigurer::disable)
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()))
                .sessionManagement((session) -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling((exceptions) -> {
                    AccessDeniedHandlerImpl deniedHandler = new AccessDeniedHandlerImpl();
                    deniedHandler.setErrorPage("/access_denied");
                    exceptions.authenticationEntryPoint(new AuthenticationEntryPointImpl())
                            .accessDeniedHandler(deniedHandler);
                });

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(10);
    }

    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter authoritiesConverter = new JwtGrantedAuthoritiesConverter();
        authoritiesConverter.setAuthorityPrefix("");
        authoritiesConverter.setAuthoritiesClaimName("authority");
        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(authoritiesConverter);
        return jwtAuthenticationConverter;
    }

    @Bean
    public JwtDecoder jwtDecoder() {
        return NimbusJwtDecoder.withPublicKey(this.key).build();
    }

    @Bean
    public JwtEncoder jwtEncoder() {
        JWK jwk = new RSAKey.Builder(this.key).privateKey(this.priv).build();
        JWKSource<SecurityContext> jwks = new ImmutableJWKSet<>(new JWKSet(jwk));
        return new NimbusJwtEncoder(jwks);
    }
}
