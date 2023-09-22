package com.backend.therapist_backend.service;

import com.backend.therapist_backend.collection.Blog;
import com.backend.therapist_backend.collection.Explore;
import com.backend.therapist_backend.repository.ExploreRepository;
import com.backend.therapist_backend.repository.MintRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExploreServiceImpl implements ExploreService{

    @Autowired
    private ExploreRepository exploreRepository;
    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public List<Explore> getAllExplore() {
        return exploreRepository.findAll();
    }

    @Override
    public String save(Explore explore) {
        return exploreRepository.save(explore).getDoctorId();
    }
}
