package com.backend.therapist_backend.collection;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class Miscellaneous {
    private List<Mint> mints;
    private List<Blog> blogs;
}
