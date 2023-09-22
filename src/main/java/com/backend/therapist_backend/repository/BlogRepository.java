package com.backend.therapist_backend.repository;

import com.backend.therapist_backend.collection.Blog;
import com.backend.therapist_backend.collection.Person;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BlogRepository extends MongoRepository<Blog,String> {
}
