package com.backend.threadbit;

import com.backend.threadbit.model.Category;
import com.backend.threadbit.repository.CategoryRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Arrays;
import java.util.List;

@CrossOrigin(
        origins = { "https://threadbitwebsite-fqj4l.ondigitalocean.app/" ,"https://secondhand-threads.vercel.app" , "http://192.168.32.1:5173" ,"http://localhost:5173" , "https://10.244.72.46:8080" },
        allowCredentials = "true",
        methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS},
        allowedHeaders = {"Content-Type", "Authorization"}
)
@SpringBootApplication
@EnableScheduling
public class ThreadBitBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(ThreadBitBackendApplication.class, args);
    }
    @Bean
    public CommandLineRunner initDatabase(CategoryRepository categoryRepository) {
        return args -> {
            // Check if categories exist
            if (categoryRepository.count() == 0) {
                List<String> categories = Arrays.asList(
                        "Tops", "Bottoms", "Dresses", "Outerwear", "Accessories", "Shoes"
                );

                // Create categories
                for (int i = 0 ; i < categories.size() ; i++) {
                    Category category = Category.builder()
                            .name(categories.get(i))
                            .id(i+1)
                            .build();
                    categoryRepository.save(category);
                }

                System.out.println("Database initialized with categories");
            }
        };
    }
}
