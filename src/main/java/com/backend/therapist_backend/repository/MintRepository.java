package com.backend.therapist_backend.repository;

import com.backend.therapist_backend.collection.Mint;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MintRepository extends MongoRepository<Mint,String> {
}
