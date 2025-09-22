package com.backend.threadbit.config;

//import com.backend.threadbit.model.*;
//import com.backend.threadbit.model.neo4j.Artist;
//import com.backend.threadbit.model.neo4j.Artwork;
//import com.backend.threadbit.repository.BidRepository;
//import com.backend.threadbit.repository.CategoryRepository;
//import com.backend.threadbit.repository.ItemRepository;
//import com.backend.threadbit.repository.UserRepository;
//import com.backend.threadbit.repository.neo4j.ArtistRepository;
import lombok.extern.slf4j.Slf4j;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.Profile;
//
//import java.time.LocalDateTime;
//import java.time.ZoneOffset;
//import java.time.ZonedDateTime;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.Collections;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;

@Configuration
@Slf4j
//@Profile("dev") // Only run in dev profile
public class DataInitializer {

    /**
     * Helper method to create a merchandise item for an artwork
     */
//    private Item createMerchandiseItem(
//            String id,
//            String title,
//            String description,
//            String brand,
//            List<Size> sizes,
//            Condition condition,
//            List<String> colors,
//            String imageUrl,
//            String sellerId,
//            Integer categoryId,
//            Integer buyNowPrice,
//            Integer originalPrice
//    ) {
//        return Item.builder()
//                .id(id.replace(" ", "_"))
//                .title(title) // Use ID as title
//                .description(description)
//                .brand(brand)
//                .size(sizes)
//                .condition(condition)
//                .color(colors)
//                .startingPrice(buyNowPrice)
//                .currentPrice(buyNowPrice)
//                .imageUrls(Collections.singletonList(imageUrl))
//                .sellerId(sellerId)
//                .categoryId(categoryId)
//                .endTime(ZonedDateTime.now(ZoneOffset.UTC).plusYears(10)) // Long expiry
//                .status(Status.ACTIVE)
//                .itemType(ItemType.ARTWORK_MERCHANDISE)
//                .originalPrice(originalPrice)
//                .buyNowPrice(buyNowPrice)
//                .build();
//    }

//    @Bean
//    public CommandLineRunner initTestData(
//            UserRepository userRepository,
//            CategoryRepository categoryRepository,
//            ItemRepository itemRepository,
//            BidRepository bidRepository,
//            ArtistRepository artistRepository) {

//        return args -> {
            // Check if we already have users (to avoid duplicate data on restarts)
//            if (userRepository.count() > 0) {
//                log.info("Database already populated with test data");
//                return;
//            }

//            log.info("Initializing test data...");


//            // Create test users
//            User user1 = User.builder()
//                    .username("alice")
//                    .email("alice@example.com")
//                    .description("sdadsa")
//                    .walletBalance("0")
//                    .avatarUrl("https://randomuser.me/api/portraits/women/1.jpg")
//                    .build();
//
//            User user2 = User.builder()
//                    .username("bob")
//                    .email("bob@example.com")
//                    .description("adsad")
//                    .avatarUrl("https://randomuser.me/api/portraits/men/1.jpg")
//                    .build();

//            userRepository.saveAll(Arrays.asList(user1, user2));
//            log.info("Created test users");
//
//            // Create categories if they don't exist
//            List<Category> categories = categoryRepository.findAll();
//            if (categories.isEmpty()) {
//                categories = Arrays.asList(
//                    Category.builder().id(1).name("Tops").build(),
//                    Category.builder().id(2).name("Bottoms").build(),
//                    Category.builder().id(3).name("Dresses").build(),
//                    Category.builder().id(4).name("Outerwear").build(),
//                    Category.builder().id(5).name("Accessories").build(),
//                    Category.builder().id(6).name("Shoes").build(),
//                    Category.builder().id(7).name("Posters").build(),
//                    Category.builder().id(8).name("Stickers").build()
//                );
//                categoryRepository.saveAll(categories);
//                log.info("Created categories");
//            }

//            // Create test items
//            Item item1 = Item.builder()
//                    .title("Vintage Denim Jacket")
//                    .description("Original Levi's denim jacket from the 90s in excellent condition")
//                    .brand("Levi's")
//                    .size(List.of(Size.M))
//                    .condition(Condition.LIKE_NEW)
//                    .color(List.of("Blue"))
//                    .startingPrice(45.00)
//                    .currentPrice(45.00)
//                    .imageUrls(Arrays.asList(
//                            "https://example.com/images/jacket1.jpg",
//                            "https://example.com/images/jacket2.jpg"
//                    ))
//                    .sellerId(user1.getId())
//                    .categoryId(categories.get(3).getId()) // Outerwear
//                    .endTime(ZonedDateTime.now(ZoneOffset.UTC).plusDays(7))
//                    .status(Status.ACTIVE)
//                    .build();
//
//            Item item2 = Item.builder()
//                    .title("Designer Sneakers")
//                    .description("Limited edition sneakers, worn twice")
//                    .brand("Nike")
//                    .size(List.of(Size.SIZE_10))
//                    .condition(Condition.GENTLY_USED)
//                    .color(List.of("White/Red"))
//                    .startingPrice(80.00)
//                    .currentPrice(80.00)
//                    .imageUrls(Arrays.asList(
//                            "https://example.com/images/sneakers1.jpg",
//                            "https://example.com/images/sneakers2.jpg"
//                    ))
//                    .sellerId(user2.getId())
//                    .categoryId(categories.get(5).getId()) // Shoes
//                    .endTime(ZonedDateTime.now(ZoneOffset.UTC).plusDays(5))
//                    .status(Status.ACTIVE)
//                    .build();
//
//            itemRepository.saveAll(Arrays.asList(item1, item2));
//            log.info("Created test items");
//
//            // Create some bids
//            Bid bid1 = Bid.builder()
//                    .itemId(item1.getId())
//                    .userId(user2.getId())
//                    .amount(50.00)
//                    .build();
//
//            item1.setCurrentPrice(50.00);
//            itemRepository.save(item1);
//
//            bidRepository.save(bid1);
//            log.info("Created test bids");
//
//            // Migrate artists from Neo4j to MongoDB users
//            log.info("Starting migration of artists from Neo4j to MongoDB users...");
//
//            // Get total count of artists for progress tracking
//            long totalArtists = artistRepository.count();
//            log.info("Found {} artists in Neo4j", totalArtists);
//
//            // Define batch size
//            final int BATCH_SIZE = 500;
//            int processedCount = 0;
//            int existingCount = 0;
//            int migratedCount = 0;
//            int errorCount = 0;
//
//            // Process artists in batches
//            for (int offset = 0; offset < totalArtists; offset += BATCH_SIZE) {
//                log.info("Processing batch starting at offset {} (batch size: {})", offset, BATCH_SIZE);
//
//                List<Artist> artistBatch;
//                try {
//                    artistBatch = artistRepository.findWithPagination(offset, BATCH_SIZE);
//                } catch (Exception e) {
//                    log.error("Error retrieving artists batch at offset {}: {}", offset, e.getMessage());
//                    errorCount++;
//                    continue;
//                }
//
//                List<User> userBatch = new ArrayList<>();
//
//                for (Artist artist : artistBatch) {
//                    try {
//                        processedCount++;
//
//                        // Create username as name@id
//                        String username = artist.getName().replace(" ", "_") + "@" + artist.getId();
//
//                        // Check if user with this username already exists
//                        if (userRepository.existsByUsername(username)) {
//                            log.debug("User with username {} already exists, skipping", username);
//                            existingCount++;
//                            continue;
//                        }
//
//                        // Create social media list with Wikipedia URL
//                        List<Map<String, String>> socialMedia = new ArrayList<>();
//                        if (artist.getWikipedia_url() != null && !artist.getWikipedia_url().isEmpty()) {
//                            Map<String, String> wikipediaEntry = new HashMap<>();
//                            wikipediaEntry.put("type", "wikipedia");
//                            wikipediaEntry.put("url", artist.getWikipedia_url());
//                            socialMedia.add(wikipediaEntry);
//                        }
//
//                        // Create user from artist with basic required fields
//                        User.UserBuilder userBuilder = User.builder()
//                                .id(artist.getId()) // Store original Neo4j artist ID
//                                .username(username)
//                                .name(artist.getName())
//                                .walletBalance("0")
//                                .email("threadbit.app@gmail.com")
//                                .isVerified(true)
//                                .socialMedia(socialMedia);
//
//                        // Only add optional fields if they exist in the artist object
//                        if (artist.getDescription() != null && !artist.getDescription().isEmpty()) {
//                            userBuilder.description(artist.getDescription());
//                        }
//                        if (artist.getImage_url() != null && !artist.getImage_url().isEmpty()) {
//                            userBuilder.avatarUrl(artist.getImage_url());
//                        }
//                        if (artist.getBirth_date() != null && !artist.getBirth_date().isEmpty()) {
//                            userBuilder.birthDate(artist.getBirth_date());
//                        }
//                        if (artist.getDeath_date() != null && !artist.getDeath_date().isEmpty()) {
//                            userBuilder.deathDate(artist.getDeath_date());
//                        }
//                        if (artist.getNationality() != null && !artist.getNationality().isEmpty()) {
//                            userBuilder.nationality(artist.getNationality());
//                        }
//                        if (artist.getNotable_works() != null && !artist.getNotable_works().isEmpty()) {
//                            userBuilder.notableWorks(artist.getNotable_works());
//                        }
//                        if (artist.getArt_movement() != null && !artist.getArt_movement().isEmpty()) {
//                            userBuilder.artMovement(artist.getArt_movement());
//                        }
//                        if (artist.getEducation() != null && !artist.getEducation().isEmpty()) {
//                            userBuilder.education(artist.getEducation());
//                        }
//                        if (artist.getAwards() != null && !artist.getAwards().isEmpty()) {
//                            userBuilder.awards(artist.getAwards());
//                        }
//
//                        User artistUser = userBuilder.build();
//                        userBatch.add(artistUser);
//                        migratedCount++;
//
//                    } catch (Exception e) {
//                        log.error("Error processing artist {}: {}", artist.getId(), e.getMessage());
//                        errorCount++;
//                    }
//                }
//
//                // Save batch of users
//                if (!userBatch.isEmpty()) {
//                    try {
//                        userRepository.saveAll(userBatch);
//                        log.info("Saved batch of {} users (total migrated: {}, existing: {}, errors: {})",
//                                userBatch.size(), migratedCount, existingCount, errorCount);
//                    } catch (Exception e) {
//                        log.error("Error saving batch of users: {}", e.getMessage());
//                        // Try to save users one by one to avoid losing the entire batch
//                        for (User user : userBatch) {
//                            try {
//                                userRepository.save(user);
//                                log.debug("Saved user {} individually", user.getUsername());
//                            } catch (Exception ex) {
//                                log.error("Error saving individual user {}: {}", user.getUsername(), ex.getMessage());
//                                errorCount++;
//                                migratedCount--;
//                            }
//                        }
//                    }
//                }
//
//                log.info("Progress: {}/{} artists processed ({}%)",
//                        processedCount, totalArtists, Math.round((processedCount * 100.0) / totalArtists));
//            }
//
//            log.info("Migration complete: {} artists processed, {} migrated, {} already existed, {} errors",
//                    processedCount, migratedCount, existingCount, errorCount);

//            // Create merchandise items for artworks
//            log.info("Starting creation of merchandise items for artworks...");
//
//            // Process artists in batches to get their artworks
//            int artworkProcessedCount = 0;
//            int merchandiseCreatedCount = 0;
//            int artworkErrorCount = 0;
//
//            for (int offset = 0; offset < totalArtists; offset += BATCH_SIZE) {
//                log.info("Processing artwork batch starting at offset {} (batch size: {})", offset, BATCH_SIZE);
//
//                List<Artist> artistBatch;
//                try {
//                    artistBatch = artistRepository.findWithPagination(offset, BATCH_SIZE);
//                } catch (Exception e) {
//                    log.error("Error retrieving artists batch at offset {}: {}", offset, e.getMessage());
//                    artworkErrorCount++;
//                    continue;
//                }
//
//                List<Item> merchandiseItems = new ArrayList<>();
//
//                for (Artist artist : artistBatch) {
//                    try {
//                        // Get artworks for this artist
//                        List<Artwork> artworks = artistRepository.getArtworksByArtistId(artist.getId());
//
//                        for (Artwork artwork : artworks) {
//                            artworkProcessedCount++;
//
//                            // Skip if artwork doesn't have required fields
//                            if (artwork.getTitle() == null || artwork.getImage_url() == null) {
//                                log.debug("Skipping artwork {} due to missing title or image", artwork.getId());
//                                continue;
//                            }
//
//                            // Create merchandise items for this artwork
//                            // 1. Hoodie (Category ID: 1 - Tops)
//                            Item hoodie = createMerchandiseItem(
//                                    artwork.getTitle() + "_Hoodie_" + artwork.getId(),
//                                    artwork.getTitle() + " Hoodie",
//                                    "Hoodie featuring " + artwork.getTitle() + " by " + artist.getName(),
//                                    artwork.getTitle(),
//                                    Arrays.asList(Size.XS, Size.S, Size.M, Size.L, Size.XL, Size.XXL),
//                                    Condition.NEW_WITH_TAGS,
//                                    Arrays.asList("Black", "White"),
//                                    artwork.getImage_url(),
//                                    artist.getId(),
//                                    1, // Tops category
//                                    2500, // Buy now price (INR)
//                                    2800  // Original price (INR)
//                            );
//                            merchandiseItems.add(hoodie);
//
//                            // 2. T-shirt (Category ID: 1 - Tops)
//                            Item tshirt = createMerchandiseItem(
//                                    artwork.getTitle() + "_Tshirt_" + artwork.getId(),
//                                    artwork.getTitle()+" T-shirt",
//                                    "T-shirt featuring " + artwork.getTitle() + " by " + artist.getName(),
//                                    artwork.getTitle(),
//                                    Arrays.asList(Size.XS, Size.S, Size.M, Size.L, Size.XL, Size.XXL),
//                                    Condition.NEW_WITH_TAGS,
//                                    Arrays.asList("Black", "White"),
//                                    artwork.getImage_url(),
//                                    artist.getId(),
//                                    1, // Tops category
//                                    1200, // Buy now price (INR)
//                                    1500  // Original price (INR)
//                            );
//                            merchandiseItems.add(tshirt);
//
//                            // 3. Poster (Category ID: 7 - Posters)
//                            Item poster = createMerchandiseItem(
//                                    artwork.getTitle() + "_Poster_" + artwork.getId(),
//                                    artwork.getTitle() + " Poster",
//                                    "Poster featuring " + artwork.getTitle() + " by " + artist.getName(),
//                                    artwork.getTitle(),
//                                    Arrays.asList(Size.XS, Size.S, Size.M, Size.L, Size.XL, Size.XXL), // No size for posters
//                                    Condition.NEW_WITH_TAGS,
//                                    Arrays.asList("Full Color"),
//                                    artwork.getImage_url(),
//                                    artist.getId(),
//                                    7, // Posters category
//                                    500, // Buy now price (INR)
//                                    700  // Original price (INR)
//                            );
//                            merchandiseItems.add(poster);
//
//                            // 4. Sticker (Category ID: 8 - Stickers)
//                            Item sticker = createMerchandiseItem(
//                                    artwork.getTitle() + "_Sticker_" + artwork.getId(),
//                                    artwork.getTitle() + " Sticker",
//                                    "Sticker featuring " + artwork.getTitle() + " by " + artist.getName(),
//                                    artwork.getTitle() + " Sticker",
//                                    Arrays.asList(Size.XS, Size.S, Size.M, Size.L, Size.XL, Size.XXL), // No size for stickers
//                                    Condition.NEW_WITH_TAGS,
//                                    Arrays.asList("Full Color"),
//                                    artwork.getImage_url(),
//                                    artist.getId(),
//                                    8, // Stickers category
//                                    100, // Buy now price (INR)
//                                    150  // Original price (INR)
//                            );
//                            merchandiseItems.add(sticker);
//
//                            merchandiseCreatedCount += 4; // Added 4 items per artwork
//                        }
//                    } catch (Exception e) {
//                        log.error("Error processing artworks for artist {}: {}", artist.getId(), e.getMessage());
//                        artworkErrorCount++;
//                    }
//                }
//
//                // Save batch of merchandise items
//                if (!merchandiseItems.isEmpty()) {
//                    try {
//                        itemRepository.saveAll(merchandiseItems);
//                        log.info("Saved batch of {} merchandise items", merchandiseItems.size());
//                    } catch (Exception e) {
//                        log.error("Error saving batch of merchandise items: {}", e.getMessage());
//                        // Try to save items one by one to avoid losing the entire batch
//                        for (Item item : merchandiseItems) {
//                            try {
//                                itemRepository.save(item);
//                                log.debug("Saved item {} individually", item.getId());
//                            } catch (Exception ex) {
//                                log.error("Error saving individual item {}: {}", item.getId(), ex.getMessage());
//                                merchandiseCreatedCount--;
//                                artworkErrorCount++;
//                            }
//                        }
//                    }
//                }
//            }
//
//            log.info("Merchandise creation complete: {} artworks processed, {} merchandise items created, {} errors",
//                    artworkProcessedCount, merchandiseCreatedCount, artworkErrorCount);
//
//            log.info("Test data initialization complete!");
//        };
//    }
}
