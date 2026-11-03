package com.example.library.security;

import com.example.library.entity.Loan;
import com.example.library.entity.User;
import com.example.library.entity.WaitingRequest;
import com.example.library.repository.LoanRepository;
import com.example.library.repository.WaitingRequestRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service("securityService")
public class SecurityService {

    private final LoanRepository loanRepository;
    private final WaitingRequestRepository waitingRequestRepository;

    public SecurityService(LoanRepository loanRepository, WaitingRequestRepository waitingRequestRepository) {
        this.loanRepository = loanRepository;
        this.waitingRequestRepository = waitingRequestRepository;
    }

    public boolean isCurrentUser(Long userId, Authentication authentication) {
        if (userId == null || authentication == null || !authentication.isAuthenticated()) {
            return false;
        }
        Object principal = authentication.getPrincipal();
        if (principal instanceof User user) {
            return user.getId() != null && user.getId().equals(userId);
        }
        return false;
    }

    public boolean isLoanOwner(Long loanId, Authentication authentication) {
        if (loanId == null || authentication == null || !authentication.isAuthenticated()) {
            return false;
        }
        Object principal = authentication.getPrincipal();
        if (principal instanceof User user) {
            Optional<Loan> loan = loanRepository.findById(loanId);
            return loan.map(l -> l.getUser().getId().equals(user.getId())).orElse(false);
        }
        return false;
    }

    public boolean isWaitingRequestOwner(Long requestId, Authentication authentication) {
        if (requestId == null || authentication == null || !authentication.isAuthenticated()) {
            return false;
        }
        Object principal = authentication.getPrincipal();
        if (principal instanceof User user) {
            Optional<WaitingRequest> request = waitingRequestRepository.findById(requestId);
            return request.map(r -> r.getUser().getId().equals(user.getId())).orElse(false);
        }
        return false;
    }
}
