# Тестовое задание на позицию "Java Backend Стажёр"

---

## Описание проекта
Это REST-приложение на Spring Boot с аутентификацией через JWT. <br>
Проект разворачивается через Docker Compose и предоставляет защищённые <br>
эндпоинты, доступ к которым возможен только после получения JWT токенов.

---

## Используемые технологии
- Java 17
- Spring Boot
- Spring Security (JWT)
- PostgreSQL
- Docker + Docker Compose
- Testcontainers (интеграционные тесты с поднятием базы в контейнере)
- JUnit 5 (unit и интеграционные тесты)
- Maven

---

## Как запустить проект

### 1. Склонируйте репозиторий

```githubexpressionlanguage
git clone https://github.com/samveldan/Test-assignment.git
cd Test-assignment
```

### 2. Запустите приложение через Docker Compose
```githubexpressionlanguage
docker-compose up -d
```
При старте контейнера автоматически создадутся и заполнятся таблицы в базе данных с помощью следующих скриптов:

- [authors_and_books.sql](./docker/authors_and_books.sql)
- [users_and_roles.sql](./docker/users_and_roles.sql)

База данных будет доступна на порту `5430`

### 3. Запустите проект в IntelliJ IDEA
- Приложение будет доступно по адресу http://localhost:8080.

---

## Получение JWT токенов

### 1. Авторизация пользователя
Перед тем, как обращаться к основным эндпоинтам, нужно войти в приложение <br>
и получить 2 токена (access и refresh). Пользователей несколько, но можно <br>
войти с тестовым логином и паролем.
```http request
POST http://localhost:8080/auth/login
Content-Type: application/json

{
    "username": "test",
    "password": "test"
}
```
### 2. В ответе получите JWT токены
```json
{
    "accessToken": "ваш_access_токен",
    "refreshToken" : "ваш_refresh_токен"
}
```
Теперь при обращении к защищённым эндпоинтам нужно добавлять заголовок <br>
`Authorization: Bearer ваш_access_токен`. <br>

### 3. Обновить accessToken
Спустя 30 минут `accessToken` перестаёт быть валидным. Чтобы его обновить <br>
нужно выполнить новый запрос используя `refreshToken`.
```http request
POST http://localhost:8080/auth/refresh
Content-Type: application/json

{
    "refreshToken": "ваш_refresh_токен"
}
```

### 4. В ответе получите новый access JWT токен
```json
{
    "accessToken": "ваш_новый_access_токен",
    "refreshToken" : "ваш_старый_refresh_токен"
}
```

---

## Доступ к защищённым эндпоинтам
Для обращения к защищённым эндпоинтам добавьте в заголовки запроса:
`Authorization: Bearer ваш_токен`.

### Пример GET-запроса
```http request
GET http://localhost:8080/books
Authorization: Bearer ваш_access_токен
```

### В ответе получите список книг
```json
[
  {
    "title": "Eugene Onegin",
    "year": 1833,
    "genre": "POETRY",
    "author": "Pushkin A.S."
  },
  {
    "title": "The Captain's Daughter",
    "year": 1836,
    "genre": "HISTORICAL_NOVEL",
    "author": "Pushkin A.S."
  },
  {
    "title": "A Hero of Our Time",
    "year": 1840,
    "genre": "NOVEL",
    "author": "Michail Y.L."
  },
  ...
]
```

---

## Основные эндпоинты

### Аутентификация
- `POST /auth/login` - Авторизация и получение JWT токенов
- `POST /auth/refresh` - Обновление accessToken

### Авторы
- `GET /authors` - Получение списка авторов
- `GET /authors/2` - Получение автора
- `POST /authors` - Создание автора

### Книги
- `GET /books` - Получение списка книг
- `GET /books/2` - Получение книги
- `PUT /books/2` - Редактирование книги
- `POST /books` - Создание книги
- `DELETE /books/2` - Удаление книги

---

## Тестирование
В проекте реализованы unit-тесты и интеграционные тесты с использованием <br>
`JUnit 5` и `Testcontainers`.

### Как запустить тесты

В IntelliJ IDEA:
- Откройте проект.
- Кликните правой кнопкой по папке `src/test/java`.
- Выберите **Run 'Tests' in 'Java'**.