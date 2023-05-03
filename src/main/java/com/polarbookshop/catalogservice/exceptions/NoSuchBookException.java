package com.polarbookshop.catalogservice.exceptions;

public class NoSuchBookException extends RuntimeException {
    public NoSuchBookException(String isbn) {
        super("The book with ISBN " + isbn + " does not exists");
    }
}
