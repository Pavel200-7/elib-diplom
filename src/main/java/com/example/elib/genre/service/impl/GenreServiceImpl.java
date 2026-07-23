package com.example.elib.genre.service.impl;

import com.example.elib.book.repository.BookRepository;
import com.example.elib.common.exception.DuplicateResourceException;
import com.example.elib.common.exception.ReferentialIntegrityException;
import com.example.elib.common.exception.ResourceNotFoundException;
import com.example.elib.genre.dto.request.CreateGenreDto;
import com.example.elib.genre.dto.request.UpdateGenreDto;
import com.example.elib.genre.dto.response.GenreDto;
import com.example.elib.genre.entity.Genre;
import com.example.elib.genre.mapper.GenreMapper;
import com.example.elib.genre.repository.GenreRepository;
import com.example.elib.genre.service.GenreService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GenreServiceImpl implements GenreService {

    private final GenreRepository genreRepository;
    private final BookRepository bookRepository;
    private final GenreMapper genreMapper;

    @Override
    @Transactional
    public GenreDto createGenre(CreateGenreDto dto) {
        if (genreRepository.existsByName(dto.getName())) {
            throw new DuplicateResourceException("Жанр с названием '" + dto.getName() + "' уже существует.");
        }

        Genre genre = Genre.create(dto.getName());
        genre = genreRepository.save(genre);
        return genreMapper.toDto(genre);
    }

    @Override
    @Transactional
    public GenreDto updateGenre(UUID id, UpdateGenreDto dto) {
        Genre genre = genreRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Жанр с id " + id + " не найден."));
        if (!genre.getName().equals(dto.getName()) && genreRepository.existsByName(dto.getName())) {
            throw new DuplicateResourceException("Жанр с названием '" + dto.getName() + "' уже существует.");
        }

        genre.update(dto.getName());
        genre = genreRepository.save(genre);
        return genreMapper.toDto(genre);
    }

    @Override
    @Transactional(readOnly = true)
    public GenreDto getGenreById(UUID id) {
        Genre genre = genreRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Жанр с id " + id + " не найден."));
        return genreMapper.toDto(genre);
    }

    @Override
    @Transactional(readOnly = true)
    public List<GenreDto> getAllGenres() {
        return genreRepository.findAllByOrderByNameAsc().stream()
                .map(genreMapper::toDto)
                .toList();
    }

    @Override
    @Transactional
    public void deleteGenre(UUID id) {
        Genre genre = genreRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Жанр с id " + id + " не найден."));
        if (bookRepository.existsByGenreId(id)) {
            throw new ReferentialIntegrityException("Невозможно удалить жанр, так как есть привязанные книги.");
        }

        genreRepository.delete(genre);
    }
}