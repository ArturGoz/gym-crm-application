package com.gca.dto;

import com.gca.security.ValidationPatterns;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PasswordChangeRequest {

    @NotNull(message = "User ID is required")
    private Long userId;

    @NotBlank(message = "Password is required")
    @Size(min = 8, max = 255, message = "Password must be between 8 and 255 characters")
    @Pattern(
            regexp = ValidationPatterns.PASSWORD_PATTERN,
            message = "Password must be at least 8 characters long, contain upper and lower case letters, a digit, and a special character"
    )
    private String password;
}
