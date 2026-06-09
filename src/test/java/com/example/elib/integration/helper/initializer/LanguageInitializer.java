package com.example.elib.integration.helper.initializer;

import com.example.elib.language.entity.Language;
import com.example.elib.language.repository.LanguageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class LanguageInitializer {

    @Autowired
    private LanguageRepository languageRepository;

    public Language createLanguage(String name) {
        Language language = Language.create(name);
        return languageRepository.save(language);
    }
}