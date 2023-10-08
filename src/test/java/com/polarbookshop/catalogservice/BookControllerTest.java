package com.polarbookshop.catalogservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.polarbookshop.catalogservice.api.BookController;
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
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
        Book  book = Book.of("1234567890123", "Vodka", "Carlos", 5.10);
        when(bookService.addBook(book)).thenReturn(book);

        mockMvc.perform(post("/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(toJson(book)))
                .andExpect(status().isCreated());
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
