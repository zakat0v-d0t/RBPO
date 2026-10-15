package com.example.library.service;

import com.example.library.entity.Copy;
import com.example.library.entity.Loan;
import com.example.library.entity.User;
import com.example.library.exception.ResourceNotFoundException;
import com.example.library.repository.LoanRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class LoanService {

    private final LoanRepository loanRepository;
    private final CopyService copyService;
    private final UserService userService;

    public LoanService(LoanRepository loanRepository, CopyService copyService, UserService userService) {
        this.loanRepository = loanRepository;
        this.copyService = copyService;
        this.userService = userService;
    }

    @Transactional(readOnly = true)
    public List<Loan> getAllLoans() {
        return loanRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Loan getLoanById(Long id) {
        return loanRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Запись о выдаче с id: " + id + " не найдена"));
    }

    public Loan createLoan(Long copyId, Long userId) {
        Copy copy = copyService.getCopyById(copyId);
        User user = userService.getUserById(userId);

        LocalDateTime now = LocalDateTime.now();
        Loan loan = new Loan(copy, user, now);
        return loanRepository.save(loan);
    }

    public Loan updateLoan(Long id, Long newCopyId, Long newUserId, Boolean returned) {
        Loan existing = getLoanById(id);

        if (newCopyId != null) {
            Copy copy = copyService.getCopyById(newCopyId);
            existing.setCopy(copy);
        }

        if (newUserId != null) {
            User user = userService.getUserById(newUserId);
            existing.setUser(user);
        }

        if (returned != null) {
            if (returned) {
                existing.setReturnDate(LocalDateTime.now());
            } else {
                existing.setReturnDate(null);
            }
        }

        return loanRepository.save(existing);
    }

    public void deleteLoan(Long id) {
        Loan loan = getLoanById(id);
        loanRepository.delete(loan);
    }
}
