# Biblioteca API

Aplicación web desarrollada como proyecto final del ciclo formativo de **Desarrollo de Aplicaciones Multiplataforma (DAM)**.

El objetivo del proyecto es simular el funcionamiento básico de un sistema de gestión de biblioteca, permitiendo administrar el catálogo de libros, registrar préstamos y devoluciones, y añadir valoraciones de lectura por parte de los usuarios.

---

## Tecnologías utilizadas

**Backend**
- Java
- Spring Boot
- Spring Security
- Spring Data JPA
- Maven

**Base de datos**
- PostgreSQL

**Frontend**
- HTML
- CSS
- JavaScript

**Otros**
- Docker (despliegue)
- Git (control de versiones)

---

## Funcionalidades principales

**Gestión de usuarios**
- Registro de usuarios
- Inicio de sesión
- Control de roles (Bibliotecario / Usuario)

**Gestión de catálogo**
- Alta de libros
- Búsqueda por título
- Búsqueda por ISBN
- Consulta de disponibilidad

**Gestión de préstamos**
- Registro de préstamos
- Registro de devoluciones
- Control de disponibilidad en tiempo real

**Sistema de reservas**
- Los usuarios pueden reservar libros prestados
- Gestión de cola de reservas por orden de llegada
- Notificación automática al devolver un libro

**Sistema de valoraciones**
- Los usuarios pueden valorar libros leídos
- Puntuación numérica del 1 al 5
- Comentarios sobre las lecturas
- Identificación automática de si el libro es de biblioteca o propio

**Recomendaciones con IA**
- Recomendaciones personalizadas basadas en el historial de préstamos del usuario mediante la API de Gemini

---

## Arquitectura

El proyecto sigue una arquitectura en capas basada en el patrón MVC.

**Estructura principal:**
- `controller` → gestión de endpoints REST
- `service` → lógica de negocio
- `repository` → acceso a datos mediante JPA
- `model` → entidades del sistema
- `dto` → objetos de transferencia de datos
- `exception` → jerarquía de excepciones y manejo de errores global
- `config` → configuración de seguridad

---

## Seguridad y autenticación

La autenticación se gestiona mediante **sesiones HTTP de Spring Security**. Al hacer login, Spring crea una sesión en el servidor e identifica al usuario en cada petición a través de una cookie de sesión. Las contraseñas nunca se almacenan en texto plano, sino hasheadas con **BCrypt**.

Se optó por este enfoque en lugar de JWT porque el frontend está integrado en la misma aplicación, lo que hace que las sesiones sean la solución más adecuada y directa.

El control de acceso por rol se gestiona con `@PreAuthorize` a nivel de endpoint, diferenciando entre el rol `BIBLIOTECARIO` y el rol `USUARIO`.

---

## Base de datos

El sistema utiliza **PostgreSQL** como base de datos relacional. El archivo `datos.sql` incluye datos de ejemplo para poder probar la aplicación sin configuración adicional.

**Entidades principales:**
- `Usuario` — gestión de usuarios y roles
- `Libro` — catálogo de libros
- `Prestamo` — historial de préstamos y devoluciones
- `Reserva` — cola de reservas por libro
- `Valoracion` — valoraciones de los usuarios

---

## Ejecución del proyecto

### Con Docker (recomendado)

1. Crear el archivo `.env` en la raíz del proyecto con las siguientes variables:

```
DB_URL=jdbc:postgresql://db:5432/BibliotecaDB
DB_USER=postgres
DB_PASSWORD=tu_password
GEMINI_API_KEY=tu_clave_gemini
```

2. Levantar el proyecto:

```bash

docker compose up --build
```

La aplicación estará disponible en: http://localhost:8080

### Usuarios de prueba

| Usuario   | Contraseña     | Rol           |
|-----------|----------------|---------------|
| dario1    | dario1         | BIBLIOTECARIO |
| dario     | biblioteca123  | BIBLIOTECARIO |
| ana1      | biblioteca123  | USUARIO       |
| carlos    | biblioteca123  | USUARIO       |
| maria     | biblioteca123  | USUARIO       |