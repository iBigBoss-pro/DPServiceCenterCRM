package com.ibigboss.crm.service;

import com.ibigboss.crm.dto.request.UserCreateRequest;
import com.ibigboss.crm.dto.request.UserUpdateRequest;
import com.ibigboss.crm.dto.response.UserResponse;
import entity.user.Role;
import entity.user.User;
import com.ibigboss.crm.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public UserResponse create(UserCreateRequest request, Long serviceCenterId) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new IllegalArgumentException("Пользователь с таким логином уже существует");
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Пользователь с таким email уже существует");
        }
        User user = new User(
            request.getUsername(),
            passwordEncoder.encode(request.getPassword()),
            Role.valueOf(request.getRole()),
            request.getFirstName(),
            request.getLastName(),
            request.getEmail()
        );
        user.setServiceCenterIdForTest(serviceCenterId);
        user.setLocationId(request.getLocationId());
        user = userRepository.save(user);
        return toResponse(user);
    }

    @Transactional
    public UserResponse update(Long id, UserUpdateRequest request, Long serviceCenterId) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Пользователь не найден: " + id));
        if (request.getUsername() != null) user.setUsername(request.getUsername());
        if (request.getPassword() != null && !request.getPassword().isBlank()) {
            user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        }
        if (request.getRole() != null) user.setRole(Role.valueOf(request.getRole()));
        if (request.getFirstName() != null) user.setFirstName(request.getFirstName());
        if (request.getLastName() != null) user.setLastName(request.getLastName());
        if (request.getEmail() != null) user.setEmail(request.getEmail());
        if (request.getLocationId() != null) user.setLocationId(request.getLocationId());
        if (request.getActive() != null) user.setActive(request.getActive());
        user = userRepository.save(user);
        return toResponse(user);
    }

    public UserResponse getById(Long id) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Пользователь не найден: " + id));
        return toResponse(user);
    }

    public UserResponse getByUsername(String username) {
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new IllegalArgumentException("Пользователь не найден: " + username));
        return toResponse(user);
    }

    public List<UserResponse> listAll() {
        return userRepository.findAll().stream()
            .map(this::toResponse)
            .toList();
    }

    @Transactional
    public void delete(Long id) {
        userRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Пользователь не найден: " + id));
        userRepository.deleteById(id);
    }

    private UserResponse toResponse(User user) {
        UserResponse response = new UserResponse();
        response.setId(user.getId());
        response.setUsername(user.getUsername());
        response.setRole(user.getRole().name());
        response.setFirstName(user.getFirstName());
        response.setLastName(user.getLastName());
        response.setEmail(user.getEmail());
        response.setLocationId(user.getLocationId());
        response.setActive(user.isActive());
        response.setCompletedOrdersCount(user.getCompletedOrdersCount());
        response.setCompletedWorksCount(user.getCompletedWorksCount());
        response.setTotalRevenue(user.getTotalRevenue());
        response.setLastCompletedAt(user.getLastCompletedAt());
        return response;
    }
}
