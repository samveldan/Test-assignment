package org.example.testingproject.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class AuthorDto {
    @NotBlank(message = "Заполните поле")
    @Size(max = 50, message = "Имя не больше 50 символов")
    private String name;

    private int birthYear;
    private List<String> bookTitles;
}
