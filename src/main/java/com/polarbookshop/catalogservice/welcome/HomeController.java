package com.polarbookshop.catalogservice.welcome;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @GetMapping("/")
    public String getGreeting(){
        System.out.println("hello");
        return "Welcome to the book catalog";
    }
}
