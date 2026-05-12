package com.example.elib.genre.service;

import com.example.elib.genre.dto.request.CreateGenreDto;
import com.example.elib.genre.dto.request.UpdateGenreDto;
import com.example.elib.genre.dto.response.GenreDto;

import java.util.List;
import java.util.UUID;

public interface GenreService {
    GenreDto createGenre(CreateGenreDto dto);
    GenreDto updateGenre(UUID id, UpdateGenreDto dto);
    GenreDto getGenreById(UUID id);
    List<GenreDto> getAllGenres();
    void deleteGenre(UUID id);
}