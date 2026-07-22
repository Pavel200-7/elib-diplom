package com.example.elib.user.controller;

import com.example.elib.user.dto.request.GetUserCriteriaDto;
import com.example.elib.user.service.UserService;
import com.example.elib.user.dto.request.UpdateUserDto;
import com.example.elib.user.dto.response.UserDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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

    @GetMapping("/search")
    public ResponseEntity<List<UserDto>> searchUsers(@RequestParam String query) {
        List<UserDto> users = userService.searchUsers(query);
        return ResponseEntity.ok(users);
    }

    @PostMapping("/page")
    public ResponseEntity<Page<UserDto>> getUserPage(@Valid @RequestBody GetUserCriteriaDto criteria) {
        Page<UserDto> users = userService.getUsersPage(criteria);
        return ResponseEntity.ok(users);
    }
}