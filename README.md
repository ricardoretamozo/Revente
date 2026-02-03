# Revente Backend API 🎟️

Bienvenido al backend de **Revente**, el marketplace secundario de entradas seguro y confiable. Este proyecto está construido con **Java 17 (Spring Boot)** y **PostgreSQL**, utilizando **Docker** para un despliegue sencillo.

## 📋 Requisitos Previos

*   **Docker Desktop** (asegúrate de que esté corriendo).
*   **Git**.

## ⚙️ Archivos Necesarios (Configuración Secreta)

Por seguridad, hay archivos con credenciales que **NO** están en el repositorio y debes crearlos manualmente antes de iniciar:

### 1. Variables de Entorno (`.env`)
Crea un archivo llamado `.env` en la raíz del proyecto (al lado de `docker-compose.yml`). Copia el contenido de `.env.example` y rellena tus claves:

```ini
JWT_SECRET=tu_secreto_super_seguro_para_tokens
FIREBASE_API_KEY=tu_api_key_de_google_cloud
```

### 2. Firebase Admin SDK (`firebase-adminsdk.json`)
Necesitas el archivo de credenciales de servicio de Google.
1.  Ve a la Consola de Firebase -> Project Settings -> Service Accounts.
2.  Genera una nueva Private Key (descarga el JSON).
3.  Renombra el archivo a `firebase-adminsdk.json`.
4.  Colócalo en la carpeta: `src/main/resources/`.

> **Nota:** Si este archivo no existe, el backend fallará al iniciar porque no podrá conectarse a Firebase Auth/Storage.

## 🚀 Cómo Levantar el Proyecto

Simplemente ejecuta:

```bash
docker-compose up --build -d
```

Esto levantará:
1.  **Base de Datos (PostgreSQL)** en el puerto `5432`.
2.  **Backend (Spring Boot)** en el puerto `8080`.

La primera vez, se ejecutarán automáticamente las migraciones (`Flyway`) para crear las tablas y se insertarán datos de prueba (**Usuarios, Eventos y Tickets**). No necesitas ejecutar ningún script SQL manualmente.

Si deseas reiniciar la base de datos desde cero (borrar todo y volver a insertar datos de prueba), ejecuta:
```bash
docker-compose down -v
docker-compose up -d --build
```

## 📚 Documentación de la API

### Autenticación
*   `POST /api/v1/auth/check-status`: Verifica si un teléfono ya está registrado.
*   `POST /api/v1/auth/login-with-phone`: Login/Registro con OTP.

### Eventos
*   `GET /api/v1/events`: Lista eventos activos (conciertos, deportes, etc.).

### Tickets
*   `POST /api/v1/tickets`: Publicar una entrada para venta.
    *   Requiere `Multipart` (JSON `data` + Archivo `file`).

### Ofertas (Próximamente)
*   `POST /api/v1/offers`: Ofertar por una entrada.

## 🛠️ Pruebas con Postman
En la carpeta `artifacts/` de este repositorio (o proporcionado por el desarrollador) encontrarás:
*   `revente_postman_collection.json`: Colección completa lista para importar.

## 🐛 Solución de Problemas Comunes

*   **Error "Could not resolve placeholder JWT_SECRET"**: Te falta el archivo `.env`.
*   **Error "firebase-adminsdk.json not found"**: Te falta el archivo JSON en `src/main/resources`.
*   **Error Docker en Mac M1/M2/M3/M4**: Asegúrate de tener "Use Rosetta" activado en Docker Desktop o usa las imágenes proporcionadas en el Dockerfile (ya optimizadas).
