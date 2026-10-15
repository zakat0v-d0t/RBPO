package com.example.library.repository;

import com.example.library.entity.WaitingRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WaitingRequestRepository extends JpaRepository<WaitingRequest, Long> {
    List<WaitingRequest> findByUserId(Long userId);
    List<WaitingRequest> findByBookEditionId(Long bookEditionId);
}
