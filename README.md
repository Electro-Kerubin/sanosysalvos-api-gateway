# sanosysalvos-api-gateway

API Gateway y punto de entrada único de Sanos y Salvos.

## Qué hace

Este repositorio no contiene lógica de negocio de dominios ni persistencia. Su responsabilidad es centralizar autenticación, autorización, logging, trazabilidad, manejo de errores y ruteo hacia los microservicios, integrándose con **Eureka Server** para el descubrimiento dinámico de servicios.

## Stack Tecnológico

- **Java 21**
- **Spring Boot 3.3.0**
- **Spring Cloud (Gateway, Eureka Client)**
- **Spring Security / OAuth2 Resource Server**
- **Docker & Docker Compose**
- **GitHub Actions (CI/CD)**

## Rutas

El ruteo se realiza de forma dinámica hacia los microservicios registrados en Eureka. Las rutas de los microservicios actualmente integrados incluyen:

- `/api/auth/**`
- `/api/reportes/**`
- `/api/geo/**`
- `/api/coincidencias/**`

## Ejecutar localmente con Docker

El proyecto incluye un archivo `docker-compose.yml` en la carpeta `infra/` que levanta la infraestructura base (Eureka, PostgreSQL, RabbitMQ) y los microservicios actuales.

1. Crea tu archivo `.env` dentro de `infra/` copiando el `.env.example` y configurando las variables.
2. Crea la red compartida:
```bash
docker network create sanosysalvos-network
```

```bash
docker compose up --build -d
```

## Variables de entorno

- `JWT_SECRET`
- `AUTH_SERVICE_URL`
- `USUARIOS_SERVICE_URL`
- `REPORTE_SERVICE_URL`
- `MASCOTAS_SERVICE_URL`
- `CONTACTOS_SERVICE_URL`
- `ESPECIES_SERVICE_URL`
- `RAZAS_SERVICE_URL`
- `GEO_SERVICE_URL`
- `MATCH_SERVICE_URL`
- `NOTIFY_SERVICE_URL`
- `STORAGE_SERVICE_URL`

## Salud

- `GET /actuator/health`
- `GET /actuator/info`
- `GET /actuator/gateway`
