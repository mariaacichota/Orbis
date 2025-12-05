# Ticket Service

Microserviço responsável pelo gerenciamento de ingressos.

## Porta
- **8082**

## Funcionalidades
- Vender ingressos para eventos
- Validar disponibilidade de ingressos
- Suporta tipos de ingressos: FULL, HALF, VIP

## Endpoints principais
- `POST /tickets` - Vender ingresso

## Dependências
- Comunica-se com o **Event Service** na porta 8081

## Persistência
- MongoDB

## Executar
```bash
./mvnw spring-boot:run
```

