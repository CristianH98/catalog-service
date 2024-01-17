package com.polarbookshop.catalogservice.api;

import com.polarbookshop.catalogservice.exceptions.BookAlreadyExistsException;
import com.polarbookshop.catalogservice.exceptions.NoSuchBookException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class BookControllerAdvice {
    @ExceptionHandler(NoSuchBookException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String bookNotFound(NoSuchBookException e) {
        return e.getMessage();
    }

    @ExceptionHandler(BookAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    String bookAlreadyExistsHandler(BookAlreadyExistsException ex) {
        return ex.getMessage();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    Map<String, String> handleValidationException(MethodArgumentNotValidException e) {

        var errors = new HashMap<String, String>();
        e.getBindingResult().getAllErrors().forEach(error -> {
            String field = ((FieldError) error).getField();
            String message = error.getDefaultMessage();
            errors.put(field, message);
            });

        return errors;
    }
}
