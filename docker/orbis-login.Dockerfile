FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app
COPY orbis-login/target/*.jar app.jar
EXPOSE 8083
ENTRYPOINT ["java", "-jar", "app.jar"]