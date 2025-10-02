package ru.otus.project;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import ru.otus.project.dto.AccountDto;
import ru.otus.project.repository.AccountRepository;

import java.util.Map;
import java.util.Optional;

@SpringBootApplication()
public class Application {

    public static void main(String[] args) {
        var context = SpringApplication.run(Application.class, args);
        var foo = context.getBean(AccountRepository.class);
        System.out.println();
    }
}