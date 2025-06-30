package org.example.testingproject.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PersonDto {
    @NotBlank(message = "Заполните поле")
    @Size(max = 30, message = "Имя не больше 30 символов")
    private String username;

    @NotBlank(message = "Заполните поле")
    private String password;
}
