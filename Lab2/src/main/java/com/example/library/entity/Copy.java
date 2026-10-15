package com.example.library.entity;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "copies")
public class Copy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "book_edition_id", nullable = false)
    private BookEdition bookEdition;

    @Column(nullable = false, unique = true)
    private String inventoryNumber;

    @Column(nullable = false)
    private String condition;

    @OneToMany(mappedBy = "copy", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Loan> loans = new ArrayList<>();

    public Copy() {
    }

    public Copy(BookEdition bookEdition, String inventoryNumber, String condition) {
        this.bookEdition = bookEdition;
        this.inventoryNumber = inventoryNumber;
        this.condition = condition;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BookEdition getBookEdition() {
        return bookEdition;
    }

    public void setBookEdition(BookEdition bookEdition) {
        this.bookEdition = bookEdition;
    }

    public String getInventoryNumber() {
        return inventoryNumber;
    }

    public void setInventoryNumber(String inventoryNumber) {
        this.inventoryNumber = inventoryNumber;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public List<Loan> getLoans() {
        return loans;
    }

    public void setLoans(List<Loan> loans) {
        this.loans = loans;
    }
}
