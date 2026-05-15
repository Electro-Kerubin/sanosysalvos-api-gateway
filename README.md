# sanosysalvos-api-gateway

## Docker

Este gateway está pensado para correr en Docker y conectarse a los microservicios de otros repositorios mediante una red compartida llamada `sanosysalvos-network`.

### 1. Crear la red compartida

```bash
docker network create sanosysalvos-network
```

### 2. Levantar los microservicios en sus repositorios

Cada microservicio debe publicar su puerto interno en la misma red y usar un nombre de contenedor o servicio coincidente con estas variables:

- `auth-service:8081`
- `reporte-service:8082`
- `geo-service:8083`
- `coincidencias-service:8084`
- `notificaciones-service:8085`
- `storage-service:8086`

### 3. Levantar el gateway

```bash
docker compose up --build
```

### 4. Rutas disponibles para el frontend

- `/api/auth/**`
- `/api/reportes/**`
- `/api/geo/**`
- `/api/coincidencias/**`
- `/api/notificaciones/**`
- `/api/storage/**`

### 5. Salud

- `/actuator/health`
- `/actuator/info`
- `/actuator/gateway`
