package com.polarbookshop.catalogservice.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.polarbookshop.catalogservice.domain.Book;
import com.polarbookshop.catalogservice.exceptions.NoSuchBookException;
import com.polarbookshop.catalogservice.service.BookService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(BookController.class)
class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookService bookService;

    @Test
    void whenBookNotExistingReturn404() throws Exception {

        String isbn = "1234";
        when(bookService.viewBookDetails(isbn)).thenThrow(NoSuchBookException.class);

        mockMvc.perform(get("/books" + isbn))
                .andExpect(status().isNotFound());
    }

    @Test
    void whenGetBooksReturn200() throws Exception {

        List<Book> bookList = new ArrayList<>();
        when(bookService.viewBookList()).thenReturn(bookList);

        mockMvc.perform(get("/books"))
                .andExpect(status().isOk());
    }

    @Test
    void whenAddBookReturn201() throws Exception {

        Book  book = Book.of("1234567890123", "Vodka", "Carlos", 5.10, "O'Reilly");
        when(bookService.addBook(book)).thenReturn(book);

        mockMvc.perform(post("/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(toJson(book)))
                .andExpect(status().isCreated());
    }

    @Test
    void whenGetBooksReturnContent() throws Exception {

        List<Book> mockBookList = Arrays.asList(
                 Book.of("1234567890123", "Vodka", "Carlos", 5.10, "O'Reilly"),
                Book.of("4567890123459", "Whiskey", "John", 8.99, "Penguin Books")
        );

        when(bookService.viewBookList()).thenReturn(mockBookList);

        mockMvc.perform(get("/books"))
                .andExpect(status().isOk())  // Expect HTTP 200 OK status
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].isbn").value("1234567890123"))
                .andExpect(jsonPath("$[0].title").value("Vodka"))
                .andExpect(jsonPath("$[0].author").value("Carlos"))
                .andExpect(jsonPath("$[0].price").value(5.10))
                .andExpect(jsonPath("$[0].publisher").value("O'Reilly"))
                .andExpect(jsonPath("$[1].isbn").value("4567890123459"))
                .andExpect(jsonPath("$[1].title").value("Whiskey"))
                .andExpect(jsonPath("$[1].author").value("John"))
                .andExpect(jsonPath("$[1].price").value(8.99))
                .andExpect(jsonPath("$[1].publisher").value("Penguin Books"));

        verify(bookService, times(1)).viewBookList();

    }

    @Test
    void whenGetByIsbnEndpointReturnSingleBook() throws Exception {

        String isbn = "1234567890123";
        Book mockBook = Book.of(isbn, "Vodka", "Carlos", 5.10, "O'Reilly");

        when(bookService.viewBookDetails(isbn)).thenReturn(mockBook);

        mockMvc.perform(get("/books/{isbn}", isbn))
                .andExpect(status().isOk())  // Expect HTTP 200 OK status
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.isbn").value(isbn))
                .andExpect(jsonPath("$.title").value("Vodka"))
                .andExpect(jsonPath("$.author").value("Carlos"))
                .andExpect(jsonPath("$.price").value(5.10))
                .andExpect(jsonPath("$.publisher").value("O'Reilly"));

        verify(bookService, times(1)).viewBookDetails(isbn);
    }

    @Test
    void whenDeleteByIsbnEndpointReturnNoContent() throws Exception {

        String isbn = "1234567890123";

        mockMvc.perform(delete("/books/{isbn}", isbn))
                .andExpect(status().isNoContent());
        verify(bookService, times(1)).removeBook(isbn);
    }

    @Test
    void whenPutByIsbnEndpointReturnUpdatedBook() throws Exception {

        String isbn = "1234567890123";
        Book updatedBook = Book.of(isbn, "Updated Title", "Updated Author", 9.99, "Updated Publisher");

        when(bookService.editBook(eq(isbn), any(Book.class))).thenReturn(updatedBook);

        mockMvc.perform(put("/books/{isbn}", isbn)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(toJson(updatedBook)))
                .andExpect(status().isOk())  // Expect HTTP 200 OK status
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.isbn").value(isbn))
                .andExpect(jsonPath("$.title").value("Updated Title"))
                .andExpect(jsonPath("$.author").value("Updated Author"))
                .andExpect(jsonPath("$.price").value(9.99))
                .andExpect(jsonPath("$.publisher").value("Updated Publisher"));

        verify(bookService, times(1)).editBook(eq(isbn), any(Book.class));
    }

    private static String toJson(Book book) {

        ObjectMapper objectMapper = new ObjectMapper();

        try {
            return objectMapper.writeValueAsString(book);
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }
}
