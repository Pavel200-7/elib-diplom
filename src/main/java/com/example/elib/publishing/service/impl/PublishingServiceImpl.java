package com.example.elib.publishing.service.impl;

import com.example.elib.book.repository.BookRepository;
import com.example.elib.common.exeption.DuplicateResourceException;
import com.example.elib.common.exeption.ReferentialIntegrityException;
import com.example.elib.common.exeption.ResourceNotFoundException;
import com.example.elib.country.entity.Country;
import com.example.elib.country.repository.CountryRepository;
import com.example.elib.publishing.dto.request.CreatePublishingDto;
import com.example.elib.publishing.dto.request.UpdatePublishingDto;
import com.example.elib.publishing.dto.response.PublishingDto;
import com.example.elib.publishing.entity.Publishing;
import com.example.elib.publishing.mapper.PublishingMapper;
import com.example.elib.publishing.repository.PublishingRepository;
import com.example.elib.publishing.service.PublishingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PublishingServiceImpl implements PublishingService {

    private final PublishingRepository publishingRepository;
    private final CountryRepository countryRepository;
    private final BookRepository bookRepository;
    private final PublishingMapper publishingMapper;

    @Override
    @Transactional
    public PublishingDto createPublishing(CreatePublishingDto dto) {
        if (publishingRepository.existsByName(dto.getName())) {
            throw new DuplicateResourceException("Издательство с названием '" + dto.getName() + "' уже существует.");
        }
        Country country = countryRepository.findById(dto.getCountryId())
                .orElseThrow(() -> new ResourceNotFoundException("Страна с id " + dto.getCountryId() + " не найдена."));

        Publishing publishing = Publishing.create(dto.getName(), dto.getDescription(), country);
        publishing = publishingRepository.save(publishing);
        return publishingMapper.toDto(publishing);
    }

    @Override
    public PublishingDto updatePublishing(UUID id, UpdatePublishingDto dto) {
        Publishing publishing = publishingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Издательство с id " + id + " не найдено."));
        if (!publishing.getName().equals(dto.getName()) && publishingRepository.existsByName(dto.getName())) {
            throw new DuplicateResourceException("Издательство с названием '" + dto.getName() + "' уже существует.");
        }
        Country country = null;
        if (publishing.getCountry() == null || !publishing.getCountry().getId().equals(dto.getCountryId())) {
            country = countryRepository.findById(dto.getCountryId())
                    .orElseThrow(() -> new ResourceNotFoundException("Страна с id " + dto.getCountryId() + " не найдена."));
        } else {
            country = publishing.getCountry();
        }

        publishing.update(dto.getName(), dto.getDescription(), country);
        publishing = publishingRepository.save(publishing);
        return publishingMapper.toDto(publishing);
    }

    @Override
    @Transactional(readOnly = true)
    public PublishingDto getPublishingById(UUID id) {
        Publishing publishing = publishingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Издательство с id " + id + " не найдено."));
        return publishingMapper.toDto(publishing);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PublishingDto> getAllPublishings() {
        return publishingRepository.findAll().stream()
                .map(publishingMapper::toDto)
                .toList();
    }

    @Override
    @Transactional
    public void deletePublishing(UUID id) {
        Publishing publishing = publishingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Издательство с id " + id + " не найдено."));
         if (bookRepository.existsByPublishingId(id)) {
             throw new ReferentialIntegrityException("Невозможно удалить издательство, так как есть привязанные книги.");
         }

        publishingRepository.delete(publishing);
    }
}