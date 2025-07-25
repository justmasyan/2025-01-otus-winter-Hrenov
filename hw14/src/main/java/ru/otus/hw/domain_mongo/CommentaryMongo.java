package ru.otus.hw.domain_mongo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "commentaries")
public class CommentaryMongo {

    @Id
    private ObjectId id;

    @DBRef
    private BookMongo book;

    private String text;
}
