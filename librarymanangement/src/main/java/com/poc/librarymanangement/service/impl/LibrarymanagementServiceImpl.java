package com.poc.librarymanangement.service.impl;

import com.poc.librarymanangement.model.Book;
import com.poc.librarymanangement.service.LibrarymanagementService;
import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
public class LibrarymanagementServiceImpl implements LibrarymanagementService {
    Logger logger = LoggerFactory.getLogger(LibrarymanagementServiceImpl.class);

    private RestTemplate restTemplate;

    public LibrarymanagementServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;

    }

    /*
    Este método habilita o Circuit Breaker.
    Se o endpoint /books falhar ao responder, irá chamar o método fallbackForaddBook().
     */
    @Override
    @CircuitBreaker(name = "add", fallbackMethod = "fallbackForaddBook")
    public String addBook(Book book) {
        logger.error("Inside addbook call book service. ");
        return restTemplate.postForObject("/books", book, String.class);
    }

    /* Este método habilita o Rate Limiter. Se o endpoint /books atingir o
    limite definido nas configurações acima, irá chamar o método fallbackForRatelimitBook().
          limitForPeriod: 5
          limitRefreshPeriod: 10000 #ms
          timeoutDuration: 1000ms
     */
    @Override
    @RateLimiter(name = "add", fallbackMethod = "fallbackForRatelimitBook")
    public String addBookwithRateLimit(Book book) {
        String response = restTemplate.postForObject("/books", book, String.class);
        logger.error("Inside addbook, cause ");
        return response;
    }

    /*
    Este método habilita o Retry. Se o endpoint /books atingir o limite
    definido nas configurações acima, irá chamar o método fallbackRetry().
     */
    @Override
    @Retry(name = "get", fallbackMethod = "fallbackRetry")
    public List<Book> getBookList() {
        return restTemplate.getForObject("/books", List.class);
    }

    /*
    Este método habilita o Bulkhead. Se o endpoint /books atingir o limite definido
    nas configurações acima, irá chamar o método fallbackBulkhead().
          max-concurrent-calls: 10
          maxWaitDuration: 10ms
     */
    @Override
    @Bulkhead(name = "get", type = Bulkhead.Type.SEMAPHORE, fallbackMethod = "fallbackBulkhead")
    public List<Book> getBookListBulkhead() {
        logger.error("Inside getBookList bulk head");
        try {
            Thread.sleep(100000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return restTemplate.getForObject("/books", List.class);
    }

    public String fallbackForaddBook(Book book, Throwable t) {
        logger.error("Inside circuit breaker fallbackForaddBook, cause - {}", t.toString());
        return "Inside circuit breaker fallback method. Some error occurred while calling service for adding book";
    }

    public String fallbackForRatelimitBook(Book book, Throwable t) {
        logger.error("Inside fallbackForRatelimitBook, cause - {}", t.toString());
        return "Inside fallbackForRatelimitBook method. Some error occurred while calling service for adding book";
    }

    public List<Book> fallbackRetry(Throwable t) {
        logger.error("Inside fallbackRetry, cause - {}", t.toString());
        Book book   =   new Book();
        book.setAuthor("Default");
        book.setStatus("Default");
        book.setTitle("Default");
        List<Book> list =   new ArrayList<Book>();
        list.add(book);
        return list;
    }
    public List<Book> fallbackBulkhead(Throwable t) {
        logger.error("Inside fallbackBulkhead, cause - {}", t.toString());
        Book book   =   new Book();
        book.setAuthor("DefaultBulkhead");
        book.setStatus("DefaultBulkhead");
        book.setTitle("DefaultBulkhead");
        List<Book> list =   new ArrayList<Book>();
        list.add(book);
        return list;
    }
}
