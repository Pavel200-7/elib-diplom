package com.example.elib.publishing.controller;

import com.example.elib.publishing.dto.request.CreatePublishingDto;
import com.example.elib.publishing.dto.request.UpdatePublishingDto;
import com.example.elib.publishing.dto.response.PublishingDto;
import com.example.elib.publishing.service.PublishingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/publishings")
@RequiredArgsConstructor
public class PublishingController {

    private final PublishingService publishingService;

    @PostMapping
    public ResponseEntity<PublishingDto> createPublishing(@RequestBody CreatePublishingDto dto) {
        PublishingDto created = publishingService.createPublishing(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PublishingDto> updatePublishing(@PathVariable UUID id, @RequestBody UpdatePublishingDto dto) {
        PublishingDto updated = publishingService.updatePublishing(id, dto);
        return ResponseEntity.ok(updated);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PublishingDto> getPublishingById(@PathVariable UUID id) {
        PublishingDto publishing = publishingService.getPublishingById(id);
        return ResponseEntity.ok(publishing);
    }

    @GetMapping
    public ResponseEntity<List<PublishingDto>> getAllPublishings() {
        List<PublishingDto> publishings = publishingService.getAllPublishings();
        return ResponseEntity.ok(publishings);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePublishing(@PathVariable UUID id) {
        publishingService.deletePublishing(id);
        return ResponseEntity.noContent().build();
    }
}