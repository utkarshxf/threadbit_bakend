package com.backend.threadbit.repository;

import com.backend.threadbit.model.Review;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends MongoRepository<Review, String> {
    List<Review> findByUserIdOrderByCreatedAtDesc(String userId);
    List<Review> findByReviewerIdOrderByCreatedAtDesc(String reviewerId);
    List<Review> findByUserIdAndReviewerIdOrderByCreatedAtDesc(String userId, String reviewerId);
}
