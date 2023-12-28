package com.backend.therapist_backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.CrossOrigin;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
@CrossOrigin(originPatterns =  "*")
//@CrossOrigin(originPatterns = "https://deployed-backend.onrender.com:8080")
@SpringBootApplication
@EnableSwagger2
public class TherapistBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(TherapistBackendApplication.class, args);
    }

}
