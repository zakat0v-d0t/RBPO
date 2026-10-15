package com.example.library.controller;

import com.example.library.dto.BookEditionDto;
import com.example.library.entity.BookEdition;
import com.example.library.service.BookEditionService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/book-editions")
public class BookEditionController {

    private final BookEditionService bookEditionService;

    public BookEditionController(BookEditionService bookEditionService) {
        this.bookEditionService = bookEditionService;
    }

    @GetMapping
    public ResponseEntity<List<BookEditionDto>> getAllBookEditions() {
        List<BookEditionDto> dtos = bookEditionService.getAllBookEditions().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookEditionDto> getBookEditionById(@PathVariable Long id) {
        BookEdition bookEdition = bookEditionService.getBookEditionById(id);
        return ResponseEntity.ok(toDto(bookEdition));
    }

    @PostMapping
    public ResponseEntity<BookEditionDto> createBookEdition(@Valid @RequestBody BookEditionDto dto) {
        BookEdition bookEdition = new BookEdition(dto.getTitle(), dto.getAuthor(), dto.getIsbn());
        BookEdition created = bookEditionService.createBookEdition(bookEdition);
        return ResponseEntity.status(HttpStatus.CREATED).body(toDto(created));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<BookEditionDto> updateBookEdition(@PathVariable Long id, @RequestBody BookEditionDto dto) {
        BookEdition patch = new BookEdition(dto.getTitle(), dto.getAuthor(), dto.getIsbn());
        BookEdition updated = bookEditionService.updateBookEdition(id, patch);
        return ResponseEntity.ok(toDto(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBookEdition(@PathVariable Long id) {
        bookEditionService.deleteBookEdition(id);
        return ResponseEntity.noContent().build();
    }

    private BookEditionDto toDto(BookEdition bookEdition) {
        return new BookEditionDto(
                bookEdition.getId(),
                bookEdition.getTitle(),
                bookEdition.getAuthor(),
                bookEdition.getIsbn()
        );
    }
}
