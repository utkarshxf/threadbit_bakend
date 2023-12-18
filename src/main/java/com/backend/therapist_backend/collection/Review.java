package com.backend.therapist_backend.collection;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;

@Data
@Builder
public class Review{
    private String userId;
    private Boolean verified;
    private String reviewStart;
    private String reviewText;
    private String date;
    private String BookId;
}
