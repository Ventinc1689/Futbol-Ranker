package com.ventinc.futbol_ranker.DTO;

import com.ventinc.futbol_ranker.model.Users;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public static UserResponseDTO toUserResponseDTO(Users user){
        UserResponseDTO dto = new UserResponseDTO();
        dto.setUserId(user.getUserId());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setRole(user.getRole());
        dto.setFullName(user.getFullName());
        dto.setCreatedDate(user.getCreatedDate());
        dto.setLastModifiedDate(user.getLastModifiedDate());
        dto.setActive(user.isActive());
        return dto;
    }

    public static Users fromRegistrationDTO(UserRegistrationDTO dto) {
        Users user = new Users();
        user.setUsername(dto.getUsername());
        user.setPassword(dto.getPassword());
        user.setEmail(dto.getEmail());
        user.setFullName(dto.getFullName());
        user.setRole(dto.getRole());
        return user;
    }

    public static void updateUserFromDTO(Users user, UserUpdateDTO dto) {
        user.setUsername(dto.getUsername());
        if (dto.getPassword() != null && !dto.getPassword().isEmpty()) {
            user.setPassword(dto.getPassword());
        }
        user.setEmail(dto.getEmail());
        user.setFullName(dto.getFullName());
        user.setRole(dto.getRole());
    }
}
