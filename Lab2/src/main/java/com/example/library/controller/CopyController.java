package com.example.library.controller;

import com.example.library.dto.CopyDto;
import com.example.library.entity.Copy;
import com.example.library.service.CopyService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/copies")
public class CopyController {

    private final CopyService copyService;

    public CopyController(CopyService copyService) {
        this.copyService = copyService;
    }

    @GetMapping
    public ResponseEntity<List<CopyDto>> getAllCopies() {
        List<CopyDto> dtos = copyService.getAllCopies().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CopyDto> getCopyById(@PathVariable Long id) {
        Copy copy = copyService.getCopyById(id);
        return ResponseEntity.ok(toDto(copy));
    }

    @PostMapping
    public ResponseEntity<CopyDto> createCopy(@Valid @RequestBody CopyDto dto) {
        Copy created = copyService.createCopy(
                dto.getBookEditionId(),
                dto.getInventoryNumber(),
                dto.getCondition()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(toDto(created));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<CopyDto> updateCopy(@PathVariable Long id, @RequestBody CopyDto dto) {
        Copy updated = copyService.updateCopy(
                id,
                dto.getBookEditionId(),
                dto.getInventoryNumber(),
                dto.getCondition()
        );
        return ResponseEntity.ok(toDto(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCopy(@PathVariable Long id) {
        copyService.deleteCopy(id);
        return ResponseEntity.noContent().build();
    }

    private CopyDto toDto(Copy copy) {
        return new CopyDto(
                copy.getId(),
                copy.getBookEdition() != null ? copy.getBookEdition().getId() : null,
                copy.getInventoryNumber(),
                copy.getCondition()
        );
    }
}
