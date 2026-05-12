package com.example.elib.user.service;

import com.example.elib.user.dto.request.CreateUserDto;
import com.example.elib.user.dto.request.UpdateUserDto;
import com.example.elib.user.dto.response.UserDto;

import java.util.UUID;

public interface UserService {
    UserDto createUser(CreateUserDto dto);
    UserDto getUser(UUID id);
    UserDto updateUser(UUID id, UpdateUserDto dto);
    UserDto activateUser(UUID id);
}
