package com.example.elib.integration.helper.initializer;

import com.example.elib.genre.entity.Genre;
import com.example.elib.genre.repository.GenreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class GenreInitializer {

    @Autowired
    private GenreRepository genreRepository;

    public Genre createGenre(String name) {
        Genre genre = Genre.create(name);
        return genreRepository.save(genre);
    }
}