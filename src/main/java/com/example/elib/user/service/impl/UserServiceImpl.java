package com.example.elib.user.service.impl;

import com.example.elib.common.exception.DuplicateResourceException;
import com.example.elib.common.exception.ResourceNotFoundException;
import com.example.elib.user.service.UserService;
import com.example.elib.user.dto.request.CreateUserDto;
import com.example.elib.user.dto.request.UpdateUserDto;
import com.example.elib.user.dto.response.UserDto;
import com.example.elib.user.entity.User;
import com.example.elib.user.mapper.UserMapper;
import com.example.elib.user.repository.UserRepository;
import com.example.elib.user.vo.Contact;
import com.example.elib.user.vo.PersonalData;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository repository;
    public final UserMapper mapper;


    @Override
    @Transactional
    public UserDto createUser(CreateUserDto dto) {
        if (repository.existsByContactEmail(dto.getEmail())) {
            throw new DuplicateResourceException("Пользователь с таким email уже существует.");

        }
        if (repository.existsByContactPhone(dto.getPhone())) {
            throw new DuplicateResourceException("Пользователь с таким телефоном уже существует.");
        }

        Contact contact = Contact.of(dto.getEmail(), dto.getPhone());
        User user = User.register(contact, dto.getId());
        repository.save(user);
        return mapper.toDto(user);
    }

    @Override
    @Transactional(readOnly = true)
    public UserDto getUser(UUID id) {
        User user = repository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Пользователь с таким id не найден."));
        return mapper.toDto(user);
    }

    @Override
    @Transactional
    public UserDto updateUser(UUID id, UpdateUserDto dto) {
        User user = repository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Пользователь с таким id не найден."));
        PersonalData personalData = PersonalData.of(
                dto.getFirstName(),
                dto.getLastName(),
                dto.getPatronymic(),
                dto.getBirthDate());
        user.setPersonalData(personalData);
        repository.save(user);
        return mapper.toDto(user);
    }

    @Override
    @Transactional
    public UserDto activateUser(UUID id) {
        User user = repository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Пользователь с таким id не найден."));
        user.activate();
        repository.save(user);
        return mapper.toDto(user);
    }

    @Override
    public List<UserDto> searchUsers(String query) {
        List<User> users = repository.searchByEmailOrPhone(query);
        return users.stream()
                .map(mapper::toDto)
                .toList();
    }
}
