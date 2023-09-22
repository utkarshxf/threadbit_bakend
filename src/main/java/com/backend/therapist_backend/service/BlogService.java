package com.backend.therapist_backend.service;

import com.backend.therapist_backend.collection.Blog;

import java.util.List;

public interface BlogService {
   List<Blog> getAllBlogs() ;

    String save(Blog blog);
}
