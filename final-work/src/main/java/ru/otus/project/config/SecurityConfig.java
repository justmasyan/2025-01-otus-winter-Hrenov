package ru.otus.project.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import ru.otus.project.security.ExceptionHandlerFilter;
import ru.otus.project.security.JwtSecurityFilter;

@EnableWebSecurity
@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http,
                                                   ExceptionHandlerFilter exceptionHandlerFilter,
                                                   JwtSecurityFilter jwtSecurityFilter) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)

//                .csrf(csrf -> csrf.ignoringRequestMatchers("/h2-console/**"))
//      .headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable))

                .cors(AbstractHttpConfigurer::disable)
                .sessionManagement(manager -> manager.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/", "/courses/**", "/api/courses/**").permitAll()
                        .requestMatchers("/api/account/authentication", "/api/roles", "/api/students/new").permitAll()
                        .requestMatchers("/api/**").authenticated()
                        .requestMatchers("/account").authenticated()//остальные html страницы
                        .requestMatchers("/api/account/registration").hasRole("ADMIN")
                        .anyRequest().permitAll()// чтобы на неизвестные пути прилетало 404, а не 403
                ).anonymous(AbstractHttpConfigurer::disable)
                .addFilterBefore(exceptionHandlerFilter, LogoutFilter.class)
                .addFilterBefore(jwtSecurityFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(exceptionHandling ->
                        exceptionHandling.authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)));
        /*
        TODO:401 и 403
        Вызов за JWT - BeanValidating, чтобы в теле были username и password обязательно
        Если их нет, то ошибка 401
        Если они есть но неправильные, то BadCredentinal
        Вызов к ресурсу - если какие-то ошибки с токеном, то соотв ошибки
        Если нет такого заголовка, то 401
         */
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
}
