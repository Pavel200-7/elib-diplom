package com.example.elib.genre.controller;

import com.example.elib.genre.dto.request.CreateGenreDto;
import com.example.elib.genre.dto.request.UpdateGenreDto;
import com.example.elib.genre.dto.response.GenreDto;
import com.example.elib.genre.service.GenreService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/genres")
@RequiredArgsConstructor
public class GenreController {

    private final GenreService genreService;

    @PostMapping
    public ResponseEntity<GenreDto> createGenre(@RequestBody CreateGenreDto dto) {
        GenreDto created = genreService.createGenre(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<GenreDto> updateGenre(@PathVariable UUID id, @RequestBody UpdateGenreDto dto) {
        GenreDto updated = genreService.updateGenre(id, dto);
        return ResponseEntity.ok(updated);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GenreDto> getGenreById(@PathVariable UUID id) {
        GenreDto genre = genreService.getGenreById(id);
        return ResponseEntity.ok(genre);
    }

    @GetMapping
    public ResponseEntity<List<GenreDto>> getAllGenres() {
        List<GenreDto> genres = genreService.getAllGenres();
        return ResponseEntity.ok(genres);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGenre(@PathVariable UUID id) {
        genreService.deleteGenre(id);
        return ResponseEntity.noContent().build();
    }
}