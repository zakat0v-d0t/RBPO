package com.example.library.controller;

import com.example.library.dto.LoanDto;
import com.example.library.entity.Loan;
import com.example.library.service.LoanService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/loans")
public class LoanController {

    private final LoanService loanService;

    public LoanController(LoanService loanService) {
        this.loanService = loanService;
    }

    @GetMapping
    public ResponseEntity<List<LoanDto>> getAllLoans() {
        List<LoanDto> dtos = loanService.getAllLoans().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<LoanDto> getLoanById(@PathVariable Long id) {
        Loan loan = loanService.getLoanById(id);
        return ResponseEntity.ok(toDto(loan));
    }

    @PostMapping
    public ResponseEntity<LoanDto> createLoan(@Valid @RequestBody LoanDto dto) {
        Loan created = loanService.createLoan(dto.getCopyId(), dto.getUserId());
        return ResponseEntity.status(HttpStatus.CREATED).body(toDto(created));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<LoanDto> updateLoan(@PathVariable Long id, @RequestBody LoanDto dto) {
        Loan updated = loanService.updateLoan(
                id,
                dto.getCopyId(),
                dto.getUserId(),
                dto.getReturned()
        );
        return ResponseEntity.ok(toDto(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLoan(@PathVariable Long id) {
        loanService.deleteLoan(id);
        return ResponseEntity.noContent().build();
    }

    private LoanDto toDto(Loan loan) {
        return new LoanDto(
                loan.getId(),
                loan.getCopy() != null ? loan.getCopy().getId() : null,
                loan.getUser() != null ? loan.getUser().getId() : null,
                loan.getLoanDate(),
                loan.getReturnDate()
        );
    }
}
