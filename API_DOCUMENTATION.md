# Uber-like Backend Application

## Overview
This is a comprehensive Spring Boot backend application for an Uber-like ride-sharing service. It includes 4 main entities with proper relationships, Spring Security with JWT authentication, and comprehensive error handling.

## Features
- **JWT Authentication & Authorization**
- **Role-based Access Control** (ADMIN, DRIVER, PASSENGER)
- **Global Exception Handling**
- **Input Validation**
- **H2 In-Memory Database** (easily configurable for production databases)
- **Comprehensive API Documentation**

## Entities

### 1. User
- Basic user information (name, email, phone, password)
- User roles: PASSENGER, DRIVER, ADMIN
# Documentação da API — Uber-like Backend

Esta documentação descreve a API atual do projeto, incluindo todos os endpoints expostos, exemplos de payloads (DTOs), fluxo de autenticação, informações sobre migrações e configuração de banco de dados. A aplicação usa Flyway para migrações e o banco deve ser configurado via `application.properties` (exemplos abaixo).

**Resumo das funcionalidades**
- Autenticação com JWT
- Controle de acesso por roles (ADMIN, DRIVER, PASSENGER)
- Gestão de usuários, motoristas, veículos e corridas
- Registro e gerenciamento de cartões de pagamento
- Localização de motoristas por proximidade
- Validação de entrada e tratamento global de erros

**Entidades principais**
- `User`: conta do usuário (nome, email, telefone, senha, roles, enabled)
- `Driver`: dados do motorista (licenseNumber, status, localização atual)
- `Vehicle`: veículo associado ao motorista (make, model, year, plate, type)
- `Ride`: pedido/execução da corrida (pickup/destination, status, fare, payment)
- `Payment`: dados de pagamento / cartão de crédito

## Endpoints

Base path: `/api`

**Publicos (sem autenticação)**
- GET `/api/public/health` — Retorna status da aplicação (status, timestamp, versão).
- GET `/api/public/info` — Informações gerais da aplicação (features e entidades).

**Autenticação (`/api/auth`)**
- POST `/api/auth/register` — Registrar novo usuário
  - Body (`UserRegistrationDto`):
    {
      "firstName": "John",
      "lastName": "Doe",
      "email": "john.doe@example.com",
      "phoneNumber": "+1234567890",
      "password": "password123",
      "role": "PASSENGER"
    }

- POST `/api/auth/login` — Login
  - Body (`LoginDto`):
    {
      "email": "john.doe@example.com",
      "password": "password123"
    }
  - Response: mapa com token JWT e dados básicos do usuário.

- POST `/api/auth/driver/register/{userId}` — Registrar usuário como motorista
  - Path param: `userId` (Long)
  - Body (`DriverRegistrationDto`):
    {
      "licenseNumber": "DL123456789",
      "vehicleMake": "Toyota",
      "vehicleModel": "Camry",
      "vehicleYear": 2022,
      "vehicleLicensePlate": "ABC123",
      "vehicleColor": "Silver",
      "vehicleType": "STANDARD"
    }

**Usuários (`/api/users`)**
- GET `/api/users` — Listar todos (ROLE_ADMIN)
- GET `/api/users/{id}` — Obter usuário por id
- PUT `/api/users/{id}/enable` — Alterna `enabled` do usuário (ROLE_ADMIN)

**Motoristas (`/api/drivers`)**
- GET `/api/drivers` — Listar todos (ROLE_ADMIN)
- GET `/api/drivers/{id}` — Obter motorista por id
- GET `/api/drivers/online` — Listar motoristas online (ROLE_ADMIN)
- PUT `/api/drivers/{id}/status?status={STATUS}` — Atualizar status (ROLE_DRIVER ou ADMIN)
- PUT `/api/drivers/{id}/location?latitude={lat}&longitude={lon}` — Atualizar localização (ROLE_DRIVER ou ADMIN)

**Corridas (`/api/rides`)**
- POST `/api/rides/request/{passengerId}` — Solicitar corrida (ROLE_PASSENGER ou ADMIN)
  - Body (`RideRequestDto`):
    {
      "pickupLatitude": 40.7128,
      "pickupLongitude": -74.0060,
      "pickupAddress": "Origem",
      "destinationLatitude": 40.7580,
      "destinationLongitude": -73.9855,
      "destinationAddress": "Destino",
      "paymentMethod": "CARD", // enum PaymentMethod
      "paymentId": 123 // opcional: id do cartão salvo
    }

