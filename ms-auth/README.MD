# ms-auth

## Descripción general

`ms-auth` es el microservicio encargado de la **autenticación y gestión de usuarios** dentro de una arquitectura basada en microservicios.

Su responsabilidad principal es emitir y gestionar credenciales de seguridad mediante JWT.

---

# Responsabilidades del módulo

## ✔ Funcionales

- Registro de usuarios (`/auth/register`)
- Login de usuarios (`/auth/login`)
- Generación de JWT (Access Token y Refresh Token)
- Renovación de tokens (`/auth/refresh`)
- Logout con revocación de refresh tokens
- Gestión de usuarios y roles

---

##  No responsabilidades

Este microservicio NO debe:

- Validar JWT en otros microservicios (eso lo hace `security-starter`)
- Contener lógica de negocio de productos, clientes o pedidos
- Compartir base de datos con otros servicios
- Manejar autorización distribuida fuera del login

---

# Arquitectura interna

 ``` 
ms-auth
│
├── controller
│ └── AuthController
│
├── service
│ ├── AuthService
│ ├── UsuarioService
│ ├── RolService
│ └── RefreshTokenService
│
├── entity
│ ├── Usuario
│ ├── Rol
│ └── RefreshToken
│
├── dto
│ ├── request
│ └── response
│
├── exception
│ └── GlobalExceptionHandler
│
├── mapper
│ └── UsuarioMapper
│
└── config
├── JwtBeansConfig
├── JwtProperties
└── PasswordEncoderConfig
 ``` 



#  Flujo de autenticación

## 1. Registro de usuario


POST /auth/register


### Flujo:
- Validación de datos
- Verificación de usuario existente
- Encriptación de password (BCrypt)
- Asignación de rol `ROLE_USER`
- Persistencia en base de datos

---

## 2. Login


POST /auth/login


### Flujo:
- Validación de credenciales
- Verificación de password
- Construcción de `JwtUser`
- Generación de:
  - Access Token
  - Refresh Token
- Persistencia del refresh token

---

## 3. Refresh Token


POST /auth/refresh


### Flujo:
- Validación del refresh token
- Verificación de expiración
- Generación de nuevos tokens
- Actualización en base de datos

---

## 4. Logout


POST /auth/logout


### Flujo:
- Búsqueda del refresh token
- Marcado como `revoked = true`
- Invalidación del token

---

# Integración con security-core

Este microservicio utiliza la librería:


security-core


para:

- Generar JWT
- Validar tokens
- Extraer claims
- Manejar usuarios y roles

### Componentes utilizados:

- JwtProvider
- JwtGenerator
- JwtParser
- JwtValidator
- JwtUser
- JwtToken

---

# Base de datos

Base de datos exclusiva:


empresa_auth


## Tablas:

- usuarios
- roles
- usuario_rol
- refresh_tokens



# Configuración (application.yml)

```yaml
server:
  port: 8081

spring:
  datasource:
    url: jdbc:mysql://localhost:3306/empresa_auth
    username: root
    password: root

security:
  jwt:
    secret: 12345678901234567890123456789012
    issuer: empresa
    expiration: 3600000
    refresh-expiration: 604800000
 ```  
# Manejo de errores

Se implementa un:

GlobalExceptionHandler
Formato estándar de respuesta:
 ``` 
 {
  "status": 400,
  "error": "BAD_REQUEST",
  "message": "Validation failed",
  "details": [],
  "timestamp": "2026-07-05T..."
}
 ``` 
# Validaciones

Se utiliza:
 ``` 
Jakarta Validation (@Valid)
@NotBlank
@Email
@Size
 ``` 
# Seguridad
Passwords encriptadas con BCrypt
JWT firmado con HMAC SHA
Refresh tokens persistidos en base de datos
Tokens revocables
# Ventajas del diseño
Separación clara de responsabilidades
JWT desacoplado en security-core
Reutilizable en múltiples microservicios
Escalable horizontalmente
Listo para API Gateway
# Desventajas actuales
No hay Redis para blacklist de tokens
No hay rate limiting
No hay auditoría de eventos
Refresh token rotation básica
🚀 Estado del módulo

- Registro funcional
- Login funcional
- Refresh token funcional
- Logout funcional
- JWT integrado
- Manejo global de errores


