package ru.otus.hw.mongock.changelog;

import com.github.cloudyrock.mongock.ChangeLog;
import com.github.cloudyrock.mongock.ChangeSet;
import com.mongodb.DBRef;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Commentary;
import ru.otus.hw.models.Genre;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

@ChangeLog
public class DatabaseChangeLog {

    private List<Book> books;

    private List<Author> authors;

    private List<Genre> genres;

    private List<Commentary> commentaries;

    @ChangeSet(order = "001", id = "dropDb", author = "just_masyan", runAlways = true)
    public void dropDb(MongoDatabase db) {
        db.drop();
    }

    @ChangeSet(order = "002", id = "prepareDocuments", author = "just_masyan")
    public void prepareDocuments(MongoDatabase db) {
        authors = getDbAuthors();
        genres = getDbGenres();
        books = getDbBooks(authors, genres);
        commentaries = getDbCommentaries(books);
    }

    @ChangeSet(order = "003", id = "insertAuthors", author = "just_masyan")
    public void insertAuthors(MongoDatabase db) {
        MongoCollection<Document> authorCollection = db.getCollection("authors");
        List<Document> newAuthors = new ArrayList<>();
        authors.forEach(author -> {
            newAuthors.add(new Document()
                    .append("_id", author.getId())
                    .append("fullName", author.getFullName())
            );
        });
        authorCollection.insertMany(newAuthors);

    }

    @ChangeSet(order = "004", id = "insertGenres", author = "just_masyan")
    public void insertGenres(MongoDatabase db) {
        MongoCollection<Document> genresCollection = db.getCollection("genres");
        List<Document> newGenres = new ArrayList<>();
        genres.forEach(genre -> {
            newGenres.add(new Document()
                    .append("_id", genre.getId())
                    .append("name", genre.getName())
            );
        });
        genresCollection.insertMany(newGenres);
    }

    @ChangeSet(order = "005", id = "insertBooks", author = "just_masyan")
    public void insertBooks(MongoDatabase db) {
        MongoCollection<Document> booksCollection = db.getCollection("books");
        List<Document> newBooks = new ArrayList<>();
        books.forEach(book -> {
            List<DBRef> genresDbRefs = book.getGenres().stream()
                    .map(genre -> new DBRef("genres", genre.getId()))
                    .toList();

            newBooks.add(new Document()
                    .append("_id", book.getId())
                    .append("title", book.getTitle())
                    .append("author", new DBRef("authors", book.getAuthor().getId()))
                    .append("genres", genresDbRefs)
            );
        });
        booksCollection.insertMany(newBooks);
    }

    @ChangeSet(order = "006", id = "insertComments", author = "just_masyan")
    public void insertCommentaries(MongoDatabase db) {
        MongoCollection<Document> commentsCollection = db.getCollection("commentaries");
        List<Document> newComments = new ArrayList<>();
        commentaries.forEach(commentaries -> newComments.add(new Document()
                .append("_id", commentaries.getId())
                .append("book", new DBRef("books", commentaries.getBook().getId()))
                .append("text", commentaries.getText())
        ));
        commentsCollection.insertMany(newComments);
    }

    private static List<Book> getDbBooks(List<Author> dbAuthors, List<Genre> dbGenres) {
        return IntStream.range(1, 4).boxed()
                .map(id -> new Book(id,
                        "BookTitle_" + id,
                        dbAuthors.get(id - 1),
                        dbGenres.subList((id - 1) * 2, (id - 1) * 2 + 2)
                )).toList();
    }

    private static List<Commentary> getDbCommentaries(List<Book> books) {
        return IntStream.range(1, 7).boxed()
                .map(id -> new Commentary(id, books.get((id - 1) / 2), "Comment_" + id))
                .toList();
    }

    private static List<Author> getDbAuthors() {
        return IntStream.range(1, 4).boxed()
                .map(id -> new Author(id, "Author_" + id))
                .toList();
    }

    private static List<Genre> getDbGenres() {
        return IntStream.range(1, 7).boxed()
                .map(id -> new Genre(id, "Genre_" + id))
                .toList();
    }
}
