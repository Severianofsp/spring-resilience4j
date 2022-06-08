package com.poc.librarymanangement.controller;

import com.poc.librarymanangement.model.Book;
import com.poc.librarymanangement.service.LibrarymanagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/library")
public class LibrarymanagementController {

    @Autowired
    private LibrarymanagementService librarymanagementService;

    @PostMapping
    public String addBook(@RequestBody Book book) {
        return librarymanagementService.addBook(book);
    }

    @PostMapping("/ratelimit")
    public String addBookwithRateLimit(@RequestBody Book book) {
        return librarymanagementService.addBookwithRateLimit(book);
    }

    @GetMapping
    public List<Book> getSellersList() {
        return librarymanagementService.getBookList();
    }

    @GetMapping("/bulkhead")
    public List<Book> getSellersListBulkhead() {
        return librarymanagementService.getBookListBulkhead();
    }
}
