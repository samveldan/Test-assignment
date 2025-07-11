package org.example.testingproject.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;

@OpenAPIDefinition(
        info = @Info(
                title = "Тестовое Задание",
                description = "Это REST-приложение на Spring Boot с аутентификацией через JWT. \n" +
                        "Проект разворачивается через Docker Compose и предоставляет защищённые\n" +
                        "эндпоинты, доступ к которым возможен только после получения JWT токенов. \n" +
                        "Схема базы данных и начальные данные управляются через Liquibase.",
                version = "1.0",
                contact = @Contact(
                        name = "Самвел Даниелян",
                        email = "samvelbala@bk.ru"
                )
        )
)
@SecurityScheme(
        type = SecuritySchemeType.HTTP,
        name = "bearerAuth",
        scheme = "bearer",
        bearerFormat = "JWT",
        description = """
        Чтобы получить JWT токен, сделайте POST-запрос на /auth/login с телом:
        
        {
          "username": "test",
          "password": "test"
        }
        
        В ответе будет два токена: access и refresh. 
        Вставьте любой в поле ниже.
        """
)
public class OpenApiConfig {
}
