package com.example.library.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public class LoanDto {

    private Long id;

    @NotNull(message = "Надо указать id копии")
    private Long copyId;

    @NotNull(message = "Надо указать id юзера")
    private Long userId;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime loanDate;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime returnDate;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Boolean returned;

    public LoanDto() {
    }

    public LoanDto(Long id, Long copyId, Long userId, LocalDateTime loanDate, LocalDateTime returnDate) {
        this.id = id;
        this.copyId = copyId;
        this.userId = userId;
        this.loanDate = loanDate;
        this.returnDate = returnDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCopyId() {
        return copyId;
    }

    public void setCopyId(Long copyId) {
        this.copyId = copyId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public LocalDateTime getLoanDate() {
        return loanDate;
    }

    public void setLoanDate(LocalDateTime loanDate) {
        this.loanDate = loanDate;
    }

    public LocalDateTime getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(LocalDateTime returnDate) {
        this.returnDate = returnDate;
    }

    public Boolean getReturned() {
        return returned;
    }

    public void setReturned(Boolean returned) {
        this.returned = returned;
    }
}
