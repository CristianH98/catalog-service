package com.polarbookshop.catalogservice.data;

import com.polarbookshop.catalogservice.domain.Book;
import com.polarbookshop.catalogservice.repository.BookRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class BookDataLoaderTest {

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BookDataLoader bookDataLoader;

    @Test
    @SuppressWarnings("unchecked")
    void loadBookTestDataReplacesExistingAndSavesDefaults() {

        bookDataLoader.loadBookTestData();

        verify(bookRepository).deleteAll();

        ArgumentCaptor<List<Book>> booksCaptor = ArgumentCaptor.forClass(List.class);
        verify(bookRepository).saveAll(booksCaptor.capture());

        List<Book> savedBooks = booksCaptor.getValue();
        assertThat(savedBooks).hasSize(3);
        assertThat(savedBooks).extracting(Book::isbn)
                .containsExactly("1234567891", "1234567892", "1234567893");
        assertThat(savedBooks).extracting(Book::publisher)
                .containsExactly("O'Reilly", "Warner Bros", "O'Reilly");
    }
}
