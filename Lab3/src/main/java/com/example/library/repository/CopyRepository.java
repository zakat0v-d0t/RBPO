package com.example.library.repository;

import com.example.library.entity.Copy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CopyRepository extends JpaRepository<Copy, Long> {
    Optional<Copy> findByInventoryNumber(String inventoryNumber);
    List<Copy> findByBookEditionId(Long bookEditionId);

    @Query("SELECT c FROM Copy c WHERE c.bookEdition.id = :editionId AND c.id NOT IN (SELECT l.copy.id FROM Loan l WHERE l.returnDate IS NULL)")
    List<Copy> findAvailableCopiesByEditionId(@Param("editionId") Long editionId);
}
