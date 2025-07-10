package com.gca.dto.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdateRequest {
    @NotNull(message = "ID cannot be null")
    private Long id;

    @NotBlank(message = "First name cannot be blank")
    @Size(min = 1, max = 50, message = "First name must be 1-50 characters")
    private String firstName;

    @NotBlank(message = "Last name cannot be blank")
    @Size(min = 1, max = 50, message = "Last name must be 1-50 characters")
    private String lastName;

    @NotBlank(message = "Username cannot be blank")
    @Size(min = 1, max = 50, message = "Username must be 1-50 characters")
    private String username;

    @NotBlank(message = "Password cannot be blank")
    @Size(min = 1, max = 255, message = "Password must be 1-255 characters")
    private String password;

    @NotNull(message = "Active status cannot be null")
    private Boolean isActive;
}
