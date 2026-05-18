package com.example.elib.copy.service;

import com.example.elib.copy.dto.request.CreateCopyDto;
import com.example.elib.copy.dto.request.GetCopyCriteriaDto;
import com.example.elib.copy.dto.request.SetRegularHolderDto;
import com.example.elib.copy.dto.request.UpdateCopyDto;
import com.example.elib.copy.dto.response.CopyDto;
import com.example.elib.copy.dto.response.CopyShortDto;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.UUID;

public interface CopyService {
    CopyDto createCopy(CreateCopyDto dto);
    List<CopyDto> createCopies(List<CreateCopyDto> dto);
    CopyDto updateCopy(UUID id, UpdateCopyDto dto);
    void deleteCopy(UUID id);
    List<CopyDto> setRegularHolder(SetRegularHolderDto dto);
    CopyDto setAvailable(UUID id);
    CopyDto setReserved(UUID id);
    CopyDto cancelReserve(UUID id);
    CopyDto setIssued(UUID id);
    CopyDto setInTransit(UUID id);
    CopyDto setShelved(UUID id);
    CopyDto setWrittenOff(UUID id);
    CopyDto getCopy(UUID id);
    CopyDto getRandomAvailableCopyByBookId(UUID bookId);
    Page<CopyShortDto> getCopiesPage(GetCopyCriteriaDto criteria);
}
