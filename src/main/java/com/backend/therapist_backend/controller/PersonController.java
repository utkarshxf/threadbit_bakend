package com.backend.therapist_backend.controller;


import com.backend.therapist_backend.collection.Person;
import com.backend.therapist_backend.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/person")
public class PersonController {
    @Autowired
    private PersonService personService;
    @PostMapping
    public String save(@RequestBody Person person){
        return personService.save(person);
    }
    @GetMapping
    public List<Person> getAllPerson(){
        return personService.getAllPerson();
    }
    @GetMapping("/name")
    public List<Person> getPersonStartWith(@RequestParam("name") String name){
        return personService.getPersonStartWith(name);
    }
    @DeleteMapping
    public void delete(@PathVariable String id){
        personService.delete(id);
    }


    @GetMapping("/id")
    public Optional<Person> getPersionById(@RequestParam("personId") String personId){
        return personService.getPersionById(personId);
    }
}
