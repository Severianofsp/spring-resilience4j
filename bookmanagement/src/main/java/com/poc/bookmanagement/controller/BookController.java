package com.poc.bookmanagement.controller;

import com.poc.bookmanagement.model.Book;
import com.poc.bookmanagement.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/books")
public class BookController {

    @Autowired
    private BookService bookService;

    @PostMapping
    public String addBook(@RequestBody Book book) {
        return bookService.addBook(book);
    }

    @GetMapping
    public List<Book> retrieveBookList() {
        return bookService.retrieveBookList();
    }
}
