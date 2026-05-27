# minhasfinancas-api

API REST em **Spring Boot 3.2.2** (Java **17**) para gerenciar **usuários** e **lançamentos financeiros** (despesas/receitas), incluindo cálculo de **saldo**.

## Tecnologias

- Java 17
- Spring Boot 3.2.2
- Spring Web (REST)
- Spring Data JPA
- PostgreSQL (configurado em `application.properties`)
- H2 (dependência para ambiente de testes/dev)

## Como rodar (local)

1. Rode a aplicação com Maven Wrapper:
   ```bash
   cd ./minhasfinancas-api
   ./mvnw spring-boot:run
   ```
2. Garanta um PostgreSQL rodando em `localhost:5432` com o banco `minhasfinancas`.

Configuração atual em `src/main/resources/application.properties`:
- `spring.datasource.url=jdbc:postgresql://localhost:5432/minhasfinancas`
- `spring.datasource.username=postgres`
- `spring.datasource.password=testesql`

## Endpoints

### Autenticação de usuário

Base: `/api/usuarios`

- `POST /api/usuarios/autenticar`
  - Request body: `UsuarioDTO`
  - Resposta: usuário autenticado

### Usuários

- `POST /api/usuarios`
  - Cria um usuário com `UsuarioDTO`.

- `GET /api/usuarios/{id}/saldo`
  - Retorna o **saldo** do usuário (`BigDecimal`).

### Lançamentos financeiros

Base: `/api/lancamentos`

- `GET /api/lancamentos?descricao={descricao}&mes={mes}&ano={ano}&usuario={idUsuario}`
  - Filtra lançamentos e exige `usuario`.

- `POST /api/lancamentos`
  - Request body: `LancamentoDTO`
  - Cria lançamento.

- `PUT /api/lancamentos/{id}`
  - Atualiza campos do lançamento com `LancamentoDTO`.

- `PUT /api/lancamentos/{id}/atualizar-status`
  - Request body: `AtualizaStatusDTO` (ex.: `status` como string para `StatusLancamento.valueOf`).

- `DELETE /api/lancamentos/{id}`
  - Remove o lançamento.

## Estrutura (principais classes)

- `src/main/java/com/alinembs/minhasfinancas/api/resource/UsuarioResource.java`
- `src/main/java/com/alinembs/minhasfinancas/api/resource/LancamentoResource.java`
- `src/main/java/com/alinembs/minhasfinancas/service/UsuarioService.java` + impl
- `src/main/java/com/alinembs/minhasfinancas/service/LancamentoService.java` + impl
- `src/main/java/com/alinembs/minhasfinancas/model/entity/*`
- `src/main/java/com/alinembs/minhasfinancas/model/enums/*`

## Testes

- `./mvnw test`

## Observações

- O endpoint de autenticação devolve **bad request** quando ocorre `ErroAutenticacao`.
- O saldo é calculado via `lancamentoService.obterSaldoPorUsuario(id)`.
