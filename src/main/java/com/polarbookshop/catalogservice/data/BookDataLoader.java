package com.polarbookshop.catalogservice.data;

import com.polarbookshop.catalogservice.domain.Book;
import com.polarbookshop.catalogservice.repository.BookRepository;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@Profile("testdata")
public class BookDataLoader {
    private final BookRepository bookRepository;
    public BookDataLoader(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void loadBookTestData() {
        var book1 = new Book("1234567891", "Northern Lights",
                "Lyra Silverstar", new BigDecimal("9.90"));
        var book2 = new Book("1234567892", "Polar Journey",
                "Iorek Polarson", new BigDecimal("12.90"));
        bookRepository.save(book1);
        bookRepository.save(book2);
    }
}
