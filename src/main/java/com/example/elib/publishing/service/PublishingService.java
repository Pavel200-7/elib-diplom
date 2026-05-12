package com.example.elib.publishing.service;

import com.example.elib.publishing.dto.request.CreatePublishingDto;
import com.example.elib.publishing.dto.request.UpdatePublishingDto;
import com.example.elib.publishing.dto.response.PublishingDto;

import java.util.List;
import java.util.UUID;

public interface PublishingService {
    PublishingDto createPublishing(CreatePublishingDto dto);
    PublishingDto updatePublishing(UUID id, UpdatePublishingDto dto);
    PublishingDto getPublishingById(UUID id);
    List<PublishingDto> getAllPublishings();
    void deletePublishing(UUID id);
}