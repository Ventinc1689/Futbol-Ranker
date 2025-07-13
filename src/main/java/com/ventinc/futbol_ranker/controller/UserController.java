package com.ventinc.futbol_ranker.controller;

import com.ventinc.futbol_ranker.DTO.UserLoginDTO;
import com.ventinc.futbol_ranker.DTO.UserRegistrationDTO;
import com.ventinc.futbol_ranker.DTO.UserResponseDTO;
import com.ventinc.futbol_ranker.DTO.UserUpdateDTO;
import com.ventinc.futbol_ranker.service.UsersService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {

    private final UsersService userService;

    public UserController(UsersService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserResponseDTO> getUserById(@PathVariable Long userId) {
        return ResponseEntity.ok(userService.getUser(userId));
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody UserRegistrationDTO user){
        userService.register(user);
        return ResponseEntity.status(HttpStatus.CREATED).body("User registered successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody UserLoginDTO user){
        String token = userService.verify(user);
        if ("fail".equals(token)) {
            throw new RuntimeException("Invalid credentials");
        }
        return ResponseEntity.ok(Map.of("token", token, "type", "Bearer"));
    }

    @PutMapping("/{userId}")
    public ResponseEntity<Void> updateUser(@PathVariable Long userId, @Valid @RequestBody UserUpdateDTO user) {
        userService.updateUser(userId, user);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{userId}/deactivate")
    public ResponseEntity<UserResponseDTO> deactivateUser(@PathVariable Long userId) {
        UserResponseDTO user = userService.deactivateUser(userId);
        return ResponseEntity.ok(user);
    }

    @PutMapping("/{userId}/activate")
    public ResponseEntity<UserResponseDTO> activateUser(@PathVariable Long userId) {
        UserResponseDTO user = userService.activateUser(userId);
        return ResponseEntity.ok(user);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long userId) {
        userService.deleteUser(userId);
        return ResponseEntity.noContent().build();
    }

}
