package com.backend.therapist_backend.service;

import com.backend.therapist_backend.collection.Book;

import java.util.List;

public interface BookService {
   List<Book> getAllBooks() ;

    String save(Book book);

    List<Book> getBookStartWith(String name);

    Book updateTask(Book book, String key);
}
