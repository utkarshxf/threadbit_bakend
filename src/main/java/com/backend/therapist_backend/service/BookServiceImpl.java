package com.backend.therapist_backend.service;

import com.backend.therapist_backend.collection.Book;
import com.backend.therapist_backend.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class BookServiceImpl implements BookService {
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    @Override
    public String save(Book book) {

        return bookRepository.save(book).get_id();
    }

    @Override
    public List<Book> getBookStartWith(String name) {
        return bookRepository.findBynameStartsWith(name);
    }

    @Override
    public Book updateTask(Book book, String key) {
        Optional<Book> existingBookOptional =  bookRepository.findById(key);
        if (!existingBookOptional.isPresent()) throw new NoSuchElementException("Book not found with _id: " + key);

        Book existingTask = bookRepository.findById(key).get();
        existingTask.setReview(book.getReview());
        return bookRepository.save(existingTask);
    }
}
