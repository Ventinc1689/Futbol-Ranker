package com.ventinc.futbol_ranker.service;

import com.ventinc.futbol_ranker.DTO.*;
import com.ventinc.futbol_ranker.model.Users;
import com.ventinc.futbol_ranker.repo.UserRepo;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UsersService {

    private final UserRepo userRepo;
    private final JWTService jwtService;

    AuthenticationManager authManager; // Handles the actual authentication process

    // BCrypt encoder with strength 12 (higher = more secure but slower)
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

    public UsersService(UserRepo userRepo, AuthenticationManager authManager, JWTService jwtService) {
        this.userRepo = userRepo;
        this.authManager = authManager;
        this.jwtService = jwtService;
    }

    public List<UserResponseDTO> getAllUsers() {
        return userRepo.findAll().stream()
                .map(UserMapper::toUserResponseDTO)
                .collect(Collectors.toList());
    }

    public UserResponseDTO getUser(Long userId){
        Users user = userRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
        return UserMapper.toUserResponseDTO(user);
    }

    public Long getUserIdByUsername(String username) {
        Users user = userRepo.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return user.getUserId();
    }

    // Register a new user with an encrypted password
    public void register(UserRegistrationDTO userDTO){
        if (userRepo.existsByUsername(userDTO.getUsername())) {
            throw new RuntimeException("Username already exists");
        }
        if (userRepo.existsByEmail(userDTO.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        Users user = UserMapper.fromRegistrationDTO(userDTO);
        user.setPassword(encoder.encode(user.getPassword())); // Encrypt the password before saving
        userRepo.save(user);
    }

    public String verify(UserLoginDTO user){
        // Create an authentication token with the provided username and password
        Authentication authentication =
                authManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));

        // If authentication is successful, generate a JWT token
        if (authentication.isAuthenticated()) {
            return jwtService.generateToken(user.getUsername());
        }
        return "fail";  // Authentication failed
    }

    public void updateUser(Long userId, UserUpdateDTO updatedUser) {
        Users existingUser = userRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        if (!existingUser.getUsername().equals(updatedUser.getUsername()) && userRepo.existsByUsername(updatedUser.getUsername())) {
            throw new RuntimeException("Username already exists");
        }

        UserMapper.updateUserFromDTO(existingUser, updatedUser);

        if (updatedUser.getPassword() != null && !updatedUser.getPassword().isEmpty()) {
            existingUser.setPassword(encoder.encode(updatedUser.getPassword()));
        }

        existingUser.setLastModifiedDate(LocalDateTime.now()); // Update last modified date
        userRepo.save(existingUser);
    }

    public UserResponseDTO deactivateUser(Long userId) {
        Users user = userRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setActive(false);
        Users savedUser = userRepo.save(user);
        return UserMapper.toUserResponseDTO(savedUser);
    }

    public UserResponseDTO activateUser(Long userId) {
        Users user = userRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setActive(true);
        Users savedUser = userRepo.save(user);
        return UserMapper.toUserResponseDTO(savedUser);
    }

    public void deleteUser(Long userId) {
        userRepo.deleteById(userId);
    }


}
