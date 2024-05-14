FROM openjdk:17-jdk-slim as builder

WORKDIR /build

COPY . .

RUN ./mvnw package -DskipTests

RUN mv target/*.jar app.jar

FROM openjdk:17-jre-slim

ENV TZ=America/Fortaleza

RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

VOLUME /tmp

WORKDIR /app

COPY --from=builder /build/app.jar /app/app.jar

CMD ["java", "-jar", "app.jar"]

