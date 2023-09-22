package com.backend.therapist_backend.service;

import com.backend.therapist_backend.collection.Blog;
import com.backend.therapist_backend.repository.BlogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BlogServiceImpl implements BlogService{
    @Autowired
    private BlogRepository blogRepository;
    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public List<Blog> getAllBlogs() {
        return blogRepository.findAll();
    }

    @Override
    public String save(Blog blog) {
        return blogRepository.save(blog).getBlogId();
    }
}
