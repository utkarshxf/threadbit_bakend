package com.backend.therapist_backend.repository;

import com.backend.therapist_backend.collection.Explore;
import com.backend.therapist_backend.collection.Person;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExploreRepository extends MongoRepository<Explore,String> {
}
