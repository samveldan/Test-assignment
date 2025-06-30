package org.example.testingproject.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class JwtRefreshDto {
    @NotBlank(message = "Заполните поле")
    private String refreshToken;
}
