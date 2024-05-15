package ru.arman.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class AuthenticationRequest {
    @NotBlank(message = "Username can't be empty")
    private String username;
    @Size(min = 4, message = "Password must be at least 4 in length")
    private String password;
}
