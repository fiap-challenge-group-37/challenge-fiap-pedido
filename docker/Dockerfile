FROM maven:3.9.6-eclipse-temurin-21 AS builder
WORKDIR /app
COPY pom.xml .
COPY src ./src

RUN apt-get update && apt-get install -y dos2unix && \
    find src/main/resources -type f -print0 | xargs -0 dos2unix && \
    iconv -f UTF-8 -t US-ASCII -c -o src/main/resources/application.properties.clean src/main/resources/application.properties && \
    mv src/main/resources/application.properties.clean src/main/resources/application.properties

RUN mvn clean package -DskipTests

FROM maven:3.9.6-eclipse-temurin-21 AS dev
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn dependency:go-offline
CMD ["mvn", "spring-boot:run"]

FROM eclipse-temurin:21-jre AS prod
WORKDIR /app
COPY --from=builder /app/target/*.jar app.jar
EXPOSE 8080
CMD ["java", "-jar", "app.jar"]
