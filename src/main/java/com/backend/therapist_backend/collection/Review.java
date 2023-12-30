package com.backend.therapist_backend.collection;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Review{
    private String userId;
    private Boolean verified;
    private Boolean anonymous;
    private Boolean showText;
    private Integer teachingStyle;
    private Integer internalMarks;
    private Integer externalMark;
    private String  reviewText;
    private String  date;
}
