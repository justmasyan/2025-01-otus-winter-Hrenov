package ru.otus.hw.changelog;

import com.github.cloudyrock.mongock.ChangeLog;
import com.github.cloudyrock.mongock.ChangeSet;
import com.mongodb.DBRef;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bson.types.ObjectId;
import ru.otus.hw.domain_mongo.AuthorMongo;
import ru.otus.hw.domain_mongo.BookMongo;
import ru.otus.hw.domain_mongo.CommentaryMongo;
import ru.otus.hw.domain_mongo.GenreMongo;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

@ChangeLog(order = "001")
public class InitMongoDBDataChangeLog {

    private List<BookMongo> books;

    private List<AuthorMongo> authors;

    private List<GenreMongo> genres;

    private List<CommentaryMongo> commentaries;

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
        MongoCollection<org.bson.Document> authorCollection = db.getCollection("authors");
        List<Document> newAuthors = new ArrayList<>();
        authors.forEach(author -> newAuthors.add(new Document()
                .append("_id", author.getId())
                .append("fullName", author.getFullName())
        ));
        authorCollection.insertMany(newAuthors);

    }

    @ChangeSet(order = "004", id = "insertGenres", author = "just_masyan")
    public void insertGenres(MongoDatabase db) {
        MongoCollection<Document> genresCollection = db.getCollection("genres");
        List<Document> newGenres = new ArrayList<>();
        genres.forEach(genre -> newGenres.add(new Document()
                .append("_id", genre.getId())
                .append("name", genre.getName())
        ));
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

    private static List<BookMongo> getDbBooks(List<AuthorMongo> dbAuthors, List<GenreMongo> dbGenres) {
        return IntStream.range(1, 4).boxed()
                .map(id -> new BookMongo(new ObjectId(),
                        "BookTitle_" + id,
                        dbAuthors.get(id - 1),
                        dbGenres.subList((id - 1) * 2, (id - 1) * 2 + 2)
                )).toList();
    }

    private static List<CommentaryMongo> getDbCommentaries(List<BookMongo> books) {
        return IntStream.range(1, 7).boxed()
                .map(id -> new CommentaryMongo(new ObjectId(), books.get((id - 1) / 2), "Comment_" + id))
                .toList();
    }

    private static List<AuthorMongo> getDbAuthors() {
        return IntStream.range(1, 4).boxed()
                .map(id -> new AuthorMongo(new ObjectId(), "Author_" + id))
                .toList();
    }

    private static List<GenreMongo> getDbGenres() {
        return IntStream.range(1, 7).boxed()
                .map(id -> new GenreMongo(new ObjectId(), "Genre_" + id))
                .toList();
    }
}
