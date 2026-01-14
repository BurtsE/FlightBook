# Сервис для сбронирования отелей

## Запуск
```
docker-compose up --build
```
Приложение будет доступно по адресу http://localhost:8080/

## Библиотеки

 - Java 17+.
 - Spring Boot 3.5.x.
 - Spring Cloud (релиз-трейн, совместимый со Spring Boot по BOM).
 - Spring Data JPA + H2 (in-memory).
 - Spring Security + JWT (реализация на усмотрение, совместимая со Spring Security).
 - Spring Cloud Eureka (Service Discovery).
 - Spring Cloud Gateway (API Gateway).
 - Lombok, MapStruct (для DTO и маппинга).

## Cтруктура системы
 - API Gateway (Spring Cloud Gateway) — шлюз, осуществляющий маршрутизацию запросов, передачу токена для проверки в сервисах.
 - Hotel Management Service — микросервис для управления отелями и номерами (CRUD), агрегации по загруженности.
 - Booking Service — микросервис для создания бронирований и управления ими, интеграции с Hotel Service.
 - Eureka Server — реализация Service Registry (реестр сервисов). Его основная задача — обеспечивать динамическое обнаружение сервисов (service discovery) в микросервисной архитектуре.
 - User Service — микросервис для управления пользователями (CRUD), проверки JWT.

## Структура API

 - DELETE /api/v1/user — удалить пользователя (ADMIN).
 - POST /api/v1/user — создать пользователя (ADMIN).
 - PATCH /api/v1/user — обновить данные пользователя (ADMIN).
 - POST /api/v1/booking — создать бронирование (с выбором или автоподбором комнаты) (USER). В теле запроса параметр autoSelect: true/false (при true поле roomId игнорируется).
 - GET /api/v1/bookings — история бронирований пользователя (USER).
 - POST /api/v1/user/register — зарегистрировать пользователя, сгенерировав токен (USER).
 - POST /api/v1/user/auth — авторизовать пользователя, сгенерировав токен (USER).
 - GET /api/v1/booking/{id} — получить бронирование по id (USER).
 - DELETE /api/v1/booking/{id} — отменить бронирование (USER).
 - POST /api/hotels — добавить отель (ADMIN).
 - POST /api/v1/rooms — добавить номер в отель (ADMIN).
 - GET /api/v1/hotels — получить список отелей (USER).
 - GET /api/v1/rooms/recommend — получить список рекомендованных номеров (USER) (те же свободные номера, отсортированные по возрастанию times_booked).
 - GET /api/v1/rooms — получить список всех свободных номеров (USER) (без специальной сортировки).
 - POST /api/v1/rooms/{id}/confirm-availability — подтвердить доступность номера на запрошенные даты (временная блокировка слота на указанный период, используется в шаге согласованности) (INTERNAL).
 - POST /api/v1/rooms/{id}/release — компенсирующее действие: снять временную блокировку слота (INTERNAL). Маршрут не публикуется через Gateway.
