package com.example.library.repository;

import com.example.library.entity.Loan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LoanRepository extends JpaRepository<Loan, Long> {
    List<Loan> findByUserId(Long userId);
    List<Loan> findByCopyId(Long copyId);
    boolean existsByCopyIdAndReturnDateIsNull(Long copyId);
    List<Loan> findByUserIdAndReturnDateIsNull(Long userId);
}
