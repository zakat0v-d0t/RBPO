package com.example.library.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public class WaitingRequestDto {

    private Long id;

    @NotNull(message = "Надо указать id книги")
    private Long bookEditionId;

    @NotNull(message = "Надо указать id юзера")
    private Long userId;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime requestDate;

    private String status;

    public WaitingRequestDto() {
    }

    public WaitingRequestDto(Long id, Long bookEditionId, Long userId, LocalDateTime requestDate, String status) {
        this.id = id;
        this.bookEditionId = bookEditionId;
        this.userId = userId;
        this.requestDate = requestDate;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getBookEditionId() {
        return bookEditionId;
    }

    public void setBookEditionId(Long bookEditionId) {
        this.bookEditionId = bookEditionId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public LocalDateTime getRequestDate() {
        return requestDate;
    }

    public void setRequestDate(LocalDateTime requestDate) {
        this.requestDate = requestDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
