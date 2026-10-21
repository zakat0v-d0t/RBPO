package com.example.library.service;

import com.example.library.entity.User;
import com.example.library.exception.BadRequestException;
import com.example.library.exception.ResourceNotFoundException;
import com.example.library.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional(readOnly = true)
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Transactional(readOnly = true)
    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Нет такого юзера с id: " + id));
    }

    public User createUser(User user) {
        if (user.getEmail() != null && userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new BadRequestException("Юзер с такой почтой " + user.getEmail() + " уже есть");
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
                        throw new BadRequestException("Почта " + patchData.getEmail() + " уже занята");
                    });
            existing.setEmail(patchData.getEmail());
        }
        if (patchData.getPhone() != null) {
            existing.setPhone(patchData.getPhone());
        }
        return userRepository.save(existing);
    }

    public void deleteUser(Long id) {
        User user = getUserById(id);
        userRepository.delete(user);
    }
}
