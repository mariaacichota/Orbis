# --- Estágio 1: Construção (Builder) ---
FROM maven:3.9.6-eclipse-temurin-17 AS builder
WORKDIR /app

# Copia todo o projeto para dentro do Docker
COPY . .

# Entra na pasta do Login e compila
WORKDIR /app/orbis-login
RUN mvn clean package -DskipTests

# --- Estágio 2: Execução (Runner) ---
FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app

# Pega o JAR gerado
COPY --from=builder /app/orbis-login/target/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
