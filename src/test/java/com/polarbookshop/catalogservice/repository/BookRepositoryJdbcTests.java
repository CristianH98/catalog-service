package com.polarbookshop.catalogservice.repository;

import com.polarbookshop.catalogservice.config.DataConfig;
import com.polarbookshop.catalogservice.domain.Book;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.context.annotation.Import;
import org.springframework.data.jdbc.core.JdbcAggregateTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJdbcTest
@Import(DataConfig.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("integration")
class BookRepositoryJdbcTests {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private JdbcAggregateTemplate jdbcAggregateTemplate;

    @Test
    void shouldFindBookByIsbn() {
        var bookIsbn = "1234561237";
        var book = Book.of(bookIsbn, "Title", "Author", 12.90, "O'Reilly");
        jdbcAggregateTemplate.insert(book);
        Optional<Book> actualBook = bookRepository.findByIsbn(bookIsbn);

        assertThat(actualBook).isPresent();
        assertThat(book.isbn()).isEqualTo(actualBook.get().isbn());
    }

    @Test
    void shouldExistsByIsbn() {
        Book book = Book.of("1234567890123", "Title", "Author", 9.99, "Publisher");
        bookRepository.save(book);

        boolean exists = bookRepository.existsByIsbn("1234567890123");

        assertTrue(exists);
    }

    @Test
    @DirtiesContext
    void shouldTestDeleteByIsbn() {
        Book book = Book.of("1234567890123", "Title", "Author", 9.99, "Publisher");
        bookRepository.save(book);

        bookRepository.deleteByIsbn("1234567890123");

        Optional<Book> deletedBook = bookRepository.findByIsbn("1234567890123");
        assertFalse(deletedBook.isPresent());
    }

}
