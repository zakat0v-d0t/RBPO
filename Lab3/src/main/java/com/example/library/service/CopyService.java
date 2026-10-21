package com.example.library.service;

import com.example.library.entity.BookEdition;
import com.example.library.entity.Copy;
import com.example.library.exception.BadRequestException;
import com.example.library.exception.ResourceNotFoundException;
import com.example.library.repository.CopyRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class CopyService {

    private final CopyRepository copyRepository;
    private final BookEditionService bookEditionService;

    public CopyService(CopyRepository copyRepository, BookEditionService bookEditionService) {
        this.copyRepository = copyRepository;
        this.bookEditionService = bookEditionService;
    }

    @Transactional(readOnly = true)
    public List<Copy> getAllCopies() {
        return copyRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Copy getCopyById(Long id) {
        return copyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Экземпляр с id: " + id + " не найден"));
    }

    public Copy createCopy(Long bookEditionId, String inventoryNumber, String condition) {
        BookEdition bookEdition = bookEditionService.getBookEditionById(bookEditionId);
        if (copyRepository.findByInventoryNumber(inventoryNumber).isPresent()) {
            throw new BadRequestException("Экземпляр с таким номером " + inventoryNumber + " уже есть");
        }
        Copy copy = new Copy(bookEdition, inventoryNumber, condition);
        return copyRepository.save(copy);
    }

    public Copy updateCopy(Long id, Long newBookEditionId, String inventoryNumber, String condition) {
        Copy existing = getCopyById(id);

        if (newBookEditionId != null) {
            BookEdition bookEdition = bookEditionService.getBookEditionById(newBookEditionId);
            existing.setBookEdition(bookEdition);
        }

        if (inventoryNumber != null && !inventoryNumber.isBlank()) {
            copyRepository.findByInventoryNumber(inventoryNumber)
                    .filter(c -> !c.getId().equals(id))
                    .ifPresent(c -> {
                        throw new BadRequestException("Инвентарный номер " + inventoryNumber + " уже занят");
                    });
            existing.setInventoryNumber(inventoryNumber);
        }

        if (condition != null && !condition.isBlank()) {
            existing.setCondition(condition);
        }

        return copyRepository.save(existing);
    }

    @Transactional(readOnly = true)
    public List<Copy> getAvailableCopies(Long bookEditionId) {
        if (bookEditionId != null) {
            bookEditionService.getBookEditionById(bookEditionId);
            return copyRepository.findAvailableCopiesByEditionId(bookEditionId);
        }
        return copyRepository.findAll().stream()
                .filter(copy -> copy.getLoans().stream().noneMatch(loan -> loan.getReturnDate() == null))
                .toList();
    }

    public void deleteCopy(Long id) {
        Copy copy = getCopyById(id);
        copyRepository.delete(copy);
    }
}
