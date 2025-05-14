package com.backend.threadbit.service;


import com.backend.threadbit.dto.UserDto;
import com.backend.threadbit.dto.UserResponseDto;

public interface UserService {
    UserResponseDto createUser(UserDto userDto);
    UserResponseDto getUserById(String id);
    UserResponseDto getUserByUsername(String username);
}
