package com.backend.threadbit.repository;

import com.backend.threadbit.model.Location;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LocationRepository extends MongoRepository<Location, String> {
    List<Location> findByUserId(String userId);
    Optional<Location> findByUserIdAndIsCurrentLocationTrue(String userId);
}
