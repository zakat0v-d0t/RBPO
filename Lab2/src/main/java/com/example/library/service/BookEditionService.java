package com.example.library.service;

import com.example.library.entity.BookEdition;
import com.example.library.exception.BadRequestException;
import com.example.library.exception.ResourceNotFoundException;
import com.example.library.repository.BookEditionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class BookEditionService {

    private final BookEditionRepository bookEditionRepository;

    public BookEditionService(BookEditionRepository bookEditionRepository) {
        this.bookEditionRepository = bookEditionRepository;
    }

    @Transactional(readOnly = true)
    public List<BookEdition> getAllBookEditions() {
        return bookEditionRepository.findAll();
    }

    @Transactional(readOnly = true)
    public BookEdition getBookEditionById(Long id) {
        return bookEditionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Книга с id: " + id + " не найдена"));
    }

    public BookEdition createBookEdition(BookEdition bookEdition) {
        if (bookEdition.getIsbn() != null && bookEditionRepository.findByIsbn(bookEdition.getIsbn()).isPresent()) {
            throw new BadRequestException("Книга с таким ISBN " + bookEdition.getIsbn() + " уже добавлена");
        }
        return bookEditionRepository.save(bookEdition);
    }

    public BookEdition updateBookEdition(Long id, BookEdition patchData) {
        BookEdition existing = getBookEditionById(id);
        if (patchData.getTitle() != null && !patchData.getTitle().isBlank()) {
            existing.setTitle(patchData.getTitle());
        }
        if (patchData.getAuthor() != null && !patchData.getAuthor().isBlank()) {
            existing.setAuthor(patchData.getAuthor());
        }
        if (patchData.getIsbn() != null && !patchData.getIsbn().isBlank()) {
            bookEditionRepository.findByIsbn(patchData.getIsbn())
                    .filter(b -> !b.getId().equals(id))
                    .ifPresent(b -> {
                        throw new BadRequestException("Этот ISBN " + patchData.getIsbn() + " уже используется");
                    });
            existing.setIsbn(patchData.getIsbn());
        }
        return bookEditionRepository.save(existing);
    }

    public void deleteBookEdition(Long id) {
        BookEdition bookEdition = getBookEditionById(id);
        bookEditionRepository.delete(bookEdition);
    }
}
