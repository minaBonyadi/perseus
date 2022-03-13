# mina bonyadi

## User service application

## Description
This project is a user crud services including an Api.

## files
```
clone https://github.com/minaBonyadi/perseus.git
```

## tools

- [ ] Java 17
- [ ] Maven
- [ ] Spring Boot
- [ ] Redis
- [ ] Git
- [ ] Swagger-ui
- [ ] Openapi
- [ ] Jupiter
- [ ] Mockito
- [ ] Docker
- [ ] Docker Compose

## Test and Deploy

- run application main method in (ChallengeApplication) and run docker-compose.yml to
up mysql database

***

## Features

- User services within a container based environment (Docker)
- providing documentation of my User services API endpoints
- User services are covering by integration tests

## User Model

User:
- id: long
- lastName: string
- firstName: string
- emails: List of Email
- phoneNumbers: List of PhoneNumber

Email:
- id: int
- mail: string

PhoneNumber:
- id: int
- number: string

## Installation

- After cloning this project just run this command( docker-compose up --build ) in your intellij terminal to install and up this application

## Usage

you can use it by postman service call they are services which implemented :
-[ ] Post -> /users/create   -> input (user dto)
-[ ] Get -> /users/{id}      -> input (user id)
-[ ] Get -> /users/spec      -> input (first name, last name)
-[ ] Put -> /{id}/phone-number -> input (user id, phone number dto)
-[ ] Put -> /{id}/email -> input (email dto)
-[ ] Delete -> /users/{id} -> input (user dto)

all outputs are user dto except delete service

## Roadmap
- add spring security

## Authors and acknowledgment

This is Mina. I am a java back-end developer which have more than five years experience in this career,
and I have a bachelor degree in software engineering.
