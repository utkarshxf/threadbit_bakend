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
                .password(userDto.getPassword()) // Consider encrypting password before saving
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
    
    private UserResponseDto convertToResponseDto(User user) {
        return UserResponseDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                // Don't include password in response
                .build();
    }
}