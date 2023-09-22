package com.backend.therapist_backend.service;

import com.backend.therapist_backend.collection.Blog;
import com.backend.therapist_backend.collection.Explore;

import java.util.List;

public interface ExploreService {
    List<Explore> getAllExplore();

    String save(Explore explore);
}
