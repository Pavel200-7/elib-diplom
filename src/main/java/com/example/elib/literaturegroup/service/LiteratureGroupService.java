package com.example.elib.literaturegroup.service;

import com.example.elib.literaturegroup.dto.request.CreateLiteratureGroupDto;
import com.example.elib.literaturegroup.dto.request.UpdateLiteratureGroupDto;
import com.example.elib.literaturegroup.dto.response.LiteratureGroupDto;

import java.util.List;
import java.util.UUID;

public interface LiteratureGroupService {
    LiteratureGroupDto createLiteratureGroup(CreateLiteratureGroupDto dto);
    LiteratureGroupDto updateLiteratureGroup(UUID id, UpdateLiteratureGroupDto dto);
    LiteratureGroupDto getLiteratureGroupById(UUID id);
    List<LiteratureGroupDto> getAllLiteratureGroups();
    void deleteLiteratureGroup(UUID id);
}