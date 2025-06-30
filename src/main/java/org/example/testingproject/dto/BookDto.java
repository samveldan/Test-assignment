package org.example.testingproject.dto;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.example.testingproject.enums.Genre;

@Data
@AllArgsConstructor
public class BookDto {
    @NotBlank(message = "Заполните поле")
    @Size(max = 50, message = "Название не больше 50 символов")
    private String title;

    @NotNull(message = "Заполните поле")
    @Min(message = "Укажите корректный год", value = 0)
    @Max(message = "Укажите корректный год", value = 2025)
    private int year;

    @NotNull(message = "Заполните поле")
    private Genre genre;

    @NotBlank(message = "Заполните поле")
    private String author;
}
