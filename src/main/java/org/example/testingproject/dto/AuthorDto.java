package org.example.testingproject.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class AuthorDto {
    @NotBlank(message = "Заполните поле")
    @Size(max = 50, message = "Имя не больше 50 символов")
    private String name;

    @NotNull(message = "Заполните поле")
    @Min(message = "Укажите корректный год", value = 0)
    @Max(message = "Укажите корректный год", value = 2025)
    private int birthYear;
    private List<String> bookTitles;
}