FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app
COPY ticket-service/target/*.jar app.jar
EXPOSE 8082
ENTRYPOINT ["java", "-jar", "app.jar"]