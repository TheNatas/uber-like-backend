# Uber-like Backend Application

A comprehensive Spring Boot backend application for ride-sharing services with JWT authentication, role-based access control, and comprehensive error handling.

## Quick Start

1. **Clone and run:**
   # Uber-like Backend

   README com instruções essenciais para rodar e testar o backend localmente.

   **Resumo:** backend Spring Boot para um serviço tipo Uber com autenticação JWT, roles (ADMIN, DRIVER, PASSENGER), Flyway para migrações e endpoints para gerenciar usuários, motoristas, veículos, corridas e pagamentos.

   **Importante:** configure um banco relacional e as credenciais em `src/main/resources/application.properties` antes de executar a aplicação (o projeto usa Flyway para migrações). Veja `API_DOCUMENTATION.md` para detalhes da API.

   ## 1) Instruções para rodar o projeto (Windows / PowerShell)

   1. Build e execução com o wrapper do Maven:

   ```powershell
   .\mvnw.cmd clean package
   .\mvnw.cmd spring-boot:run
   ```

   2. Ou rodar diretamente com Maven (se instalado):

   ```powershell
   mvn clean package
   mvn spring-boot:run
   ```

   3. Verificar health:

   ```powershell
   curl -X GET http://localhost:8080/api/public/health
   ```

   Observação: Flyway executa migrações automaticamente na inicialização; garanta que o banco configurado esteja acessível.

   ## 2) Estrutura de pastas (resumo)

   Raiz do projeto (principais diretórios):

   ```
   /src
      /main
         /java/com/example/demo
            /config           # Configurações e inicializadores
            /controller       # Endpoints REST
            /dto              # Objetos de requisição/resposta
            /entity           # Entidades JPA
            /exception        # Handler de erros personalizados
            /migration        # Migrações Java (Flyway)
            /repository       # Repositórios Spring Data JPA
            /security         # JWT e configuração de segurança
            /service          # Lógica de negócio
         /resources
            /db/migration     # Migrações SQL do Flyway
            application.properties

   bin/                  # cópias e utilitários (opcional)
   README.md
   API_DOCUMENTATION.md
   ```

   ## 3) Exemplo de requests e responses

   1) Login (obter JWT)

   Request:

   ```bash
   curl -X POST http://localhost:8080/api/auth/login \
      -H "Content-Type: application/json" \
      -d '{"email":"admin@uber.com","password":"admin123"}'
   ```

   Success response (exemplo):

   ```json
   {
      "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6...",
      "user": {
         "id": 1,
         "email": "admin@uber.com",
         "role": "ADMIN"
      }
   }
   ```

   2) Registrar usuário

   Request:

   ```bash
   curl -X POST http://localhost:8080/api/auth/register \
      -H "Content-Type: application/json" \
      -d '{"firstName":"John","lastName":"Doe","email":"john.doe@email.com","phoneNumber":"+1234567891","password":"password123"}'
   ```

   Response (exemplo):

   ```json
   {
      "message": "User registered successfully",
      "userId": 2
   }
   ```

   3) Solicitar corrida (exemplo)

   Request (use token obtido no login):

   ```bash
   curl -X POST http://localhost:8080/api/rides/request/2 \
      -H "Content-Type: application/json" \
      -H "Authorization: Bearer {jwt-token}" \
      -d '{"pickupLatitude":40.7128,"pickupLongitude":-74.0060,"destinationLatitude":40.7580,"destinationLongitude":-73.9855,"paymentMethod":"CARD"}'
   ```

   Response (exemplo):

   ```json
   {
      "id": 10,
      "passengerId": 2,
      "status": "REQUESTED",
      "pickupLatitude": 40.7128,
      "pickupLongitude": -74.0060
   }
   ```

   4) Motorista aceita corrida

   ```bash
   curl -X PUT http://localhost:8080/api/rides/10/accept/1 \
      -H "Authorization: Bearer {driver-jwt-token}"
   ```

   Response (exemplo):

   ```json
   {
      "id": 10,
      "driverId": 1,
      "status": "ACCEPTED",
      "acceptedAt": "2025-11-19T12:34:56"
   }
   ```

   5) Inserir cartão de pagamento

   ```bash
   curl -X POST http://localhost:8080/api/payment/2 \
      -H "Content-Type: application/json" \
      -H "Authorization: Bearer {jwt-token}" \
      -d '{"description":"Cartão John","number":"4111111111111111","code":"123","expirationDate":"2026-12"}'
   ```

   Response (exemplo):

   ```json
   {
      "id": 1,
      "passengerId": 2,
      "status": "ACTIVE",
      "description": "Cartão John"
   }
   ```

   Observação: campos e formatos seguem os DTOs em `src/main/java/com/example/demo/dto`.

   ## 4) Credenciais (dados de exemplo pré-carregados)

   Use as contas abaixo para testes locais (senhas inseridas/hasheadas pelas migrações Java):

   ```
   Admin:    admin@uber.com / admin123
   Passenger: john.doe@email.com / password123
   Passenger: jane.smith@email.com / password123
   Driver:   mike.johnson@email.com / password123
   Driver:   sarah.wilson@email.com / password123
   ```
   
