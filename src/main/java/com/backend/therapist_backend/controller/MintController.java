package com.backend.therapist_backend.controller;

import com.backend.therapist_backend.collection.Explore;
import com.backend.therapist_backend.collection.Mint;
import com.backend.therapist_backend.service.MintService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/mints")
public class MintController {
    @Autowired
    private MintService mintService;

    @GetMapping
    public List<Mint> getAllPerson(){
        return mintService.getAllMints();
    }

    @PostMapping
    public String save(@RequestBody Mint mint){
        return mintService.save(mint);
    }

}
