package com.backend.threadbit.service;


import com.backend.threadbit.dto.UserDto;
import com.backend.threadbit.dto.UserResponseDto;
import com.backend.threadbit.model.User;
import com.backend.threadbit.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    @Autowired
    private final UserRepository userRepository;
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
                .orElseThrow(() -> new EntityNotFoundException("User phone found with phone: " + phone));

        return convertToResponseDto(user);
    }

    private UserResponseDto convertToResponseDto(User user) {
        return UserResponseDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .build();
    }
}