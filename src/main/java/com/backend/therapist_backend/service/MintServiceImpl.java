package com.backend.therapist_backend.service;

import com.backend.therapist_backend.collection.Mint;
import com.backend.therapist_backend.repository.MintRepository;
import com.backend.therapist_backend.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MintServiceImpl implements MintService{

    @Autowired
    private MintRepository mintsRepository;
    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public List<Mint> getAllMints() {
        return mintsRepository.findAll();
    }

    @Override
    public String save(Mint mint) {
        return mintsRepository.save(mint).getMintId();
    }
}
