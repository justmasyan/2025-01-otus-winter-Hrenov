package ru.otus.hw;

import com.github.cloudyrock.spring.v5.EnableMongock;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import ru.otus.hw.models.Author;
import ru.otus.hw.repositories.AuthorRepository;

@EnableMongock
@EnableMongoRepositories
@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        var context = SpringApplication.run(Application.class, args);
        var repo = context.getBean(AuthorRepository.class);
        Author author = new Author();
        author.setFullName("Pushkin2");
        repo.insert(author);
    }
}
