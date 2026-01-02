package com.polarbookshop.catalogservice.service;

import com.polarbookshop.catalogservice.domain.Book;
import com.polarbookshop.catalogservice.exceptions.BookAlreadyExistsException;
import com.polarbookshop.catalogservice.exceptions.NoSuchBookException;
import com.polarbookshop.catalogservice.repository.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Optional;
import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

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
    void shouldReturnBookList() {

        var books = new ArrayList<Book>();
        when(bookRepository.findAll()).thenReturn(books);
        var savedBooks = bookService.viewBookList();
        assertThat(savedBooks).isSameAs(books);
        verify(bookRepository).findAll();
    }

    @Test
    void shouldAddBook() {

        when(bookRepository.existsByIsbn(isbn)).thenReturn(false);
        when(bookRepository.save(book)).thenReturn(book);

        var savedBook = bookService.addBook(book);

        assertThat(savedBook).isEqualTo(book);
        verify(bookRepository).save(book);
    }

    @Test
    void shouldReturnBookDetails() {

        when(bookRepository.findByIsbn(isbn)).thenReturn(Optional.of(book));
        assertThat(bookService.viewBookDetails(isbn)).isEqualTo(book);
    }

    @Test
    void whenBookMissingThenViewBookDetailsThrows() {

        when(bookRepository.findByIsbn(isbn)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> bookService.viewBookDetails(isbn))
                .isInstanceOf(NoSuchBookException.class)
                .hasMessageContaining(isbn);
    }

    @Test
    void whenBookAlreadyExistsThenAddBookThrows() {

        when(bookRepository.existsByIsbn(isbn)).thenReturn(true);

        assertThatThrownBy(() -> bookService.addBook(book))
                .isInstanceOf(BookAlreadyExistsException.class)
                .hasMessageContaining(isbn);
        verify(bookRepository, never()).save(any(Book.class));
    }

    @Test
    void shouldRemoveBook() {

        bookService.removeBook(isbn);
        verify(bookRepository).deleteByIsbn(isbn);
    }

    @Test
    void shouldEditBook() {

        String isbn = "1234567890123";
        Book newBook = Book.of(isbn, "New Title", "New Author", 29.99, "New Publisher");

        when(bookRepository.findByIsbn(isbn)).thenReturn(Optional.empty());
        when(bookRepository.save(newBook)).thenReturn(newBook);

        Book result = bookService.editBook(isbn, newBook);

        assertThat(result).isEqualTo(newBook);
        verify(bookRepository, times(1)).findByIsbn(isbn);
        verify(bookRepository).save(newBook);
    }

    @Test
    void shouldEditExistingBookPreservingIdentityFields() {

        Instant createdDate = Instant.parse("2023-01-01T00:00:00Z");
        Instant lastModifiedDate = Instant.parse("2023-01-02T00:00:00Z");
        Book existingBook = new Book(
                99L,
                3,
                isbn,
                "Old Title",
                "Old Author",
                10.99,
                "Old Publisher",
                createdDate,
                lastModifiedDate);

        Book updatedBook = Book.of("9999999999", "New Title", "New Author", 29.99, "New Publisher");

        when(bookRepository.findByIsbn(isbn)).thenReturn(Optional.of(existingBook));
        when(bookRepository.save(any(Book.class))).thenAnswer(invocation -> invocation.getArgument(0, Book.class));

        Book result = bookService.editBook(isbn, updatedBook);

        ArgumentCaptor<Book> bookCaptor = ArgumentCaptor.forClass(Book.class);
        verify(bookRepository).save(bookCaptor.capture());
        Book savedBook = bookCaptor.getValue();

        assertThat(savedBook.id()).isEqualTo(existingBook.id());
        assertThat(savedBook.version()).isEqualTo(existingBook.version());
        assertThat(savedBook.isbn()).isEqualTo(existingBook.isbn());
        assertThat(savedBook.title()).isEqualTo(updatedBook.title());
        assertThat(savedBook.author()).isEqualTo(updatedBook.author());
        assertThat(savedBook.price()).isEqualTo(updatedBook.price());
        assertThat(savedBook.publisher()).isEqualTo(updatedBook.publisher());
        assertThat(savedBook.createdDate()).isEqualTo(existingBook.createdDate());
        assertThat(savedBook.lastModifiedDate()).isEqualTo(existingBook.lastModifiedDate());
        assertThat(result).isEqualTo(savedBook);
    }
}
