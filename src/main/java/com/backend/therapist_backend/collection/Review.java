package com.backend.therapist_backend.collection;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;

import java.util.Date;

@Data
@Builder
public class Review{
    private String userId;
    private Boolean verified;
    private Boolean anonymous;
    private Integer teachingStyle;
    private Integer internalMarks;
    private Integer externalMark;
    private String  reviewText;
    private String  date;
}
