package com.example.elib.copy.service.impl;

import com.example.elib.book.entity.Book;
import com.example.elib.book.repository.BookRepository;
import com.example.elib.common.dto.pagination.PageData;
import com.example.elib.common.exception.DuplicateResourceException;
import com.example.elib.common.exception.ResourceNotFoundException;
import com.example.elib.common.service.InventoryNumberGenerator;
import com.example.elib.copy.dto.request.CreateCopyDto;
import com.example.elib.copy.dto.request.GetCopyCriteriaDto;
import com.example.elib.copy.dto.request.SetRegularHolderDto;
import com.example.elib.copy.dto.request.UpdateCopyDto;
import com.example.elib.copy.dto.request.pagination.CopySearchCriteria;
import com.example.elib.copy.dto.request.pagination.CopySortCriteria;
import com.example.elib.copy.dto.response.CopyDto;
import com.example.elib.copy.dto.response.CopyShortDto;
import com.example.elib.copy.entity.Copy;
import com.example.elib.copy.enums.CopyEvent;
import com.example.elib.copy.enums.CopyStatus;
import com.example.elib.copy.mapper.CopyMapper;
import com.example.elib.copy.repository.CopyRepository;
import com.example.elib.copy.repository.spec.CopySpecificationBuilder;
import com.example.elib.copy.service.CopyService;
import com.example.elib.copy.service.impl.utils.CopyPageRequestUtils;
import com.example.elib.copy.sm.CopyStateMachineConfig;
import com.example.elib.holder.entity.Holder;
import com.example.elib.holder.repository.HolderRepository;
import com.github.oxo42.stateless4j.StateMachine;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.elib.copy.enums.CopySortField;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
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
    private final CopySpecificationBuilder specBuilder;

    private final InventoryNumberGenerator numberGenerator;

    @Override
    @Transactional
    public CopyDto createCopy(CreateCopyDto dto) {
        if (copyRepository.existsByIsbn(dto.getIsbn())) {
            throw new DuplicateResourceException("Экземпляр с isbn '" + dto.getIsbn() + "' уже существует.");
        }
        Book book = bookRepository.findById(dto.getBookId())
                .orElseThrow(() -> new ResourceNotFoundException("Книга с id " + dto.getBookId() + " не найдена."));

        Copy copy = Copy.addCopy(numberGenerator.generate(),
                dto.getIsbn(),
                book);
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
        Copy copy = copyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Экземпляр с id " + id + " не найден."));

        if (copy.getHolder() == null) {
            throw new IllegalStateException("Нельзя сделать доступным экземпляр без установленного постоянного места храниения.");
        }

        return executeTransition(id, CopyEvent.MAKE_AVAILABLE);
    }

    @Override
    @Transactional
    public CopyDto setReserved(UUID id) {
        return executeTransition(id, CopyEvent.RESERVE);
    }

    @Override
    @Transactional
    public CopyDto cancelReserve(UUID id) {
        return executeTransition(id, CopyEvent.CANCEL_RESERVE);
    }

    @Override
    @Transactional
    public CopyDto setIssued(UUID id) {
        return executeTransition(id, CopyEvent.ISSUE);
    }

    @Override
    @Transactional
    public CopyDto setInTransit(UUID id) {
        return executeTransition(id, CopyEvent.RETURN);
    }

    @Override
    @Transactional
    public CopyDto setShelved(UUID id) {
        return executeTransition(id, CopyEvent.SHELVE);
    }

    @Override
    @Transactional
    public CopyDto setWrittenOff(UUID id) {
        return executeTransition(id, CopyEvent.WRITE_OFF);
    }

    /**
     * Производит переход экземплара книги из 1 состояния в другое посредством собыния - входного параметра перехода
     * @param id id экземпляра
     * @param event входной параметр функции перехода машины состояний
     * @return dto экземпляра
     */
    private CopyDto executeTransition(UUID id, CopyEvent event) {
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

    @Override
    @Transactional(readOnly = true)
    public CopyDto getCopy(UUID id) {
        Copy copy = copyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Экземпляр с id " + id + " не найден."));
        return copyMapper.toDto(copy);
    }

    @Override
    @Transactional(readOnly = true)
    public CopyDto getRandomAvailableCopyByBookId(UUID bookId) {
        if (!bookRepository.existsById(bookId)) {
            throw new ResourceNotFoundException("Книга с id " + bookId + " не найдена.");
        }

        List<Copy> availableCopies = copyRepository.findByBookIdAndStatus(bookId, CopyStatus.AVAILABLE);
        if (availableCopies.isEmpty()) {
            throw new ResourceNotFoundException("Нет доступных экземпляров для книги с id " + bookId);
        }

        Copy randomCopy = availableCopies.get(ThreadLocalRandom.current().nextInt(availableCopies.size()));
        return copyMapper.toDto(randomCopy);
    }

    @Override
    public Page<CopyShortDto> getCopiesPage(GetCopyCriteriaDto criteria) {
        CopySearchCriteria searchCriteria = criteria.getSearchCriteria();
        CopySortCriteria sortCriteria = criteria.getSortCriteria();
        PageData pageData = criteria.getPageData();

        Specification<Copy> spec = specBuilder.fromCriteria(searchCriteria);
        PageRequest pageRequest = CopyPageRequestUtils.buildPageRequest(pageData, sortCriteria);

        Page<Copy> copyPage = copyRepository.findAll(spec, pageRequest);
        return copyPage.map(copyMapper::toShortDto);
    }
}