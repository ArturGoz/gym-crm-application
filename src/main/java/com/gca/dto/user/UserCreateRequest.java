package com.gca.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class UserCreateRequest {
    private String firstName;
    private String lastName;
    private String username;
    private String password;
}
