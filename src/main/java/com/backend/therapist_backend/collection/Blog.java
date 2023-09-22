package com.backend.therapist_backend.collection;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Blog {
    private String name;
    private String username;
    private String imageUrl;
    private String title;
    private String data;
    private Integer views;
}
