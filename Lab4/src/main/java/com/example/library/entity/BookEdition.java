package com.example.library.entity;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "book_editions")
public class BookEdition {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String author;

    @Column(nullable = false, unique = true)
    private String isbn;

    @OneToMany(mappedBy = "bookEdition", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Copy> copies = new ArrayList<>();

    @OneToMany(mappedBy = "bookEdition", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<WaitingRequest> waitingRequests = new ArrayList<>();

    public BookEdition() {
    }

    public BookEdition(String title, String author, String isbn) {
        this.title = title;
        this.author = author;
        this.isbn = isbn;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public List<Copy> getCopies() {
        return copies;
    }

    public void setCopies(List<Copy> copies) {
        this.copies = copies;
    }

    public List<WaitingRequest> getWaitingRequests() {
        return waitingRequests;
    }

    public void setWaitingRequests(List<WaitingRequest> waitingRequests) {
        this.waitingRequests = waitingRequests;
    }
}
