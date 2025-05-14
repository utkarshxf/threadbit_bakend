package com.backend.threadbit;

import com.backend.threadbit.model.Category;
import com.backend.threadbit.repository.CategoryRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.CrossOrigin;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Arrays;
import java.util.List;

@CrossOrigin(originPatterns = "*")
@SpringBootApplication
@EnableSwagger2
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
                for (String categoryName : categories) {
                    Category category = Category.builder()
                            .name(categoryName)
                            .build();
                    categoryRepository.save(category);
                }

                System.out.println("Database initialized with categories");
            }
        };
    }
}
