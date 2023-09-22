package com.backend.therapist_backend.controller;

import com.backend.therapist_backend.collection.Blog;
import com.backend.therapist_backend.collection.Person;
import com.backend.therapist_backend.service.BlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/blogs")
public class BlogController {
    @Autowired
    private BlogService blogService;

    @GetMapping
    public List<Blog> getAllPerson(){
        return blogService.getAllBlogs();
    }

    @PostMapping
    public String save(@RequestBody Blog blog){
        return blogService.save(blog);
    }

}
