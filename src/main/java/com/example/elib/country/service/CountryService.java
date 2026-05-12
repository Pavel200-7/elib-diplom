package com.example.elib.country.service;

import com.example.elib.country.dto.request.CreateCountryDto;
import com.example.elib.country.dto.request.UpdateCountryDto;
import com.example.elib.country.dto.response.CountryDto;

import java.util.List;
import java.util.UUID;

public interface CountryService {
    CountryDto createCountry(CreateCountryDto dto);
    CountryDto updateCountry(UUID id, UpdateCountryDto dto);
    CountryDto getCountryById(UUID id);
    List<CountryDto> getAllCountries();
    void deleteCountry(UUID id);
}