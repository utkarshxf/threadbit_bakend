package com.backend.threadbit.service;


import com.backend.threadbit.dto.ReviewDto;
import com.backend.threadbit.dto.ReviewResponseDto;
import com.backend.threadbit.dto.UserDto;
import com.backend.threadbit.dto.UserRatingDto;
import com.backend.threadbit.dto.UserResponseDto;

import java.util.List;

public interface UserService {
    UserResponseDto createUser(UserDto userDto);
    UserResponseDto getUserById(String id);
    UserResponseDto getUserByUsername(String username);
    UserResponseDto getUserByPhone(String phone);

    // Review related methods
    List<ReviewResponseDto> getUserReviews(String userId);
    UserRatingDto getUserRating(String userId);
    ReviewResponseDto addReview(String userId, String reviewerId, ReviewDto reviewDto);
}
