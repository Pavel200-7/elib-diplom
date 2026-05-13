package com.example.elib.copy.service.impl;

import com.example.elib.book.entity.Book;
import com.example.elib.book.repository.BookRepository;
import com.example.elib.common.exeption.DuplicateResourceException;
import com.example.elib.common.exeption.ResourceNotFoundException;
import com.example.elib.copy.dto.request.CreateCopyDto;
import com.example.elib.copy.dto.request.SetRegularHolderDto;
import com.example.elib.copy.dto.request.UpdateCopyDto;
import com.example.elib.copy.dto.response.CopyDto;
import com.example.elib.copy.entity.Copy;
import com.example.elib.copy.enums.CopyEvent;
import com.example.elib.copy.enums.CopyStatus;
import com.example.elib.copy.mapper.CopyMapper;
import com.example.elib.copy.repository.CopyRepository;
import com.example.elib.copy.service.CopyService;
import com.example.elib.copy.sm.CopyStateMachineConfig;
import com.example.elib.holder.entity.Holder;
import com.example.elib.holder.repository.HolderRepository;
import com.example.elib.room.entity.Room;
import com.github.oxo42.stateless4j.StateMachine;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CopyServiceImpl implements CopyService {

    private final CopyRepository copyRepository;
    private final BookRepository bookRepository;
    private final HolderRepository holderRepository;
    private final CopyMapper copyMapper;
    private final CopyStateMachineConfig stateMachineConfig;

    @Override
    @Transactional
    public CopyDto createCopy(CreateCopyDto dto) {
        if (copyRepository.existsByInventoryNumber(dto.getInventoryNumber())) {
            throw new DuplicateResourceException("Экземпляр с инвентарным номером '" + dto.getInventoryNumber() + "' уже существует.");
        }
        if (copyRepository.existsByIsbn(dto.getIsbn())) {
            throw new DuplicateResourceException("Экземпляр с isbn '" + dto.getIsbn() + "' уже существует.");
        }
        Book book = bookRepository.findById(dto.getBookId())
                .orElseThrow(() -> new ResourceNotFoundException("Книга с id " + dto.getBookId() + " не найдена."));

        Copy copy = Copy.addCopy(dto.getInventoryNumber(), dto.getIsbn(), book);
        copyRepository.save(copy);
        return copyMapper.toDto(copy);
    }

    @Override
    @Transactional
    public List<CopyDto> createCopies(List<CreateCopyDto> dto) {
        return dto.stream()
                .map(this::createCopy)
                .collect(Collectors.toUnmodifiableList());
    }

    @Override
    @Transactional
    public CopyDto updateCopy(UUID id, UpdateCopyDto dto) {
        Copy copy = copyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Экземпляр с id " + id + " не найден."));
        if (!copy.isInStatus(CopyStatus.ADDED)) {
            throw new IllegalStateException("Изменить данные экземпляра можно только сразу после добавления, но до отметки его действующим.");
        }
        if (!copy.getIsbn().equals(dto.getIsbn()) && copyRepository.existsByIsbn(dto.getIsbn())) {
            throw new DuplicateResourceException("Экземпляр с isbn '" + dto.getIsbn() + "' уже существует.");
        }
        if (!copy.getInventoryNumber().equals(dto.getInventoryNumber()) && copyRepository.existsByInventoryNumber(dto.getInventoryNumber())) {
            throw new DuplicateResourceException("Экземпляр с инвентарным номером '" + dto.getInventoryNumber() + "' уже существует.");
        }

        copy.updateCopy(dto.getInventoryNumber(), dto.getIsbn());
        copyRepository.save(copy);
        return copyMapper.toDto(copy);
    }

    @Override
    @Transactional
    public void deleteCopy(UUID id) {
        Copy copy = copyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Экземпляр с id " + id + " не найден."));
        if (!copy.isInStatus(CopyStatus.ADDED)) {
            throw new IllegalStateException("Удалить экземпляр можно только сразу после добавления, но до отметки его действующим.");
        }
        copyRepository.delete(copy);
    }

    @Override
    @Transactional
    public List<CopyDto> setRegularHolder(SetRegularHolderDto dto) {
        Holder holder = holderRepository.findById(dto.getHolderId())
                .orElseThrow(() -> new ResourceNotFoundException("Место хранения с id " + dto.getHolderId() + " не найдено."));
        return dto.getCopiesId().stream()
                .map(id -> {
                    Copy copy = copyRepository.findById(id)
                            .orElseThrow(() -> new ResourceNotFoundException("Экземпляр с id " + id + " не найден."));
                    copy.setHolder(holder);
                    copyRepository.save(copy);
                    return copyMapper.toDto(copy);
                }).collect(Collectors.toUnmodifiableList());
    }

    @Override
    @Transactional
    public CopyDto setAvailable(UUID id) {
        return executeTransition(id, CopyEvent.MAKE_AVAILABLE, "AVAILABLE");
    }

    @Override
    @Transactional
    public CopyDto setReserved(UUID id) {
        return executeTransition(id, CopyEvent.RESERVE, "RESERVED");
    }

    @Override
    @Transactional
    public CopyDto cancelReserve(UUID id) {
        return executeTransition(id, CopyEvent.CANCEL_RESERVE, "AVAILABLE");
    }

    @Override
    @Transactional
    public CopyDto setIssued(UUID id) {
        return executeTransition(id, CopyEvent.ISSUE, "ISSUED");
    }

    @Override
    @Transactional
    public CopyDto setInTransit(UUID id) {
        return executeTransition(id, CopyEvent.RETURN, "IN_TRANSIT");
    }

    @Override
    @Transactional
    public CopyDto setShelved(UUID id) {
        return executeTransition(id, CopyEvent.SHELVE, "AVAILABLE");
    }

    @Override
    @Transactional
    public CopyDto setWrittenOff(UUID id) {
        return executeTransition(id, CopyEvent.WRITE_OFF, "WRITTEN_OFF");
    }

    @Override
    @Transactional(readOnly = true)
    public CopyDto getCopy(UUID id) {
        Copy copy = copyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Экземпляр с id " + id + " не найден."));
        return copyMapper.toDto(copy);
    }

    private CopyDto executeTransition(UUID id, CopyEvent event, String targetStatusName) {
        Copy copy = copyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Экземпляр с id " + id + " не найден."));

        StateMachine<CopyStatus, CopyEvent> stateMachine = stateMachineConfig.createMachine(copy.getStatus());

        if (!stateMachine.canFire(event)) {
            throw new IllegalStateException(String.format("Невозможно выполнить переход '%s' из состояния '%s'.",
                    event,
                    copy.getStatus())
            );
        }

        stateMachine.fire(event);
        CopyStatus newStatus = stateMachine.getState();
        copy.setStatus(newStatus);
        copyRepository.save(copy);

        log.info("Экземпляр {} переведён из {} в {} через событие {}",
                id, copy.getStatus(), newStatus, event);

        return copyMapper.toDto(copy);
    }

}
