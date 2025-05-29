package com.backend.threadbit.config;

import com.backend.threadbit.model.*;
import com.backend.threadbit.repository.BidRepository;
import com.backend.threadbit.repository.CategoryRepository;
import com.backend.threadbit.repository.ItemRepository;
import com.backend.threadbit.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.List;

@Configuration
@Slf4j
@Profile("dev") // Only run in dev profile
public class DataInitializer {

    @Bean
    public CommandLineRunner initTestData(
            UserRepository userRepository,
            CategoryRepository categoryRepository,
            ItemRepository itemRepository,
            BidRepository bidRepository) {

        return args -> {
            // Check if we already have users (to avoid duplicate data on restarts)
            if (userRepository.count() > 0) {
                log.info("Database already populated with test data");
                return;
            }

            log.info("Initializing test data...");


            // Create test users
            User user1 = User.builder()
                    .username("alice")
                    .email("alice@example.com")
                    .password("password123")
                    .walletBalance("0")
                    .avatarUrl("https://randomuser.me/api/portraits/women/1.jpg")
                    .build();

            User user2 = User.builder()
                    .username("bob")
                    .email("bob@example.com")
                    .password("password123")
                    .avatarUrl("https://randomuser.me/api/portraits/men/1.jpg")
                    .build();

            userRepository.saveAll(Arrays.asList(user1, user2));
            log.info("Created test users");

            // Create categories if they don't exist
            List<Category> categories = categoryRepository.findAll();
            if (categories.isEmpty()) {
                categories = Arrays.asList(
                    Category.builder().name("Tops").build(),
                    Category.builder().name("Bottoms").build(),
                    Category.builder().name("Dresses").build(),
                    Category.builder().name("Outerwear").build(),
                    Category.builder().name("Accessories").build(),
                    Category.builder().name("Shoes").build()
                );
                categoryRepository.saveAll(categories);
                log.info("Created categories");
            }

            // Create test items
            Item item1 = Item.builder()
                    .title("Vintage Denim Jacket")
                    .description("Original Levi's denim jacket from the 90s in excellent condition")
                    .brand("Levi's")
                    .size(Size.M)
                    .condition(Condition.LIKE_NEW)
                    .color("Blue")
                    .startingPrice(45.00)
                    .currentPrice(45.00)
                    .imageUrls(Arrays.asList(
                            "https://example.com/images/jacket1.jpg",
                            "https://example.com/images/jacket2.jpg"
                    ))
                    .sellerId(user1.getId())
                    .categoryId(categories.get(3).getId()) // Outerwear
                    .endTime(ZonedDateTime.now(ZoneOffset.UTC).plusDays(7))
                    .status(Status.ACTIVE)
                    .build();

            Item item2 = Item.builder()
                    .title("Designer Sneakers")
                    .description("Limited edition sneakers, worn twice")
                    .brand("Nike")
                    .size(Size.SIZE_10)
                    .condition(Condition.GENTLY_USED)
                    .color("White/Red")
                    .startingPrice(80.00)
                    .currentPrice(80.00)
                    .imageUrls(Arrays.asList(
                            "https://example.com/images/sneakers1.jpg",
                            "https://example.com/images/sneakers2.jpg"
                    ))
                    .sellerId(user2.getId())
                    .categoryId(categories.get(5).getId()) // Shoes
                    .endTime(ZonedDateTime.now(ZoneOffset.UTC).plusDays(5))
                    .status(Status.ACTIVE)
                    .build();

            itemRepository.saveAll(Arrays.asList(item1, item2));
            log.info("Created test items");

            // Create some bids
            Bid bid1 = Bid.builder()
                    .itemId(item1.getId())
                    .userId(user2.getId())
                    .amount(50.00)
                    .build();

            item1.setCurrentPrice(50.00);
            itemRepository.save(item1);

            bidRepository.save(bid1);
            log.info("Created test bids");

            log.info("Test data initialization complete!");
        };
    }
}
