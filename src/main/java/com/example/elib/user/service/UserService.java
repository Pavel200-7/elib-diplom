package com.example.elib.user.service;

import com.example.elib.user.dto.request.CreateUserDto;
import com.example.elib.user.dto.request.GetUserCriteriaDto;
import com.example.elib.user.dto.request.UpdateUserDto;
import com.example.elib.user.dto.response.UserDto;
import org.springframework.data.domain.Page;

import java.util.UUID;
import java.util.List;

public interface UserService {
    UserDto createUser(CreateUserDto dto);
    UserDto getUser(UUID id);
    UserDto updateUser(UUID id, UpdateUserDto dto);
    UserDto activateUser(UUID id);
    Page<UserDto> getUsersPage(GetUserCriteriaDto criteria);
}
