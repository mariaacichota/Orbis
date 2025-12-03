# Event Service

Microserviço responsável pelo gerenciamento de eventos.

## Porta
- **8081**

## Funcionalidades
- Criar, atualizar, listar e deletar eventos
- Buscar eventos por organizador
- Buscar eventos por categoria e tag
- Buscar eventos com filtros dinâmicos (título, localização, data)
- Gerenciar speakers, activities, categories e tags

## Endpoints principais
- `POST /events` - Criar evento
- `GET /events` - Listar eventos
- `GET /events/{eventId}` - Buscar evento por ID
- `PUT /events/{eventId}` - Atualizar evento
- `DELETE /events/{eventId}` - Deletar evento
- `GET /events/organizer/{organizerId}` - Listar eventos por organizador
- `GET /events/search` - Buscar eventos por categoria e tag
- `GET /events/search-dynamic` - Buscar eventos com filtros
- `GET /events/{eventId}/capacity` - Verificar capacidade do evento

## Executar
```bash
./mvnw spring-boot:run
```

