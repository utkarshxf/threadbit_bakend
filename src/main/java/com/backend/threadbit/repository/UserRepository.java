package com.backend.threadbit.repository;

import com.backend.threadbit.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<User, String> {
    Optional<User> findByUsername(String username);
    Optional<User> findByPhoneNumber(String phone);
    boolean existsByUsername(String username);

    @Query("{ 'username': { $regex: ?0, $options: 'i' } }")
    List<User> findByUsernameContaining(String username);
}
