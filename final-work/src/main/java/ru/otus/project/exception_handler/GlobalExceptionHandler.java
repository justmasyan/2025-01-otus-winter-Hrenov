package ru.otus.project.exception_handler;

import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.otus.project.dto.ErrorResponseDto;
import ru.otus.project.exception.CourseNotFoundException;
import ru.otus.project.exception.HomeworkNotFoundException;
import ru.otus.project.exception.LectorAlreadyInCourseException;
import ru.otus.project.exception.LectorNotFoundException;
import ru.otus.project.exception.LectorNotInCourseException;
import ru.otus.project.exception.NoZeroIdException;
import ru.otus.project.exception.RoleNotFoundException;
import ru.otus.project.exception.StudentAlreadyInGroupException;
import ru.otus.project.exception.StudentNotFoundException;
import ru.otus.project.exception.StudentNotInGroupException;
import ru.otus.project.exception.StudentsExistWhenGroupCreateException;
import ru.otus.project.exception.StudyGroupNotFoundException;

import java.nio.charset.MalformedInputException;
import java.security.SignatureException;
import java.util.Date;

@RestControllerAdvice
public class GlobalExceptionHandler {

    //InvalidBearerTokenException возникает при новом JWT и старом JWT
    //NoResourceFoundException возникает при попытке зайти не недоступную страницу
    //LectorNotInLectureException
    //LectorAlreadyInLectureException
    //NotAvailableLectors
    //LectorNotInHomeworkException
    //EmptyHomeworkException
    //AccountNotInHomeworkException
    //InvalidBearerTokenException возникает при истечении JWT

    @ExceptionHandler(Throwable.class)
    public ResponseEntity<?> throwableException(Throwable e) {
        return new ResponseEntity<>(new ErrorResponseDto("Неизвестная ошибка в сервисе", new Date()), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(AuthorizationDeniedException.class)
    public ResponseEntity<?> authorizationDeniedException(AuthorizationDeniedException e) {
        return new ResponseEntity<>(new ErrorResponseDto("Нет прав на этот ресурс", new Date()), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<?> badCredentials(BadCredentialsException e) {
        return new ResponseEntity<>(new ErrorResponseDto("Неправильный логин или пароль", new Date()), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<?> expiredJwtException(ExpiredJwtException e) {
        return new ResponseEntity<>(new ErrorResponseDto("Токен авторизации истек. Авторизуйтесь повторно", new Date()), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(MalformedInputException.class)
    public ResponseEntity<?> malformedInputException(MalformedInputException e) {
        return new ResponseEntity<>(new ErrorResponseDto("Токен не соответствует стандарту", new Date()), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(SignatureException.class)
    public ResponseEntity<?> signatureException(SignatureException e) {
        return new ResponseEntity<>(new ErrorResponseDto("Подпись токена авторизации ему не соответствует", new Date()), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(CourseNotFoundException.class)
    public ResponseEntity<?> courseNotFoundException(CourseNotFoundException e) {
        return new ResponseEntity<>(new ErrorResponseDto("Курс не найден", new Date()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HomeworkNotFoundException.class)
    public ResponseEntity<?> homeworkNotFoundException(HomeworkNotFoundException e) {
        return new ResponseEntity<>(new ErrorResponseDto("Домашняя работа не найден", new Date()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(LectorAlreadyInCourseException.class)
    public ResponseEntity<?> lectorAlreadyInCourseException(LectorAlreadyInCourseException e) {
        return new ResponseEntity<>(new ErrorResponseDto("Лектор уже преподает на курсе", new Date()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(LectorNotInCourseException.class)
    public ResponseEntity<?> lectorNotInCourseException(LectorNotInCourseException e) {
        return new ResponseEntity<>(new ErrorResponseDto("Лектор не преподает на этом курсе", new Date()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(LectorNotFoundException.class)
    public ResponseEntity<?> lectorNotFoundException(LectorNotFoundException e) {
        return new ResponseEntity<>(new ErrorResponseDto("Преподаватель не найден", new Date()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NoZeroIdException.class)
    public ResponseEntity<?> notZeroIdWhenInsertException(NoZeroIdException e) {
        return new ResponseEntity<>(new ErrorResponseDto("Ненулевой id при создании", new Date()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(RoleNotFoundException.class)
    public ResponseEntity<?> roleNotFoundException(RoleNotFoundException e) {
        return new ResponseEntity<>(new ErrorResponseDto("Роль не найдена", new Date()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(StudentAlreadyInGroupException.class)
    public ResponseEntity<?> studentAlreadyInGroupException(StudentAlreadyInGroupException e) {
        return new ResponseEntity<>(new ErrorResponseDto("Студент уже состоит в учебной группе", new Date()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(StudentNotFoundException.class)
    public ResponseEntity<?> studentNotFoundException(StudentNotFoundException e) {
        return new ResponseEntity<>(new ErrorResponseDto("Студент не найден", new Date()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(StudentNotInGroupException.class)
    public ResponseEntity<?> studentNotInGroupException(StudentNotInGroupException e) {
        return new ResponseEntity<>(new ErrorResponseDto("Студент и так не состоит в учебной группе", new Date()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(StudentsExistWhenGroupCreateException.class)
    public ResponseEntity<?> studentsExistWhenGroupCreateException(StudentsExistWhenGroupCreateException e) {
        return new ResponseEntity<>(new ErrorResponseDto("Учебная группа изначально должна быть без студентов", new Date()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(StudyGroupNotFoundException.class)
    public ResponseEntity<?> studyGroupNotFoundException(StudyGroupNotFoundException e) {
        return new ResponseEntity<>(new ErrorResponseDto("Учебная группа не найдена", new Date()), HttpStatus.BAD_REQUEST);
    }
}
