package com.backend.therapist_backend.service;

import com.backend.therapist_backend.collection.Mint;

import java.util.List;

public interface MintService {
    List<Mint> getAllMints();

    String save(Mint mint);
}
