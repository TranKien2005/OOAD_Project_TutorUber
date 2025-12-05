package com.library.backend.dtos.responses;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserResponse {

    Long id;

    String username;

    String fullName;

    String email;

    String phone;

    String role;

    Boolean active;

    LocalDateTime createdAt;
}
