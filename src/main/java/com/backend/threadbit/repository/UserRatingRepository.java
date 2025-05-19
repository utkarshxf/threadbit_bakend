package com.backend.threadbit.repository;

import com.backend.threadbit.model.UserRating;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRatingRepository extends MongoRepository<UserRating, String> {
    Optional<UserRating> findByUserId(String userId);
}