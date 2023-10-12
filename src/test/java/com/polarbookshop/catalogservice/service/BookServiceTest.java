package com.polarbookshop.catalogservice.service;

import com.polarbookshop.catalogservice.domain.Book;
import com.polarbookshop.catalogservice.repository.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BookService bookService;

    private Book book;

    private String isbn;

    @BeforeEach
    void setUp() {
        book = Book.of("1234567890", "Title", "Author",9.90, "O'Reilly");
        isbn = book.isbn();
    }

    @Test
    void viewBookList() {
        var books = new ArrayList<Book>();
        when(bookRepository.findAll()).thenReturn(books);
        var savedBooks = bookService.viewBookList();
        assertThat(savedBooks).isNotNull();
    }

    @Test
    void addBook() {
        when(bookRepository.save(book)).thenReturn(book);
        assertThat(bookService.addBook(book)).isNotNull();
    }

    @Test
    void viewBookDetails() {
        when(bookRepository.findByIsbn(isbn)).thenReturn(Optional.of(book));
        assertThat(bookService.viewBookDetails(isbn)).isEqualTo(book);
    }

    @Test
    void removeBook() {
        bookService.removeBook(isbn);
        verify(bookRepository).deleteByIsbn(isbn);
    }
}