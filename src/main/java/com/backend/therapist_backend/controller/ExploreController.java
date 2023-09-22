package com.backend.therapist_backend.controller;

import com.backend.therapist_backend.collection.Blog;
import com.backend.therapist_backend.collection.Explore;
import com.backend.therapist_backend.service.ExploreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/explore")
public class ExploreController {
    @Autowired
    private ExploreService exploreService;

    @GetMapping
    public List<Explore> getAllPerson(){
        return exploreService.getAllExplore();
    }

    @PostMapping
    public String save(@RequestBody Explore explore){
        return exploreService.save(explore);
    }

}
