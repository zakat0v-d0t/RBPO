package com.example.library.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class CopyDto {

    private Long id;

    @NotNull(message = "Надо указать id книги")
    private Long bookEditionId;

    @NotBlank(message = "Надо указать инвентарный номер")
    private String inventoryNumber;

    @NotBlank(message = "Надо указать состояние")
    private String condition;

    public CopyDto() {
    }

    public CopyDto(Long id, Long bookEditionId, String inventoryNumber, String condition) {
        this.id = id;
        this.bookEditionId = bookEditionId;
        this.inventoryNumber = inventoryNumber;
        this.condition = condition;
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
}
