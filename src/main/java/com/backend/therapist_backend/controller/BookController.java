package com.backend.therapist_backend.controller;

import com.backend.therapist_backend.collection.Book;
import com.backend.therapist_backend.collection.Person;
import com.backend.therapist_backend.collection.Review;
import com.backend.therapist_backend.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/books")
public class BookController {
    @Autowired
    private BookService bookService;

    @GetMapping
    public List<Book> getAllBooks(){
        return bookService.getAllBooks();
    }

    @PostMapping
    public String Save(@RequestBody Book book){
        return bookService.save(book);
    }

    @GetMapping("/name")
    public List<Book> getBookStartWith(@RequestParam("name") String name){
        return bookService.getBookStartWith(name);
    }
    @PutMapping("/{id}")
    public Book modifyReview(@PathVariable String id, @RequestBody Book book) {
        return bookService.updateTask(book, id);
    }

}
