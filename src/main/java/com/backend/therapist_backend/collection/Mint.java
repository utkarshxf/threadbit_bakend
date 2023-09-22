package com.backend.therapist_backend.collection;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;
import nonapi.io.github.classgraph.json.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@Document(collection = "mint")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Mint {
    @Id
    private String mintId;
    private String name;
    private String username;
    private String title;
    private String data;
    private Integer views;
}
