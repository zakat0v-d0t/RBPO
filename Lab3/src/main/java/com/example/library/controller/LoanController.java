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

    @PostMapping("/issue")
    public ResponseEntity<LoanDto> issueBook(@Valid @RequestBody LoanDto dto) {
        Loan created = loanService.issueBook(dto.getCopyId(), dto.getUserId());
        return ResponseEntity.status(HttpStatus.CREATED).body(toDto(created));
    }

    @PostMapping("/{id}/return")
    public ResponseEntity<LoanDto> returnBook(@PathVariable Long id) {
        Loan returned = loanService.returnBook(id);
        return ResponseEntity.ok(toDto(returned));
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
        LoanDto dto = new LoanDto();
        dto.setId(loan.getId());
        dto.setCopyId(loan.getCopy().getId());
        dto.setUserId(loan.getUser().getId());
        dto.setLoanDate(loan.getLoanDate());
        dto.setReturnDate(loan.getReturnDate());
        dto.setReturned(loan.getReturnDate() != null);
        return dto;
    }
}
