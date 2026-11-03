package com.example.library.service;

import com.example.library.dto.RegisterRequestDto;
import com.example.library.entity.Role;
import com.example.library.entity.User;
import com.example.library.entity.UserStatus;
import com.example.library.exception.BadRequestException;
import com.example.library.exception.ResourceNotFoundException;
import com.example.library.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.regex.Pattern;

@Service
@Transactional
public class UserService {

    private static final Pattern PASSWORD_PATTERN = Pattern.compile(
            "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!._*\\-]).{8,}$"
    );

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional(readOnly = true)
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Transactional(readOnly = true)
    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Пользователь с id " + id + " не найден"));
    }

    @Transactional(readOnly = true)
    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Пользователь с логином '" + username + "' не найден"));
    }

    public User registerUser(RegisterRequestDto dto) {
        if (userRepository.existsByUsername(dto.getUsername())) {
            throw new BadRequestException("Пользователь с логином '" + dto.getUsername() + "' уже зарегистрирован");
        }
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new BadRequestException("Пользователь с email '" + dto.getEmail() + "' уже зарегистрирован");
        }

        validatePassword(dto.getPassword());

        User user = new User();
        user.setUsername(dto.getUsername().trim());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setName(dto.getName().trim());
        user.setEmail(dto.getEmail().trim().toLowerCase());
        user.setPhone(dto.getPhone() != null ? dto.getPhone().trim() : null);
        user.setRole(Role.ROLE_READER);
        user.setStatus(UserStatus.ACTIVE);

        return userRepository.save(user);
    }

    public User createUser(User user) {
        if (user.getUsername() == null || user.getUsername().isBlank()) {
            throw new BadRequestException("Логин пользователя не может быть пустым");
        }
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new BadRequestException("Пользователь с логином '" + user.getUsername() + "' уже существует");
        }
        if (user.getEmail() != null && userRepository.existsByEmail(user.getEmail())) {
            throw new BadRequestException("Пользователь с email '" + user.getEmail() + "' уже существует");
        }
        if (user.getPassword() != null && !user.getPassword().isBlank()) {
            validatePassword(user.getPassword());
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        } else {
            throw new BadRequestException("Пароль не может быть пустым");
        }
        if (user.getRole() == null) {
            user.setRole(Role.ROLE_READER);
        }
        if (user.getStatus() == null) {
            user.setStatus(UserStatus.ACTIVE);
        }
        return userRepository.save(user);
    }

    public User updateUser(Long id, User patchData) {
        User existing = getUserById(id);
        if (patchData.getName() != null && !patchData.getName().isBlank()) {
            existing.setName(patchData.getName());
        }
        if (patchData.getEmail() != null && !patchData.getEmail().isBlank()) {
            userRepository.findByEmail(patchData.getEmail())
                    .filter(u -> !u.getId().equals(id))
                    .ifPresent(u -> {
                        throw new BadRequestException("Почта '" + patchData.getEmail() + "' уже занята другим пользователем");
                    });
            existing.setEmail(patchData.getEmail());
        }
        if (patchData.getPhone() != null) {
            existing.setPhone(patchData.getPhone());
        }
        return userRepository.save(existing);
    }

    public User updateUserStatus(Long id, UserStatus newStatus) {
        if (newStatus == null) {
            throw new BadRequestException("Статус не может быть пустым");
        }
        User existing = getUserById(id);
        existing.setStatus(newStatus);
        return userRepository.save(existing);
    }

    public void deleteUser(Long id) {
        User user = getUserById(id);
        userRepository.delete(user);
    }

    private void validatePassword(String password) {
        if (password == null || !PASSWORD_PATTERN.matcher(password).matches()) {
            throw new BadRequestException(
                    "Пароль не соответствует требованиям безопасности: должен содержать не менее 8 символов, " +
                    "включая хотя бы одну цифру, одну строчную букву, одну заглавную букву и один специальный символ (@#$%^&+=!._*-)"
            );
        }
    }
}
