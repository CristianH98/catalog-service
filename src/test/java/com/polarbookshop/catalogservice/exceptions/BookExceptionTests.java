package com.polarbookshop.catalogservice.exceptions;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class BookExceptionTests {

    @Test
    void noSuchBookExceptionIncludesIsbn() {

        String isbn = "1234567890";
        NoSuchBookException exception = new NoSuchBookException(isbn);

        assertThat(exception.getMessage()).isEqualTo("The book with ISBN " + isbn + " does not exists");
    }

    @Test
    void bookAlreadyExistsExceptionIncludesIsbn() {

        String isbn = "1234567890";
        BookAlreadyExistsException exception = new BookAlreadyExistsException(isbn);

        assertThat(exception.getMessage()).isEqualTo("The book with ISBN " + isbn + " already exists");
    }
}
