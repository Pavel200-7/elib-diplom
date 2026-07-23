package com.example.elib.country.service.impl;

import com.example.elib.author.repository.AuthorRepository;
import com.example.elib.common.exception.DuplicateResourceException;
import com.example.elib.common.exception.ReferentialIntegrityException;
import com.example.elib.common.exception.ResourceNotFoundException;
import com.example.elib.country.dto.request.CreateCountryDto;
import com.example.elib.country.dto.request.UpdateCountryDto;
import com.example.elib.country.dto.response.CountryDto;
import com.example.elib.country.entity.Country;
import com.example.elib.country.mapper.CountryMapper;
import com.example.elib.country.repository.CountryRepository;
import com.example.elib.country.service.CountryService;
import com.example.elib.publishing.repository.PublishingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CountryServiceImpl implements CountryService {

    private final CountryRepository countryRepository;
    private final CountryMapper countryMapper;
    private final AuthorRepository authorRepository;
    private final PublishingRepository publishingRepository;


    @Override
    @Transactional
    public CountryDto createCountry(CreateCountryDto dto) {
        if (countryRepository.existsByName(dto.getName())) {
            throw new DuplicateResourceException("Страна с названием '" + dto.getName() + "' уже существует.");
        }

        Country country = Country.create(dto.getName());
        country = countryRepository.save(country);
        return countryMapper.toDto(country);
    }

    @Override
    @Transactional
    public CountryDto updateCountry(UUID id, UpdateCountryDto dto) {
        Country country = countryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Страна с id " + id + " не найдена."));

        if (!country.getName().equals(dto.getName()) && countryRepository.existsByName(dto.getName())) {
            throw new DuplicateResourceException("Страна с названием '" + dto.getName() + "' уже существует.");
        }

        country.update(dto.getName());
        country = countryRepository.save(country);
        return countryMapper.toDto(country);
    }

    @Override
    @Transactional(readOnly = true)
    public CountryDto getCountryById(UUID id) {
        Country country = countryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Страна с id " + id + " не найдена."));
        return countryMapper.toDto(country);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CountryDto> getAllCountries() {
        return countryRepository.findAllByOrderByNameAsc().stream()
                .map(countryMapper::toDto)
                .toList();
    }

    @Override
    @Transactional
    public void deleteCountry(UUID id) {
        Country country = countryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Страна с id " + id + " не найдена."));
         if (authorRepository.existsByCountryId(id) || publishingRepository.existsByCountryId(id)) {
             throw new ReferentialIntegrityException("Невозможно удалить страну, так как есть привязанные авторы или издательства.");
         }

        countryRepository.delete(country);
    }
}