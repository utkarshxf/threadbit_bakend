package com.backend.therapist_backend.collection;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class Explore {
    private String name;
    private String doctorId;
    private List<String> experiences;
    private Integer fees;
    private String exp;
}
