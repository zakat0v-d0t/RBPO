package com.example.library.repository;

import com.example.library.entity.Copy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CopyRepository extends JpaRepository<Copy, Long> {
    Optional<Copy> findByInventoryNumber(String inventoryNumber);
    List<Copy> findByBookEditionId(Long bookEditionId);
}
