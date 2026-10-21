package com.example.library.service;

import com.example.library.entity.BookEdition;
import com.example.library.entity.User;
import com.example.library.entity.WaitingRequest;
import com.example.library.exception.BadRequestException;
import com.example.library.exception.ResourceNotFoundException;
import com.example.library.repository.WaitingRequestRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class WaitingRequestService {

    private final WaitingRequestRepository waitingRequestRepository;
    private final BookEditionService bookEditionService;
    private final UserService userService;

    public WaitingRequestService(WaitingRequestRepository waitingRequestRepository,
                                 BookEditionService bookEditionService,
                                 UserService userService) {
        this.waitingRequestRepository = waitingRequestRepository;
        this.bookEditionService = bookEditionService;
        this.userService = userService;
    }

    @Transactional(readOnly = true)
    public List<WaitingRequest> getAllWaitingRequests() {
        return waitingRequestRepository.findAll();
    }

    @Transactional(readOnly = true)
    public WaitingRequest getWaitingRequestById(Long id) {
        return waitingRequestRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Заявка с id: " + id + " не найдена"));
    }

    public WaitingRequest subscribeToWaitlist(Long bookEditionId, Long userId) {
        BookEdition bookEdition = bookEditionService.getBookEditionById(bookEditionId);
        User user = userService.getUserById(userId);

        if (waitingRequestRepository.existsByBookEditionIdAndUserIdAndStatus(bookEditionId, userId, "PENDING")) {
            throw new BadRequestException("Читатель уже находится в активной очереди ожидания на данную книгу!");
        }

        LocalDateTime now = LocalDateTime.now();
        WaitingRequest waitingRequest = new WaitingRequest(bookEdition, user, now, "PENDING");
        return waitingRequestRepository.save(waitingRequest);
    }

    public WaitingRequest createWaitingRequest(Long bookEditionId, Long userId, String status) {
        BookEdition bookEdition = bookEditionService.getBookEditionById(bookEditionId);
        User user = userService.getUserById(userId);

        LocalDateTime now = LocalDateTime.now();
        String initialStatus = (status != null && !status.isBlank()) ? status : "PENDING";
        WaitingRequest waitingRequest = new WaitingRequest(bookEdition, user, now, initialStatus);
        return waitingRequestRepository.save(waitingRequest);
    }

    public WaitingRequest updateWaitingRequest(Long id, Long newBookEditionId, Long newUserId, String status) {
        WaitingRequest existing = getWaitingRequestById(id);

        if (newBookEditionId != null) {
            BookEdition bookEdition = bookEditionService.getBookEditionById(newBookEditionId);
            existing.setBookEdition(bookEdition);
        }

        if (newUserId != null) {
            User user = userService.getUserById(newUserId);
            existing.setUser(user);
        }

        if (status != null && !status.isBlank()) {
            existing.setStatus(status);
        }

        return waitingRequestRepository.save(existing);
    }

    public void deleteWaitingRequest(Long id) {
        WaitingRequest waitingRequest = getWaitingRequestById(id);
        waitingRequestRepository.delete(waitingRequest);
    }
}
