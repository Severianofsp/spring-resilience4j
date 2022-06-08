package com.poc.librarymanangement.service;

import com.poc.librarymanangement.model.Book;

import java.util.List;

public interface LibrarymanagementService {
    String addBook(Book book);

    String addBookwithRateLimit(Book book);

    List<Book> getBookList();

    List<Book> getBookListBulkhead();
}
