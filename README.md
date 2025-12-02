# Uber-like Backend — Documentação da API

## 1. Visão Geral
Backend desenvolvido em **Spring Boot** para um serviço de transporte similar ao Uber.
A aplicação oferece autenticação JWT, controle de acesso por roles, gerenciamento de usuários, motoristas, veículos, corridas e pagamentos, além de validação e tratamento global de erros.

## 2. Tecnologias Principais
- Java 22
- Spring Boot 3.5.x
- Spring Web / Data JPA / Security / Validation
- JWT (jjwt)
- Flyway
- PostgreSQL
- springdoc-openapi (Swagger)

## 3. Entidades
### User
- Informações básicas, autenticação e roles.

### Driver
- Dados do motorista e localização.

### Vehicle
- Veículo associado ao motorista.

### Ride
- Corridas: origem, destino, status, motorista, passageiro e pagamento.

### Payment
- Cartões de crédito/métodos de pagamento.

## 4. Autenticação
Fluxo:
1. Registro `/api/auth/register`
2. Login `/api/auth/login`
3. Usar `Authorization: Bearer {token}`

## 5. Endpoints Principais
### Públicos
- `GET /api/public/health`
- `GET /api/public/info`

### Autenticação
- `POST /api/auth/register`
- `POST /api/auth/login`
- `POST /api/auth/driver/register/{userId}`

### Usuários
- `GET /api/users`
- `GET /api/users/{id}`
- `PUT /api/users/{id}/enable`

### Motoristas
- `GET /api/drivers`
- `PUT /api/drivers/{id}/status`
- `PUT /api/drivers/{id}/location`
- `GET /api/drivers/online`

### Corridas
- `POST /api/rides/request/{passengerId}`
- `PUT /api/rides/{rideId}/accept/{driverId}`
- `PUT /api/rides/{rideId}/status`
- `GET /api/rides/available`
- `GET /api/rides/history/{userId}`
- `GET /api/rides/nearby-drivers`

### Pagamentos
- `POST /api/payment/{userId}`
- `PATCH /api/payment/cancel/{userId}/{creditCardId}`
- `GET /api/payment/get-all/{userId}`

## 6. DTOs
Inclui UserRegistrationDto, LoginDto, DriverRegistrationDto, RideRequestDto e CreditCardDto.

## 7. Tratamento de Erros
Códigos utilizados:
- 400, 401, 403, 404, 500

Erro padrão inclui: status, mensagem, path, timestamp e details.

## 8. Banco de Dados
Flyway habilitado por padrão.
Exemplo para PostgreSQL:

```
spring.datasource.url=jdbc:postgresql://localhost:5432/uberdb
spring.datasource.username=usuario
spring.datasource.password=senha
spring.jpa.hibernate.ddl-auto=validate
spring.flyway.enabled=true
spring.flyway.locations=classpath:db/migration
```

## 9. Execução
```
mvn spring-boot:run
```

Swagger UI:
```
http://localhost:8080/swagger-ui/index.html
```
