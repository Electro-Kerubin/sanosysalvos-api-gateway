# sanosysalvos-api-gateway

API Gateway y punto de entrada único de Sanos y Salvos.

## Qué hace

Este repositorio no contiene lógica de negocio de dominios ni persistencia. Su responsabilidad es centralizar autenticación, autorización, logging, trazabilidad, manejo de errores y ruteo hacia los microservicios.

## Rutas

- `/api/auth/**`
- `/api/usuarios/**`
- `/api/reportes/**`
- `/api/mascotas/**`
- `/api/contactos/**`
- `/api/especies/**`
- `/api/razas/**`
- `/api/geo/**`
- `/api/coincidencias/**`
- `/api/notificaciones/**`
- `/api/storage/**`

## Ejecutar localmente con Docker

Si vas a conectar el gateway con microservicios levantados en otros repositorios, crea primero la red compartida:

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
