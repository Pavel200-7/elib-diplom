package com.example.elib.author.service;

import com.example.elib.author.dto.request.CreateAuthorDto;
import com.example.elib.author.dto.request.UpdateAuthorDto;
import com.example.elib.author.dto.response.AuthorDto;

import java.util.List;
import java.util.UUID;

public interface AuthorService {
    AuthorDto createAuthor(CreateAuthorDto dto);
    AuthorDto updateAuthor(UUID id, UpdateAuthorDto dto);
    AuthorDto getAuthorById(UUID id);
    List<AuthorDto> getAllAuthors();
    void deleteAuthor(UUID id);
}