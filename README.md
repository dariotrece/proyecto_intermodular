# Biblioteca API

Aplicación web desarrollada como proyecto final del ciclo formativo de **Desarrollo de Aplicaciones Multiplataforma (DAM)**.

El objetivo del proyecto es simular el funcionamiento básico de un sistema de gestión de biblioteca, permitiendo administrar el catálogo de libros, registrar préstamos y devoluciones y añadir valoraciones de lectura por parte de los usuarios.

---

# Tecnologías utilizadas

Backend
- Java
- Spring Boot
- Spring Security
- Spring Data JPA
- Maven

Base de datos
- PostgreSQL

Frontend
- HTML
- CSS
- JavaScript

Otros
- Docker (despliegue)
- Git (control de versiones)

---

# Funcionalidades principales

Gestión de usuarios
- Registro de usuarios
- Inicio de sesión
- Control de roles (Bibliotecario / Usuario)

Gestión de catálogo
- Alta de libros
- Búsqueda por título
- Búsqueda por ISBN
- Consulta de disponibilidad

Gestión de préstamos
- Registro de préstamos
- Registro de devoluciones
- Control de disponibilidad en tiempo real

Sistema de valoraciones
- Los usuarios pueden valorar libros
- Puntuación numérica
- Comentarios sobre las lecturas
- Identificación de libros propios o de biblioteca

---

# Arquitectura

El proyecto sigue una arquitectura en capas basada en el patrón MVC.

Estructura principal:
- controller → gestión de endpoints REST
- service → lógica de negocio
- repository → acceso a datos mediante JPA
- model → entidades del sistema
- dto → objetos de transferencia de datos
- exception → manejo de errores
- config → configuración de seguridad


---

# Base de datos

El sistema utiliza **PostgreSQL** como base de datos relacional.

Las entidades principales del sistema son:

- Usuario
- Libro
- Prestamo
- Valoracion

Las relaciones entre entidades permiten mantener el historial de préstamos y asociar valoraciones a los usuarios.

---

# Ejecución del proyecto

1. Configurar las variables de entorno en un archivo `.env`
  -  DB_URL=jdbc:postgresql://localhost:5432/BibliotecaDB
  -  DB_USERNAME=postgres
  - DB_PASSWORD=tu_password
  - JWT_SECRET=clave_segura

2. Ejecutar la aplicación

La aplicación estará disponible en: http://localhost:8080
