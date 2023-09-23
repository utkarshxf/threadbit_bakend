package com.backend.therapist_backend.collection;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;
@Data
@Builder
@Document(collection = "patient")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Person {
    @Id
    private String personId;
    private String firstName;
    private String lastName;
    private List<String> todayTask;
    private Integer activeDays;
    private ArrayList<Float> dailyReport;
}
