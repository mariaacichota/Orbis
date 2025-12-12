FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app
COPY event-service/target/*.jar app.jar
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "app.jar"]