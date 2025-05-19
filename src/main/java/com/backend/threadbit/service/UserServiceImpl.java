package com.backend.threadbit.service;


import com.backend.threadbit.dto.ReviewDto;
import com.backend.threadbit.dto.ReviewResponseDto;
import com.backend.threadbit.dto.UserDto;
import com.backend.threadbit.dto.UserRatingDto;
import com.backend.threadbit.dto.UserResponseDto;
import com.backend.threadbit.model.Review;
import com.backend.threadbit.model.User;
import com.backend.threadbit.model.UserRating;
import com.backend.threadbit.repository.ReviewRepository;
import com.backend.threadbit.repository.UserRatingRepository;
import com.backend.threadbit.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    @Autowired
    private final UserRepository userRepository;

    @Autowired
    private final ReviewRepository reviewRepository;

    @Autowired
    private final UserRatingRepository userRatingRepository;
    @Override
    public UserResponseDto createUser(UserDto userDto) {
        // Convert UserDto to User entity
        User user = User.builder()
                .username(userDto.getUsername())
                .email(userDto.getEmail())
                .phoneNumber(userDto.getPhoneNumber())
                .socialMedia(userDto.getSocialMedia())
                .walletBalance(userDto.getWalletBalance())
                .avatarUrl(userDto.getAvatarUrl())
                .password(userDto.getPassword())
                .build();

        // Save the user
        User savedUser = userRepository.save(user);

        // Convert and return as UserResponseDto
        return convertToResponseDto(savedUser);
    }

    @Override
    public UserResponseDto getUserById(String id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + id));

        return convertToResponseDto(user);
    }

    @Override
    public UserResponseDto getUserByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("User not found with username: " + username));

        return convertToResponseDto(user);
    }

    @Override
    public UserResponseDto getUserByPhone(String phone) {
        User user = userRepository.findByPhoneNumber(phone)
                .orElseThrow(() -> new EntityNotFoundException("User phone not found with phone: " + phone));

        return convertToResponseDto(user);
    }

    private UserResponseDto convertToResponseDto(User user) {
        return UserResponseDto.builder()
                .id(user.getId())
                .phoneNumber(user.getPhoneNumber())
                .socialMedia(user.getSocialMedia())
                .walletBalance(user.getWalletBalance())
                .name(user.getName())
                .avatarUrl(user.getAvatarUrl())
                .username(user.getUsername())
                .email(user.getEmail())
                .build();
    }

    @Override
    public List<ReviewResponseDto> getUserReviews(String userId) {
        // Verify user exists
        userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + userId));

        // Get all reviews for the user
        List<Review> reviews = reviewRepository.findByUserId(userId);

        // Convert to DTOs
        return reviews.stream()
                .map(this::convertToReviewResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserRatingDto getUserRating(String userId) {
        // Verify user exists
        userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + userId));

        // Get user rating or create a new one if it doesn't exist
        UserRating userRating = userRatingRepository.findByUserId(userId)
                .orElse(UserRating.builder()
                        .userId(userId)
                        .averageRating(0.0)
                        .reviewCount(0)
                        .build());

        // Convert to DTO
        return UserRatingDto.builder()
                .userId(userRating.getUserId())
                .averageRating(userRating.getAverageRating())
                .reviewCount(userRating.getReviewCount())
                .build();
    }

    @Override
    public ReviewResponseDto addReview(String userId, String reviewerId, ReviewDto reviewDto) {
        // Verify both users exist
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + userId));

        User reviewer = userRepository.findById(reviewerId)
                .orElseThrow(() -> new EntityNotFoundException("Reviewer not found with id: " + reviewerId));

        // Create and save the review
        Review review = Review.builder()
                .userId(userId)
                .reviewerId(reviewerId)
                .rating(reviewDto.getRating())
                .comment(reviewDto.getComment())
                .build();

        Review savedReview = reviewRepository.save(review);

        // Update user rating
        updateUserRating(userId);

        // Return the saved review
        return convertToReviewResponseDto(savedReview);
    }

    private void updateUserRating(String userId) {
        // Get all reviews for the user
        List<Review> reviews = reviewRepository.findByUserId(userId);

        if (reviews.isEmpty()) {
            return;
        }

        // Calculate average rating
        double averageRating = reviews.stream()
                .mapToInt(Review::getRating)
                .average()
                .orElse(0.0);

        // Get or create user rating
        UserRating userRating = userRatingRepository.findByUserId(userId)
                .orElse(UserRating.builder()
                        .userId(userId)
                        .build());

        // Update and save
        userRating.setAverageRating(averageRating);
        userRating.setReviewCount(reviews.size());
        userRatingRepository.save(userRating);
    }

    private ReviewResponseDto convertToReviewResponseDto(Review review) {
        // Get reviewer's username
        String reviewerUsername = "";
        try {
            User reviewer = userRepository.findById(review.getReviewerId()).orElse(null);
            if (reviewer != null) {
                reviewerUsername = reviewer.getUsername();
            }
        } catch (Exception e) {
            log.error("Error getting reviewer", e);
        }

        return ReviewResponseDto.builder()
                .id(review.getId())
                .userId(review.getUserId())
                .reviewerId(review.getReviewerId())
                .reviewerUsername(reviewerUsername)
                .rating(review.getRating())
                .comment(review.getComment())
                .createdAt(review.getCreatedAt())
                .build();
    }
}
