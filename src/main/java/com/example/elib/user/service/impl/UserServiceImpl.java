package com.example.elib.user.service.impl;

import com.example.elib.common.dto.pagination.PageData;
import com.example.elib.common.exception.DuplicateResourceException;
import com.example.elib.common.exception.ResourceNotFoundException;
import com.example.elib.common.service.ReaderNumberGenerator;
import com.example.elib.copy.dto.request.GetCopyCriteriaDto;
import com.example.elib.copy.dto.request.pagination.CopySearchCriteria;
import com.example.elib.copy.dto.request.pagination.CopySortCriteria;
import com.example.elib.copy.dto.response.CopyShortDto;
import com.example.elib.copy.entity.Copy;
import com.example.elib.copy.service.impl.utils.CopyPageRequestUtils;
import com.example.elib.user.dto.request.GetUserCriteriaDto;
import com.example.elib.user.dto.request.pagination.UserSearchCriteria;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository repository;
    private final ReaderNumberGenerator readerNumberGenerator;
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
        String readerBookNumber = readerNumberGenerator.generate();

        User user = User.register(contact, dto.getId(), readerBookNumber);
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
    @Transactional(readOnly = true)
    public Page<UserDto> getUsersPage(GetUserCriteriaDto criteria) {
        String query = criteria.getSearchCriteria().getQuery();
        long total = repository.countByQuery(query);

        PageData pageData = criteria.getPageData();
        Pageable pageable = PageRequest.of(pageData.getPage(), pageData.getSize());
        List<User> users = repository.searchByQuery(
                query,
                pageable.getPageSize(),
                (int) pageable.getOffset()
        );

        List<UserDto> dtos = users.stream()
                .map(mapper::toDto)
                .toList();
        return new PageImpl<>(dtos, pageable, total);
    }


}
