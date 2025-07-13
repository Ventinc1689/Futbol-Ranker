package com.ventinc.futbol_ranker.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDTO {

    private Long userId;
    private String username;
    private String email;
    private String role;
    private String fullName;
    private LocalDateTime createdDate;
    private LocalDateTime lastModifiedDate;
    private boolean isActive;

}
