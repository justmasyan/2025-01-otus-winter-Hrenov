package ru.otus.hw.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.dto.AuthorDto;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.GenreDto;
import ru.otus.hw.services.BookService;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest(value = BookController.class,
        excludeAutoConfiguration = SecurityAutoConfiguration.class)
class BookControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockitoBean
    private BookService bookService;

    private List<AuthorDto> dbAuthors;

    private List<GenreDto> dbGenres;

    private List<BookDto> dbBooks;

    @BeforeEach
    void setUp() {
        dbAuthors = getDbAuthors();
        dbGenres = getDbGenres();
        dbBooks = getDbBooks();
    }

    @Test
    void findAllBooks() throws Exception {
        List<BookDto> exceptedBooks = dbBooks;
        when(bookService.findAll()).thenReturn(exceptedBooks);
        mvc.perform(get("/books"))
                .andExpect(view().name("books"))
                .andExpect(model().attribute("books", exceptedBooks));
    }

    @Test
    void findBookById() throws Exception {
        BookDto exceptedBook = dbBooks.get(0);
        when(bookService.findById(1L)).thenReturn(Optional.ofNullable(exceptedBook));
        mvc.perform(get("/books/%d".formatted(1L)))
                .andExpect(view().name("book"))
                .andExpect(model().attribute("book", exceptedBook));
    }

    @Test
    void insertBook() throws Exception {
        BookDto exceptedBook = new BookDto(0L, "NEW_BOOK", dbAuthors.get(0), dbGenres.subList(0, 2));
        Set<Long> genresIds = exceptedBook.getGenres().stream()
                .map(GenreDto::getId).collect(Collectors.toSet());

        String genresParam = genresIds.stream()
                .map(String::valueOf)
                .collect(Collectors.joining(","));

        mvc.perform(post("/books").with(csrf())
                .param("title", exceptedBook.getTitle())
                .param("authorId", String.valueOf(exceptedBook.getAuthor().getId()))
                .param("genresIds", genresParam)
        ).andExpect(view().name("redirect:/books"));

        verify(bookService, times(1))
                .insert(exceptedBook.getTitle(), exceptedBook.getAuthor().getId(), genresIds);
    }

    @Test
    void updateBook() throws Exception {
        BookDto exceptedBook = new BookDto(1L, "NEW_BOOK", dbAuthors.get(1), dbGenres.subList(4, 5));
        Set<Long> genresIds = exceptedBook.getGenres().stream()
                .map(GenreDto::getId).collect(Collectors.toSet());

        String genresParam = genresIds.stream()
                .map(String::valueOf)
                .collect(Collectors.joining(","));

        mvc.perform(put("/books/%d".formatted(exceptedBook.getId()))
                .with(csrf())
                .param("title", exceptedBook.getTitle())
                .param("authorId", String.valueOf(exceptedBook.getAuthor().getId()))
                .param("genresIds", genresParam)
        ).andExpect(view().name("redirect:/books"));

        verify(bookService, times(1))
                .update(exceptedBook.getId(), exceptedBook.getTitle(), exceptedBook.getAuthor().getId(), genresIds);
    }

    @Test
    void deleteBook() throws Exception {
        BookDto expectedBook = dbBooks.get(0);
        mvc.perform(delete("/books/%d".formatted(expectedBook.getId())).with(csrf()))
                .andExpect(view().name("redirect:/"));

        verify(bookService, times(1))
                .deleteById(expectedBook.getId());
    }

    private static List<BookDto> getDbBooks() {
        var dbAuthors = getDbAuthors();
        var dbGenres = getDbGenres();
        return getDbBooks(dbAuthors, dbGenres);
    }

    private static List<BookDto> getDbBooks(List<AuthorDto> dbAuthors, List<GenreDto> dbGenres) {
        return IntStream.range(1, 4).boxed()
                .map(id -> new BookDto(id,
                        "BookTitle_" + id,
                        dbAuthors.get(id - 1),
                        dbGenres.subList((id - 1) * 2, (id - 1) * 2 + 2)
                )).toList();
    }

    private static List<AuthorDto> getDbAuthors() {
        return IntStream.range(1, 4).boxed()
                .map(id -> new AuthorDto(id, "Author_" + id))
                .toList();
    }

    private static List<GenreDto> getDbGenres() {
        return IntStream.range(1, 7).boxed()
                .map(id -> new GenreDto(id, "Genre_" + id))
                .toList();
    }
}