package com.example.library.dto;

import com.example.library.entity.UserStatus;
import jakarta.validation.constraints.NotNull;

public class UserStatusUpdateDto {

    @NotNull(message = "Статус не может быть пустым")
    private UserStatus status;

    public UserStatusUpdateDto() {
    }

    public UserStatusUpdateDto(UserStatus status) {
        this.status = status;
    }

    public UserStatus getStatus() {
        return status;
    }

    public void setStatus(UserStatus status) {
        this.status = status;
    }
}
