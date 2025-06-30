package org.example.testingproject;

import org.example.testingproject.dto.AuthorDto;
import org.example.testingproject.models.Author;
import org.example.testingproject.models.Book;
import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class TestingProjectApplication {
    public static void main(String[] args) {
        SpringApplication.run(TestingProjectApplication.class, args);
    }

    @Bean
    public ModelMapper mapper() {
        return new ModelMapper();
    }
}
