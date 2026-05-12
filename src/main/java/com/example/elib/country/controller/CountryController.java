package com.example.elib.country.controller;

import com.example.elib.country.dto.request.CreateCountryDto;
import com.example.elib.country.dto.request.UpdateCountryDto;
import com.example.elib.country.dto.response.CountryDto;
import com.example.elib.country.service.CountryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/countries")
@RequiredArgsConstructor
public class CountryController {

    private final CountryService countryService;

    @PostMapping
    public ResponseEntity<CountryDto> createCountry(@RequestBody CreateCountryDto dto) {
        CountryDto created = countryService.createCountry(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CountryDto> updateCountry(@PathVariable UUID id, @RequestBody UpdateCountryDto dto) {
        CountryDto updated = countryService.updateCountry(id, dto);
        return ResponseEntity.ok(updated);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CountryDto> getCountryById(@PathVariable UUID id) {
        CountryDto country = countryService.getCountryById(id);
        return ResponseEntity.ok(country);
    }

    @GetMapping
    public ResponseEntity<List<CountryDto>> getAllCountries() {
        List<CountryDto> countries = countryService.getAllCountries();
        return ResponseEntity.ok(countries);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCountry(@PathVariable UUID id) {
        countryService.deleteCountry(id);
        return ResponseEntity.noContent().build();
    }
}