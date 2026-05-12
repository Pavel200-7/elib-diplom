package com.example.elib.author.service.impl;

import com.example.elib.author.dto.request.CreateAuthorDto;
import com.example.elib.author.dto.request.UpdateAuthorDto;
import com.example.elib.author.dto.response.AuthorDto;
import com.example.elib.author.entity.Author;
import com.example.elib.author.mapper.AuthorMapper;
import com.example.elib.author.repository.AuthorRepository;
import com.example.elib.author.service.AuthorService;
import com.example.elib.book.repository.BookRepository;
import com.example.elib.common.exeption.DuplicateResourceException;
import com.example.elib.common.exeption.ReferentialIntegrityException;
import com.example.elib.common.exeption.ResourceNotFoundException;
import com.example.elib.country.entity.Country;
import com.example.elib.country.repository.CountryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthorServiceImpl implements AuthorService {

    private final AuthorRepository authorRepository;
    private final CountryRepository countryRepository;
    private final BookRepository bookRepository;
    private final AuthorMapper authorMapper;

    @Override
    @Transactional
    public AuthorDto createAuthor(CreateAuthorDto dto) {
        if (authorRepository.existsByName(dto.getName())) {
            throw new DuplicateResourceException("Автор с именем '" + dto.getName() + "' уже существует.");
        }
        Country country = countryRepository.findById(dto.getCountryId())
                .orElseThrow(() -> new ResourceNotFoundException("Страна с id " + dto.getCountryId() + " не найдена."));

        Author author = Author.create(dto.getName(), country);
        author = authorRepository.save(author);
        return authorMapper.toDto(author);
    }

    @Override
    @Transactional
    public AuthorDto updateAuthor(UUID id, UpdateAuthorDto dto) {
        Author author = authorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Автор с id " + id + " не найден."));
        if (!author.getName().equals(dto.getName()) && authorRepository.existsByName(dto.getName())) {
            throw new DuplicateResourceException("Автор с именем '" + dto.getName() + "' уже существует.");
        }
        Country country = null;
        if (author.getCountry() == null || !author.getCountry().getId().equals(dto.getCountryId())) {
            country = countryRepository.findById(dto.getCountryId())
                    .orElseThrow(() -> new ResourceNotFoundException("Страна с id " + dto.getCountryId() + " не найдена."));
        } else {
            country = author.getCountry();
        }

        author.update(dto.getName(), country);
        author = authorRepository.save(author);
        return authorMapper.toDto(author);
    }

    @Override
    @Transactional(readOnly = true)
    public AuthorDto getAuthorById(UUID id) {
        Author author = authorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Автор с id " + id + " не найден."));
        return authorMapper.toDto(author);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AuthorDto> getAllAuthors() {
        return authorRepository.findAll().stream()
                .map(authorMapper::toDto)
                .toList();
    }

    @Override
    @Transactional
    public void deleteAuthor(UUID id) {
        Author author = authorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Автор с id " + id + " не найден."));

        // TODO: проверить, есть ли ссылки на этого автора из Book
         if (bookRepository.existsByAuthorId(id)) {
             throw new ReferentialIntegrityException("Невозможно удалить автора, так как есть привязанные книги.");
         }

        authorRepository.delete(author);
    }
}