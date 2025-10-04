package ru.otus.project.exception_handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.security.oauth2.server.resource.InvalidBearerTokenException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.resource.NoResourceFoundException;
import ru.otus.project.dto.ErrorResponseDto;
import ru.otus.project.exception.AccountNotInHomeworkException;
import ru.otus.project.exception.CourseNotFoundException;
import ru.otus.project.exception.EmptyHomeworkException;
import ru.otus.project.exception.EmptyTopicsException;
import ru.otus.project.exception.HomeworkNotFoundException;
import ru.otus.project.exception.LectorAlreadyInCourseException;
import ru.otus.project.exception.LectorAlreadyInLectureException;
import ru.otus.project.exception.LectorNotFoundException;
import ru.otus.project.exception.LectorNotInCourseException;
import ru.otus.project.exception.LectorNotInHomeworkException;
import ru.otus.project.exception.LectorNotInLectureException;
import ru.otus.project.exception.LectureMaterialNotFoundException;
import ru.otus.project.exception.NoAvailableLectorsException;
import ru.otus.project.exception.NoZeroIdException;
import ru.otus.project.exception.RoleNotFoundException;
import ru.otus.project.exception.StudentAlreadyInGroupException;
import ru.otus.project.exception.StudentNotFoundException;
import ru.otus.project.exception.StudentNotInGroupException;
import ru.otus.project.exception.StudentsExistWhenGroupCreateException;
import ru.otus.project.exception.StudyGroupNotFoundException;
import ru.otus.project.exception.TopicNotFoundException;
import ru.otus.project.exception.TopicTitleExistException;
import ru.otus.project.exception.UndefinedRoleException;
import ru.otus.project.exception.ZeroIdException;

import java.util.Date;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Throwable.class)
    public ResponseEntity<?> throwableException(Throwable e) {
        return new ResponseEntity<>(new ErrorResponseDto("Неизвестная ошибка в сервисе", new Date()), HttpStatus.UNAUTHORIZED);
    }

    //отдельно должен обрабатываться
    @ExceptionHandler(AuthorizationDeniedException.class)
    public ResponseEntity<?> authorizationDeniedException(AuthorizationDeniedException e) {
        return new ResponseEntity<>(new ErrorResponseDto("Нет прав на этот ресурс", new Date()), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<?> badCredentials(BadCredentialsException e) {
        return new ResponseEntity<>(new ErrorResponseDto("Неправильный логин или пароль", new Date()), HttpStatus.UNAUTHORIZED);
    }

    //отдельно должен обрабатываться
    @ExceptionHandler(InvalidBearerTokenException.class)
    public ResponseEntity<?> invalidBearerTokenException(InvalidBearerTokenException e) {
        return new ResponseEntity<>(new ErrorResponseDto("Некорректный токен", new Date()), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<?> httpMessageNotReadableException(HttpMessageNotReadableException e) {
        return new ResponseEntity<>(new ErrorResponseDto("Неправильное тело запроса", new Date()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ModelAndView noResourceFoundException(NoResourceFoundException e) {
        return new ModelAndView("error_page");
    }

    @ExceptionHandler(LectorNotInLectureException.class)
    public ResponseEntity<?> lectorNotInLectureException(LectorNotInLectureException e) {
        return new ResponseEntity<>(new ErrorResponseDto("Преподаватель не ведет эту лекцию", new Date()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(LectorAlreadyInLectureException.class)
    public ResponseEntity<?> lectorAlreadyInLectureException(AuthorizationDeniedException e) {
        return new ResponseEntity<>(new ErrorResponseDto("Преподаватель уже ведет эту лекцию", new Date()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NoAvailableLectorsException.class)
    public ResponseEntity<?> notAvailableLectors(NoAvailableLectorsException e) {
        return new ResponseEntity<>(new ErrorResponseDto("Нет доступных преподавателей", new Date()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(LectorNotInHomeworkException.class)
    public ResponseEntity<?> lectorNotInHomeworkException(LectorNotInHomeworkException e) {
        return new ResponseEntity<>(new ErrorResponseDto("Преподаватель не проверяет это ДЗ", new Date()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(LectureMaterialNotFoundException.class)
    public ResponseEntity<?> lectureMaterialNotFoundException(LectureMaterialNotFoundException e) {
        return new ResponseEntity<>(new ErrorResponseDto("Ссылка к лекции не была найдена", new Date()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EmptyHomeworkException.class)
    public ResponseEntity<?> emptyHomeworkException(EmptyHomeworkException e) {
        return new ResponseEntity<>(new ErrorResponseDto("ДЗ не должно быть пустым", new Date()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AccountNotInHomeworkException.class)
    public ResponseEntity<?> accountNotInHomeworkException(AccountNotInHomeworkException e) {
        return new ResponseEntity<>(new ErrorResponseDto("Данный пользователь никак не связан с этой ДЗ", new Date()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ZeroIdException.class)
    public ResponseEntity<?> zeroIdException(ZeroIdException e) {
        return new ResponseEntity<>(new ErrorResponseDto("id не должен равняться 0", new Date()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(TopicTitleExistException.class)
    public ResponseEntity<?> topicTitleExistException(TopicTitleExistException e) {
        return new ResponseEntity<>(new ErrorResponseDto("Такая тема лекции уже существует", new Date()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EmptyTopicsException.class)
    public ResponseEntity<?> emptyTopicsException(EmptyTopicsException e) {
        return new ResponseEntity<>(new ErrorResponseDto("Темы лекций не должны быть пустыми", new Date()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(TopicNotFoundException.class)
    public ResponseEntity<?> topicNotFoundException(TopicNotFoundException e) {
        return new ResponseEntity<>(new ErrorResponseDto("Тема лекции не найдена", new Date()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UndefinedRoleException.class)
    public ResponseEntity<?> undefinedRoleException(UndefinedRoleException e) {
        return new ResponseEntity<>(new ErrorResponseDto("Неопределенная роль", new Date()), HttpStatus.BAD_REQUEST);
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
