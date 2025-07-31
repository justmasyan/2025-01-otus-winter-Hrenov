package ru.otus.hw.domain_mongo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Document(collection = "books")
public class BookMongo {

    @Id
    private ObjectId id;

    private String title;

    @DBRef
    private AuthorMongo author;

    @DBRef
    private List<GenreMongo> genres;
}
