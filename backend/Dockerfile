FROM maven:3.8.4-openjdk-17 AS builder

WORKDIR /app

COPY ./pom.xml .

RUN mvn dependency:go-offline

COPY . .

RUN mvn package -DskipTests

FROM openjdk:17-jdk-alpine

RUN adduser -D myuser
USER myuser

WORKDIR /app

COPY --from=builder /app/target/backend-0.0.1-SNAPSHOT.jar backend.jar

ENTRYPOINT ["java", "-jar", "backend.jar"]
