package com.example.library.controller;

import com.example.library.dto.WaitingRequestDto;
import com.example.library.entity.Role;
import com.example.library.entity.User;
import com.example.library.entity.WaitingRequest;
import com.example.library.service.WaitingRequestService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/waiting-requests")
public class WaitingRequestController {

    private final WaitingRequestService waitingRequestService;

    public WaitingRequestController(WaitingRequestService waitingRequestService) {
        this.waitingRequestService = waitingRequestService;
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('READER', 'ADMIN')")
    public ResponseEntity<List<WaitingRequestDto>> getAllWaitingRequests(Authentication authentication) {
        List<WaitingRequest> requests;
        if (authentication != null && authentication.getPrincipal() instanceof User user) {
            if (user.getRole() == Role.ROLE_ADMIN) {
                requests = waitingRequestService.getAllWaitingRequests();
            } else {
                requests = waitingRequestService.getWaitingRequestsByUserId(user.getId());
            }
        } else {
            requests = List.of();
        }

        List<WaitingRequestDto> dtos = requests.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @securityService.isWaitingRequestOwner(#id, authentication)")
    public ResponseEntity<WaitingRequestDto> getWaitingRequestById(@PathVariable Long id) {
        WaitingRequest request = waitingRequestService.getWaitingRequestById(id);
        return ResponseEntity.ok(toDto(request));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<WaitingRequestDto> createWaitingRequest(@Valid @RequestBody WaitingRequestDto dto) {
        WaitingRequest created = waitingRequestService.createWaitingRequest(
                dto.getBookEditionId(),
                dto.getUserId(),
                dto.getStatus()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(toDto(created));
    }

    @PostMapping("/subscribe")
    @PreAuthorize("hasRole('ADMIN') or @securityService.isCurrentUser(#dto.userId, authentication)")
    public ResponseEntity<WaitingRequestDto> subscribe(@Valid @RequestBody WaitingRequestDto dto) {
        WaitingRequest created = waitingRequestService.subscribeToWaitlist(
                dto.getBookEditionId(),
                dto.getUserId()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(toDto(created));
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<WaitingRequestDto> updateWaitingRequest(@PathVariable Long id, @RequestBody WaitingRequestDto dto) {
        WaitingRequest updated = waitingRequestService.updateWaitingRequest(
                id,
                dto.getBookEditionId(),
                dto.getUserId(),
                dto.getStatus()
        );
        return ResponseEntity.ok(toDto(updated));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @securityService.isWaitingRequestOwner(#id, authentication)")
    public ResponseEntity<Void> deleteWaitingRequest(@PathVariable Long id) {
        waitingRequestService.deleteWaitingRequest(id);
        return ResponseEntity.noContent().build();
    }

    private WaitingRequestDto toDto(WaitingRequest request) {
        WaitingRequestDto dto = new WaitingRequestDto();
        dto.setId(request.getId());
        dto.setBookEditionId(request.getBookEdition().getId());
        dto.setUserId(request.getUser().getId());
        dto.setRequestDate(request.getRequestDate());
        dto.setStatus(request.getStatus());
        return dto;
    }
}
