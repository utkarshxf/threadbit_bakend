package com.backend.therapist_backend.collection;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;
import nonapi.io.github.classgraph.json.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Builder
@Document(collection = "explore")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Explore {
    @Id
    private String doctorId;
    private String name;
    private List<String> experiences;
    private Integer fees;
    private String exp;
}