- PUT `/api/rides/{rideId}/accept/{driverId}` — Motorista aceita corrida (ROLE_DRIVER)
- PUT `/api/rides/{rideId}/status?status={STATUS}` — Atualizar status da corrida (ROLE_DRIVER)
- GET `/api/rides/available` — Buscar corridas disponíveis (ROLE_DRIVER)
- GET `/api/rides/history/{userId}?userType={PASSENGER|DRIVER}` — Histórico de corridas
- GET `/api/rides/nearby-drivers?latitude={lat}&longitude={lon}&radiusKm={r}` — Motoristas próximos (ROLE_PASSENGER)

**Pagamento (`/api/payment`)**
- POST `/api/payment/{userId}` — Inserir cartão/criar pagamento (ROLE_PASSENGER)
  - Body (`CreditCardDto`):
    {
      "description": "Meu cartão",
      "number": "4111111111111111",
      "code": "123",
      "expirationDate": "2026-12" // YearMonth
    }

- PATCH `/api/payment/cancel/{userId}/{creditCardId}` — Cancelar cartão (ROLE_PASSENGER)
- GET `/api/payment/get-all/{userId}` — Listar cartões do usuário (ROLE_PASSENGER)

## DTOs principais
- `UserRegistrationDto` — `firstName`, `lastName`, `email`, `phoneNumber`, `password`, `role` (default PASSENGER)
- `LoginDto` — `email`, `password`
- `DriverRegistrationDto` — `licenseNumber`, `vehicleMake`, `vehicleModel`, `vehicleYear`, `vehicleLicensePlate`, `vehicleColor`, `vehicleType`
- `RideRequestDto` — `pickupLatitude`, `pickupLongitude`, `pickupAddress`, `destinationLatitude`, `destinationLongitude`, `destinationAddress`, `paymentMethod`, `paymentId`
- `CreditCardDto` — `description`, `number`, `code`, `expirationDate` (YearMonth)

## Fluxo de autenticação
1. Registrar usuário (`/api/auth/register`) ou usar conta existente.
2. Efetuar login (`/api/auth/login`) para receber token JWT.
3. Incluir cabeçalho `Authorization: Bearer {jwt-token}` nas requisições protegidas.

## Tratamento de erros
- 400 Bad Request — erros de validação e regras de negócio
- 401 Unauthorized — falha de autenticação
- 403 Forbidden — acesso não autorizado por role
- 404 Not Found — recurso não encontrado
- 500 Internal Server Error — erro inesperado

Resposta de erro padrão inclui: status, tipo, mensagem, path e timestamp.

## Migrations e Banco de Dados
- O projeto utiliza **Flyway** para migrações (localizações: `classpath:db/migration` e `classpath:com/example/demo/migration`).
- Observação: **Não usamos H2** no fluxo atual. Configure seu banco relacional (PostgreSQL, MySQL, etc.) via `application.properties` antes de rodar a aplicação.

Exemplo mínimo para PostgreSQL em `application.properties`:

```
spring.datasource.url=jdbc:postgresql://localhost:5432/uberdb
spring.datasource.username=seu_usuario
spring.datasource.password=sua_senha
spring.datasource.driverClassName=org.postgresql.Driver
spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect
spring.jpa.hibernate.ddl-auto=validate
spring.flyway.enabled=true
spring.flyway.locations=classpath:db/migration,classpath:com/example/demo/migration
```

Se preferir MySQL, adapte a `url`, `driverClassName` e `hibernate.dialect` adequadamente.

## Dados de amostra
- A aplicação carrega dados iniciais via migrations/initializer (ver `DataInitializer`), incluindo um usuário ADMIN e alguns usuários/ motoristas de exemplo.

## Como executar (local)
1. Ajuste `application.properties` com as credenciais do banco desejado.
2. Execute as migrations (Flyway roda automaticamente ao subir a aplicação).
3. Rodar com Maven:

```powershell
mvnw.cmd spring-boot:run
```

4. Teste o endpoint de saúde:

```powershell
curl -X GET http://localhost:8080/api/public/health
```

## Segurança
- Autenticação com JWT (segredo e tempo de expiração configuráveis via `application.properties`)
- Senhas criptografadas com BCrypt
- Verificações de roles via `@PreAuthorize`

## Observações finais / Próximos passos
- Se quiser, atualizo também o `application.properties` de exemplo no repositório para refletir PostgreSQL (ou outro banco) e removo as propriedades do H2 — me diga qual SGBD prefere.

---
Arquivo(s) de migração usados: `src/main/resources/db/migration/V1__create_initial_schema.sql`, `src/main/resources/db/migration/V3__create_payments_table.sql` e migrações Java em `src/main/java/com/example/demo/migration`.
