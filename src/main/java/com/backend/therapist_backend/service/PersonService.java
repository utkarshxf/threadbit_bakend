package com.backend.therapist_backend.service;


import com.backend.therapist_backend.collection.Person;
import org.bson.Document;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface PersonService {
    String save(Person person);


    void delete(String id);


    List<Person> getAllPerson();

    Optional<Person> getPersionById(String id);
}
