package com.example.library.service;

import com.example.library.entity.Copy;
import com.example.library.entity.Loan;
import com.example.library.entity.User;
import com.example.library.entity.WaitingRequest;
import com.example.library.exception.BadRequestException;
import com.example.library.exception.ResourceNotFoundException;
import com.example.library.repository.LoanRepository;
import com.example.library.repository.WaitingRequestRepository;
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
    private final WaitingRequestRepository waitingRequestRepository;

    public LoanService(LoanRepository loanRepository,
                       CopyService copyService,
                       UserService userService,
                       WaitingRequestRepository waitingRequestRepository) {
        this.loanRepository = loanRepository;
        this.copyService = copyService;
        this.userService = userService;
        this.waitingRequestRepository = waitingRequestRepository;
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

    public Loan issueBook(Long copyId, Long userId) {
        Copy copy = copyService.getCopyById(copyId);
        User user = userService.getUserById(userId);

        if (loanRepository.existsByCopyIdAndReturnDateIsNull(copyId)) {
            throw new BadRequestException("Экземпляр книги (id: " + copyId + ", инв. номер: "
                    + copy.getInventoryNumber() + ") уже находится на руках у читателя!");
        }

        LocalDateTime now = LocalDateTime.now();
        Loan loan = new Loan(copy, user, now);
        return loanRepository.save(loan);
    }

    public Loan createLoan(Long copyId, Long userId) {
        return issueBook(copyId, userId);
    }

    public Loan returnBook(Long id) {
        Loan loan = getLoanById(id);
        if (loan.getReturnDate() != null) {
            throw new BadRequestException("Книга по данной выдаче (id " + id + ") уже была возвращена " + loan.getReturnDate());
        }

        loan.setReturnDate(LocalDateTime.now());
        Loan savedLoan = loanRepository.save(loan);

        Long bookEditionId = loan.getCopy().getBookEdition().getId();
        List<WaitingRequest> queue = waitingRequestRepository
                .findByBookEditionIdAndStatusOrderByRequestDateAsc(bookEditionId, "PENDING");

        if (!queue.isEmpty()) {
            WaitingRequest nextWaiting = queue.get(0);
            nextWaiting.setStatus("FULFILLED");
            waitingRequestRepository.save(nextWaiting);
        }

        return savedLoan;
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
                if (existing.getReturnDate() == null) {
                    return returnBook(id);
                }
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
