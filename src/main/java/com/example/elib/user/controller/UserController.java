package com.example.elib.user.controller;

import com.example.elib.user.service.UserService;
import com.example.elib.user.dto.request.UpdateUserDto;
import com.example.elib.user.dto.response.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUser(@PathVariable UUID id) {
        UserDto user = userService.getUser(id);
        return ResponseEntity.ok(user);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDto> updateUser(
            @PathVariable UUID id,
            @RequestBody UpdateUserDto updateUserDto) {
        UserDto updatedUser = userService.updateUser(id, updateUserDto);
        return ResponseEntity.ok(updatedUser);
    }

    @PostMapping("/{id}/activate")
    public ResponseEntity<UserDto> activateUser(@PathVariable UUID id) {
        UserDto activatedUser = userService.activateUser(id);
        return ResponseEntity.ok(activatedUser);
    }
}