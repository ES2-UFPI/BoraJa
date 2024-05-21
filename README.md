# ![BoraJa](https://i.ibb.co/c1zpjX2/Whats-App-Image-2024-04-24-at-13-25-24.jpg)

# BoraJá!

BoraJá é uma aplicação mobile para compartilhamento de caronas remuneradas, ligando pessoas que desejam ir a algum, a pessoas com transportes que vão para o mesmo destino e desejam fazer uma graninha!

## 🚀 Começando

Essas instruções permitirão que você obtenha uma cópia do projeto em operação na sua máquina local para fins de desenvolvimento e teste.

### 📋 Pré-requisitos

Você precisará de uma máquina com Docker instalado. Se o ambiente for Windows por favor verifique também a instalação do docker-compose. No terminal digite:

```
$ docker --version && docker-compose --version
```

A resposta esperada deve ser algo semelhante a isso:

```
Docker version 25.0.3, build 4debf41
Docker Compose version v2.24.5-desktop.1
```

### 🔧 Instalação

#### Antes de mais nada **renomeie o arquivo '.env.exemplo' para '.env'**

Inicialize os containers do Banco de Dados

```
docker compose database up -d
```

Uma vez iniciado suba também o container do backend

```
docker compose backend up
```

Por fim, abra uma nova aba do terminal e suba o container do frontend

```
docker compose frontend up
```

Uma vez em execução algo similar a isso deve aparecer, escaneie o QR Code, instale o App do Expo no seu celular e o aplicativo deverá iniciar normalmente.

```
$ expo start
Starting project at C:\your\path\to\project
Starting Metro Bundler
Your project may not work correctly until you install the expected versions of the packages.
▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄
█ ▄▄▄▄▄ █▄▀▀▄▄▀▀█▄█ ▄▄▄▄▄ █
█ █   █ ███▄█  ▀███ █   █ █
█ █▄▄▄█ ██▄▀▄▀ ██▀█ █▄▄▄█ █
█▄▄▄▄▄▄▄█ █ ▀▄▀ ▀ █▄▄▄▄▄▄▄█
█ ▄▀▄ ▀▄██ ▄▄▀▀█▀ █▄█▀█▀▀▄█
██  ▀██▄▄█▀  ▀█▄▄ ▀███▄▀▀ █
█ ▄▀ ▄ ▄  ▄█▄▄▀▄ █ ▄▀▀█▀ ██
█ ▄ ▄  ▄▄▀▄  ▄▄▀ ▄▀ ██▄▀  █
█▄████▄▄█ ▀▄▀▀  █ ▄▄▄  ▄▀▄█
█ ▄▄▄▄▄ ██ █▄▀  █ █▄█ ██▀▄█
█ █   █ █  ▄█▄██▄ ▄  ▄ █  █
█ █▄▄▄█ █▀▄▀█▄█  ▄█▀▀▄█   █
█▄▄▄▄▄▄▄█▄▄██▄▄▄▄▄▄█▄▄███▄█

› Metro waiting on exp://192.168.1.68:8081
› Scan the QR code above with Expo Go (Android) or the Camera app (iOS)

› Web is waiting on http://localhost:8081

› Using Expo Go

› Press s │ switch to development build
› Press a │ open Android
› Press w │ open web
› Press j │ open debugger
› Press r │ reload app
› Press m │ toggle menu
› Press o │ open project code in your editor

```

## 📦 Implantação

## 🛠️ Construído com

Ferramentas usadas

- [Spring Boot](https://spring.io/projects/spring-boot) - O framework web usado
- [Maven](https://maven.apache.org/) - Gerente de Dependência
- [Expo](https://expo.dev/) - Framework para desenvolvimento mobile
- [PostgreSql](https://www.postgresql.org/) - Banco de dados
- [Docker](https://www.docker.com/) - Orquestrador de containers

## ✒️ Autores

- **Um desenvolvedor** - _Trabalho Inicial_ - [Fernandoblima1](https://github.com/fernandoblima1)
- **Fulano De Tal** - _Documentação_ - [fulanodetal](https://github.com/linkParaPerfil)

---
