package com.backend.therapist_backend.repository;

import com.backend.therapist_backend.collection.Book;
import com.backend.therapist_backend.collection.Person;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends MongoRepository<Book,String> {


    List<Book> findBynameStartsWith(String name);
}
