# Builder stage
FROM maven:3.9.6-eclipse-temurin-21 AS builder
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Dev stage
FROM maven:3.9.6-eclipse-temurin-21 AS dev
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn dependency:go-offline
CMD ["mvn", "spring-boot:run"]

# Runtime (prod) stage
FROM eclipse-temurin:21-jre AS prod
WORKDIR /app
COPY --from=builder /app/target/*.jar app.jar
EXPOSE 8080
CMD ["java", "-jar", "app.jar"]