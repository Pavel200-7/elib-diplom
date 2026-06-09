package com.example.elib.integration.helper.initializer;

import com.example.elib.country.entity.Country;
import com.example.elib.publishing.entity.Publishing;
import com.example.elib.publishing.repository.PublishingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PublishingInitializer {

    @Autowired
    private PublishingRepository publishingRepository;

    public Publishing createPublishing(String name, Country country) {
        Publishing publishing = Publishing.create(name, "null", country);
        return publishingRepository.save(publishing);
    }
}