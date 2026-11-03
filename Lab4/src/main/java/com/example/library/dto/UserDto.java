package com.example.library.dto;

import com.example.library.entity.Role;
import com.example.library.entity.UserStatus;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class UserDto {

    private Long id;

    @NotBlank(message = "Необходимо указать логин (username)")
    private String username;

    @NotBlank(message = "Надо указать имя")
    private String name;

    @NotBlank(message = "Надо указать почту")
    @Email(message = "Неправильный формат почты")
    private String email;

    private String phone;

    private Role role;

    private UserStatus status;

    public UserDto() {
    }

    public UserDto(Long id, String username, String name, String email, String phone, Role role, UserStatus status) {
        this.id = id;
        this.username = username;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.role = role;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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
