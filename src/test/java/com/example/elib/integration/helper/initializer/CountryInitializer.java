package com.example.elib.integration.helper.initializer;

import com.example.elib.country.entity.Country;
import com.example.elib.country.repository.CountryRepository;
import com.example.elib.genre.entity.Genre;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CountryInitializer {

    @Autowired
    private CountryRepository countryRepository;

    public Country createCountry(String name) {
        Country country = Country.create(name);
        return countryRepository.save(country);
    }
}
