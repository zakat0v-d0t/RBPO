package com.example.library.dto;

import com.example.library.entity.Role;
import com.example.library.entity.UserStatus;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class CreateUserDto {

    @NotBlank(message = "Необходимо указать логин (username)")
    private String username;

    @NotBlank(message = "Пароль обязателен для заполнения")
    private String password;

    @NotBlank(message = "Надо указать имя")
    private String name;

    @NotBlank(message = "Надо указать почту")
    @Email(message = "Неправильный формат почты")
    private String email;

    private String phone;

    private Role role;

    private UserStatus status;

    public CreateUserDto() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public UserStatus getStatus() {
        return status;
    }

    public void setStatus(UserStatus status) {
        this.status = status;
    }
}
