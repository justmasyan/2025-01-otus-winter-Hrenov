package ru.otus.hw.mongock.test_changelog;

import com.github.cloudyrock.mongock.ChangeLog;
import com.github.cloudyrock.mongock.ChangeSet;
import com.mongodb.DBRef;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.util.List;

import static ru.otus.hw.TestDataProvider.getDbAuthors;
import static ru.otus.hw.TestDataProvider.getDbBooks;
import static ru.otus.hw.TestDataProvider.getDbComments;
import static ru.otus.hw.TestDataProvider.getDbGenres;

@ChangeLog
public class TestDatabaseChangeLog {

    @ChangeSet(order = "001", id = "dropDb", author = "just_masyan", runAlways = true)
    public void dropDb(MongoDatabase db) {
        db.drop();
    }

    @ChangeSet(order = "003", id = "insertAuthors", author = "just_masyan")
    public void insertAuthors(MongoDatabase db) {
        MongoCollection<Document> authorCollection = db.getCollection("authors");
        List<Document> authorDocuments = getDbAuthors().stream()
                .map(author -> new Document()
                        .append("_id", author.getId())
                        .append("fullName", author.getFullName())
                ).toList();
        authorCollection.insertMany(authorDocuments);

    }

    @ChangeSet(order = "004", id = "insertGenres", author = "just_masyan")
    public void insertGenres(MongoDatabase db) {
        MongoCollection<Document> genresCollection = db.getCollection("genres");
        List<Document> genreDocuments = getDbGenres().stream()
                .map(genre -> new Document()
                        .append("_id", genre.getId())
                        .append("name", genre.getName())
                ).toList();
        genresCollection.insertMany(genreDocuments);
    }

    @ChangeSet(order = "005", id = "insertBooks", author = "just_masyan")
    public void insertBooks(MongoDatabase db) {
        MongoCollection<Document> booksCollection = db.getCollection("books");
        List<Document> bookDocuments = getDbBooks().stream().map(book -> {
            List<DBRef> genresDbRefs = book.getGenres().stream()
                    .map(genre -> new DBRef("genres", genre.getId()))
                    .toList();

            return new Document()
                    .append("_id", book.getId())
                    .append("title", book.getTitle())
                    .append("author", new DBRef("authors", book.getAuthor().getId()))
                    .append("genres", genresDbRefs);
        }).toList();
        booksCollection.insertMany(bookDocuments);
    }

    @ChangeSet(order = "006", id = "insertComments", author = "just_masyan")
    public void insertCommentaries(MongoDatabase db) {
        MongoCollection<Document> commentsCollection = db.getCollection("commentaries");
        List<Document> commentDocuments = getDbComments().stream().map(
                comment -> new Document()
                        .append("_id", comment.getId())
                        .append("book", new DBRef("books", comment.getBook().getId()))
                        .append("text", comment.getText())
        ).toList();
        commentsCollection.insertMany(commentDocuments);
    }
}
