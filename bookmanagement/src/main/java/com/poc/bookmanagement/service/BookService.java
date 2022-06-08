package com.poc.bookmanagement.service;

import com.poc.bookmanagement.model.Book;

import java.util.List;

public interface BookService {

    public String addBook(Book book);

    public List<Book> retrieveBookList();
}
