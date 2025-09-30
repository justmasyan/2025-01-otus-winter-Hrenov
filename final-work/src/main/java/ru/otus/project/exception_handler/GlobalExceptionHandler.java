package ru.otus.project.exception_handler;

import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.otus.project.dto.ErrorResponseDto;

import java.security.SignatureException;
import java.util.Date;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<?> badCredentials(BadCredentialsException e) {
        return new ResponseEntity<>(new ErrorResponseDto("Неправильный логин или пароль", new Date()), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<?> expiredJwtException(ExpiredJwtException e) {
        return new ResponseEntity<>(new ErrorResponseDto("Токен авторизации истек. Авторизуйтесь повторно", new Date()), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(SignatureException.class)
    public ResponseEntity<?> signatureException(SignatureException e) {
        return new ResponseEntity<>(new ErrorResponseDto("Подпись токена авторизации ему не соответствует", new Date()), HttpStatus.UNAUTHORIZED);
    }

    //JwtExpired

    //SignatureException

    //MalformedJwtException

    //Пользователь не найден
}
