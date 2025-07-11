# Тестовое задание на позицию "Java Backend Стажёр"


### Описание проекта
Это REST-приложение на Spring Boot с аутентификацией через JWT. 
Проект разворачивается через Docker Compose и предоставляет защищённые
эндпоинты, доступ к которым возможен только после получения JWT токенов. 
Схема базы данных и начальные данные управляются через Liquibase.

---

### Используемые технологии
- Java 17
- Spring Boot
- Spring Security (JWT)
- Liquibase (управление миграциями БД)
- PostgreSQL
- Docker + Docker Compose
- Testcontainers (интеграционные тесты с поднятием базы в контейнере)
- JUnit 5 (unit и интеграционные тесты)
- Maven
- Swagger / OpenAPI (автогенерация и тестирование REST API)

---

### Как запустить проект

#### 1. Склонируйте репозиторий

```githubexpressionlanguage
git clone https://github.com/samveldan/Test-assignment.git
cd Test-assignment
```

#### 2. Запустите приложение через Docker Compose
```githubexpressionlanguage
docker-compose up -d
```
Будет запущен контейнер с PostgreSQL.
Таблицы и начальные данные будут созданы автоматически при запуске Spring Boot приложения, с помощью Liquibase.

База данных доступна на порту 5430 с такими параметрами:

- Пользователь: postgres
- Пароль: qwerty123
- База: testing-db

#### 3. Запустите проект в IntelliJ IDEA
- Приложение будет доступно по адресу http://localhost:8080.

---

### Получение JWT токенов

#### 1. Авторизация пользователя
Перед тем, как обращаться к основным эндпоинтам, нужно войти в приложение 
и получить 2 токена (access и refresh). Пользователей несколько, но можно 
войти с тестовым логином и паролем.
```http request
POST http://localhost:8080/auth/login
Content-Type: application/json

{
    "username": "test",
    "password": "test"
}
```
#### 2. В ответе получите JWT токены
```json
{
    "accessToken": "ваш_access_токен",
    "refreshToken" : "ваш_refresh_токен"
}
```
Теперь при обращении к защищённым эндпоинтам нужно добавлять заголовок 
`Authorization: Bearer ваш_access_токен`. <br>

#### 3. Обновить accessToken
Спустя 30 минут `accessToken` перестаёт быть валидным. Чтобы его обновить
нужно выполнить новый запрос используя `refreshToken`.
```http request
POST http://localhost:8080/auth/refresh
Content-Type: application/json

{
    "refreshToken": "ваш_refresh_токен"
}
```

#### 4. В ответе получите новый access JWT токен
```json
{
    "accessToken": "ваш_новый_access_токен",
    "refreshToken" : "ваш_старый_refresh_токен"
}
```

---

### Доступ к защищённым эндпоинтам
Для обращения к защищённым эндпоинтам добавьте в заголовки запроса:
`Authorization: Bearer ваш_токен`.

#### Пример GET-запроса
```http request
GET http://localhost:8080/books
Authorization: Bearer ваш_access_токен
```

#### В ответе получите список книг
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

### Документация Swagger

В проекте подключена автогенерация документации с помощью **Swagger/OpenAPI**.

После запуска приложения документация доступна по адресу:

- **Swagger UI**: [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)
- **OpenAPI JSON**: [http://localhost:8080/v3/api-docs](http://localhost:8080/v3/api-docs)

Вы можете протестировать все эндпоинты прямо из браузера: ввести параметры, заголовки и отправить запрос через кнопку `Try it out`.

Чтобы протестировать защищённые эндпоинты, нажмите кнопку `Authorize` и вставьте JWT токен.

---

### Основные эндпоинты

#### Аутентификация
- `POST /auth/login` - Авторизация и получение JWT токенов
- `POST /auth/refresh` - Обновление accessToken

#### Авторы
- `GET /authors` - Получение списка авторов
- `GET /authors/{id}` - Получение автора
- `POST /authors` - Создание автора

#### Книги
- `GET /books` - Получение списка книг
- `GET /books/{id}` - Получение книги
- `PUT /books/{id}` - Редактирование книги
- `POST /books` - Создание книги
- `DELETE /books/{id}` - Удаление книги

---

### Тестирование
В проекте реализованы unit-тесты и интеграционные тесты с использованием <br>
`JUnit 5` и `Testcontainers`.

### Как запустить тесты

В IntelliJ IDEA:
- Откройте проект.
- Кликните правой кнопкой по папке `src/test/java`.
- Выберите **Run 'Tests' in 'Java'**.
