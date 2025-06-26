# Orbis - Sistema de Gestão de Eventos

Orbis é uma plataforma moderna de **gestão de eventos**, desenvolvida com **Spring Boot no backend** e **React no frontend**. Seu objetivo é facilitar o planejamento, organização, publicação e gerenciamento de eventos como conferências, workshops, palestras e shows.

---

## 🧭 Funcionalidades Principais

- Registro e autenticação de usuários com **JWT**
- Criação e listagem de **eventos**
- **Compra de ingressos** com diferentes tipos (ex: inteira, meia, VIP)
- Organização de eventos por **categorias**, **tags**, **palestrantes** e **atividades**
- Controle de permissões por **roles de usuário**
- API REST com tratamento de exceções e validações personalizadas

---

## 🛠️ Tecnologias Utilizadas

### Backend (Java)

- **Spring Boot**
- **Spring Security** com **JWT**
- **Spring Data JPA**
- **Hibernate Validator**
- **Maven**
- **H2** para ambiente de testes
- **PostgreSQL** para ambiente de produção
- **Lombok**

### Frontend (React)

- **React.js**
- **fetch API** para requisições HTTP
- **React Router DOM**
- **CSS Modules / estilo customizado**

### DevOps

- **Docker** e **Docker Compose**
- **Kubernetes (k8s)** com `kubectl`

---

## 📦 Estrutura do Projeto

```bash
Orbis/
├── backend/        # Spring Boot API
├── frontend/       # React.js SPA
├── docker/         # Dockerfile + docker-compose
├── k8s/            # Arquivos de manifesto Kubernetes
├── sql/            # Scripts SQL de criação de tabelas
├── documentation/  # Diagramas e notebooks técnicos
```

---

## 🔐 Segurança com JWT

- Login gera um token JWT
- Requisições protegidas exigem header Authorization: `Bearer <token>`
- Autenticação configurada em `SecurityConfig`
- Filtro `JwtAuthenticationFilter` inspeciona cada requisição
- `JwtTokenProvider` cuida da criação e validação do token

---

## 👤 Entidades Principais

- `User`: dados de login e permissões
- `Event`: contém título, descrição, data, local
- `Ticket`: vinculado a um `User` e `Event`
- `Speaker`, `Activity`: usados na programação do evento
- `Category`, `Tag`, `TicketType`: organizadores e variantes do evento

---

## 🔄 Fluxo da Aplicação

1. Usuário se registra (`/auth/register`)
2. Faz login (`/auth/login`) e recebe JWT
3. Visualiza eventos (`/events`)
4. Compra ingressos (`/tickets`)
5. Visualiza histórico de ingressos (`/user/tickets`)

---

## 💻 Integração Frontend ↔ Backend

- Todas as chamadas são feitas via `fetch()`
- Endpoints: `/auth/login`, `/auth/register`, `/events`, `/users`, `/tickets`
- Integração direta com `http://localhost:8080`
- O JWT é incluído manualmente nos headers nas requisições protegidas

---

## 🐳 Docker

```bash
cd docker
docker-compose up --build
```

- Backend será executado em `http://localhost:8080`
- Banco PostgreSQL será inicializado via container

---

## ☸️ Kubernetes

```bash
kubectl apply -f k8s/postgres.yaml
kubectl apply -f k8s/backend.yaml
```

- Cria serviços, deployments e volumes persistentes para backend e banco

---

## 🗄️ SQL

- Script de criação de tabelas em: `sql/scripts/0001_Create Table.sql`
- Útil para testes manuais ou ambientes fora do JPA

---

## 📚 Documentação

- Diagramas UML em `.png` e `.pdf`
- Notebook explicando o modelo: `documentation/scripts/script_uml.ipynb`

---

## 🧪 Testes

- Testes de unidade e integração para controllers, serviços, modelos e segurança
- Ferramentas: JUnit + Mockito
- Cobertura disponível em: `backend/htmlReport/index.html`
- Nenhum teste com Selenium ou frontend automatizado por enquanto

---

## 🚀 Como Executar Localmente

### Backend

```bash
cd backend
./mvnw spring-boot:run
```

### Frontend

```bash
cd frontend
npm install
npm start
```

---

## 📌 Exemplos de Endpoints

| Método | Endpoint                   | Autenticado? | Descrição                        |
|--------|----------------------------|--------------|----------------------------------|
| POST   | /auth/register             | ❌           | Registro de novo usuário         |
| POST   | /auth/login                | ❌           | Login e geração de JWT           |
| GET    | /events                    | ❌/✅        | Lista eventos                    |
| POST   | /events                    | ✅           | Cria novo evento                 |
| POST   | /tickets                   | ✅           | Compra ingresso                  |
| GET    | /user/tickets              | ✅           | Lista ingressos do usuário       |
| GET    | /users                     | ✅           | Lista usuários                   |

---

### 👥 Colaboradores  
<table>
  <tr>
    <td align="center"><a href="https://github.com/ameliagalvao"><img src="https://avatars.githubusercontent.com/ameliagalvao" width="80px;" alt="Amélia Galvão"/><br /><sub><b>Amélia Galvão</b></sub></a><br /><a href="#" title="Code">💻🛠️</a></td>
    <td align="center"><a href="https://github.com/ArielCAlves"><img src="https://avatars.githubusercontent.com/ArielCAlves" width="80px;" alt="Ariel Alves"/><br /><sub><b>Ariel Alves</b></sub></a><br /><a href="#" title="Code">💻🛠️</a></td>
    <td align="center"><a href="https://github.com/Abrahao02"><img src="https://avatars.githubusercontent.com/Abrahao02" width="80px;" alt="Eduardo Abrahao"/><br /><sub><b>Eduardo Abrahao</b></sub></a><br /><a href="#" title="Code">💻🛠️</a></td>
    <td align="center"><a href="https://github.com/Eduardo-Kolberg"><img src="https://avatars.githubusercontent.com/Eduardo-Kolberg" width="80px;" alt="Eduardo Kolberg"/><br /><sub><b>Eduardo Kolberg</b></sub></a><br /><a href="#" title="Code">💻🛠️</a></td>
    <td align="center"><a href="https://github.com/mariaacichota"><img src="https://avatars.githubusercontent.com/mariaacichota" width="80px;" alt="Maria Cichota"/><br /><sub><b>Maria Cichota</b></sub></a><br /><a href="#" title="Code">💻🛠️</a></td>
    <td align="center"><a href="https://github.com/Raquelsantos242"><img src="https://avatars.githubusercontent.com/Raquelsantos242" width="80px;" alt="Raquel Braga"/><br /><sub><b>Raquel Braga</b></sub></a><br /><a href="#" title="Code">💻🛠️</a></td>
    <td align="center"><a href="https://github.com/wendel315"><img src="https://avatars.githubusercontent.com/wendel315" width="80px;" alt="Wendel Marins"/><br /><sub><b>Wendel Marins</b></sub></a><br /><a href="#" title="Code">💻🛠️</a></td>
  </tr>
</table>

---
