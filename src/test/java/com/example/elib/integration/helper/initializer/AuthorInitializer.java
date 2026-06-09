package com.example.elib.integration.helper.initializer;

import com.example.elib.author.entity.Author;
import com.example.elib.author.repository.AuthorRepository;
import com.example.elib.country.entity.Country;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AuthorInitializer {

    @Autowired
    private AuthorRepository authorRepository;

    public Author createAuthor(String name, Country country) {
        Author author = Author.create(name, country);
        return authorRepository.save(author);
    }
}