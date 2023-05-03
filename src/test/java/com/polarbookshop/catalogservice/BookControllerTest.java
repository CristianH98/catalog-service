package com.polarbookshop.catalogservice;

import com.polarbookshop.catalogservice.api.BookController;
import com.polarbookshop.catalogservice.exceptions.NoSuchBookException;
import com.polarbookshop.catalogservice.service.BookService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BookController.class)
public class BookControllerTest {

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
}
