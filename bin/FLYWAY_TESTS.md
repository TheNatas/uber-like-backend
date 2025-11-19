# 🧪 Testes para Verificar o Flyway

## 🎯 O que o Flyway faz?

O Flyway aplicou **2 migrações**:
- **V1**: Criou as tabelas (users, drivers, vehicles, rides)
- **V2**: Inseriu dados iniciais

---

## ✅ TESTE 1: Ver nos Logs

Quando a aplicação inicia, você vê:
```
Migrating schema "PUBLIC" to version "1 - create initial schema"
Migrating schema "PUBLIC" to version "2 - insert initial data"
Successfully applied 2 migrations to schema "PUBLIC", now at version v2
```

---

## ✅ TESTE 2: Console H2 (Visual)

1. Acesse: `http://localhost:8080/h2-console`
2. Use as credenciais:
   - **JDBC URL**: `jdbc:h2:mem:uberdb`
   - **User**: `sa`
   - **Password**: `password`

3. Execute queries SQL:

```sql
-- Ver histórico de migrações do Flyway
SELECT * FROM flyway_schema_history;

-- Ver usuários inseridos pelo Flyway
SELECT * FROM users;

-- Ver motoristas
SELECT * FROM drivers;

-- Ver veículos
SELECT * FROM vehicles;
```

**Resultado esperado:**
- 5 usuários (1 admin, 2 passageiros, 2 motoristas)
- 2 motoristas com perfis completos
- 2 veículos cadastrados

---

## ✅ TESTE 3: Testar API com Dados do Flyway

### Teste de Login (PowerShell)

```powershell
# Login com admin (dados inseridos pelo Flyway V2)
curl http://localhost:8080/api/auth/login -H "Content-Type: application/json" -d '{\"email\":\"admin@uber.com\",\"password\":\"admin123\"}'

# Login com passageiro
curl http://localhost:8080/api/auth/login -H "Content-Type: application/json" -d '{\"email\":\"john.doe@email.com\",\"password\":\"password123\"}'

# Login com motorista
curl http://localhost:8080/api/auth/login -H "Content-Type: application/json" -d '{\"email\":\"mike.johnson@email.com\",\"password\":\"password123\"}'
```

**Resultado esperado:** Você deve receber um token JWT para cada login bem-sucedido! 🎉

---

## 🎓 TESTE 4: Criar Nova Migração (Teste de Versionamento)

Para testar se o Flyway realmente gerencia versões, crie uma nova migração:

### Crie o arquivo: `V3__add_rating_to_rides.sql`
```sql
-- Add rating column to rides table
ALTER TABLE rides ADD COLUMN passenger_rating INT;
ALTER TABLE rides ADD COLUMN driver_rating INT;
```

### Reinicie a aplicação

Você verá nos logs:
```
Migrating schema "PUBLIC" to version "3 - add rating to rides"
Successfully applied 3 migrations to schema "PUBLIC", now at version v3
```

✨ **Isso prova que o Flyway:**
- Detecta novas migrações automaticamente
- Aplica apenas as migrações pendentes
- Mantém o histórico versionado

---

## 🚀 Benefícios Confirmados

✅ **Versionamento**: Cada mudança no BD é rastreada (V1, V2, V3...)  
✅ **Automático**: Migrações aplicadas ao iniciar a aplicação  
✅ **Seguro**: Não perde dados, apenas adiciona/modifica  
✅ **Time**: Toda equipe usa o mesmo schema do BD  
✅ **Produção**: Fácil deployment em diferentes ambientes  

---

## 📝 Usuários de Teste (Inseridos pelo Flyway)

| Email | Senha | Role |
|-------|-------|------|
| admin@uber.com | admin123 | ADMIN |
| john.doe@email.com | password123 | PASSENGER |
| jane.smith@email.com | password123 | PASSENGER |
| mike.johnson@email.com | password123 | DRIVER |
| sarah.wilson@email.com | password123 | DRIVER |

Todos esses usuários foram criados pelo Flyway através da migração V2! 🎯
