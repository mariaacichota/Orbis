# --- Estágio 1: Construção (Builder) ---
FROM maven:3.9.6-eclipse-temurin-21 AS builder
WORKDIR /app

# Copia todo o projeto para dentro do Docker
COPY . .

# Entra na pasta do serviço específico e compila
WORKDIR /app/event-service
RUN mvn clean package -DskipTests

# --- Estágio 2: Execução (Runner) ---
FROM eclipse-temurin:21-jdk-alpine
WORKDIR /app

# Pega o .jar que foi gerado no estágio anterior
# Note o caminho: /app/event-service/target/...
COPY --from=builder /app/event-service/target/*.jar app.jar

EXPOSE 8081
ENTRYPOINT ["java", "-jar", "app.jar"]
