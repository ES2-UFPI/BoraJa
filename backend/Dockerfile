# Use uma imagem base que suporte Maven e OpenJDK 17
FROM maven:3.8.4-openjdk-17 AS builder

# Defina o diretório de trabalho dentro do container
WORKDIR /app

# Copie o arquivo pom.xml para o container
COPY ./pom.xml .

# Execute mvn dependency:go-offline para baixar todas as dependências offline
RUN mvn dependency:go-offline

# Copie o restante do código fonte para o container
COPY . .

# Construa o projeto Maven, ignorando os testes
RUN mvn package -DskipTests

# Inicie uma nova fase de construção usando uma imagem base diferente
FROM openjdk:17-jdk-alpine

# Crie um novo usuário dentro do container
RUN adduser -D myuser
USER myuser

# Defina o diretório de trabalho dentro do container
WORKDIR /app

# Copie o JAR compilado do projeto para o container
COPY --from=builder /app/target/backend-0.0.1-SNAPSHOT.jar backend.jar

# Defina o ponto de entrada do container
ENTRYPOINT ["java", "-jar", "backend.jar"]