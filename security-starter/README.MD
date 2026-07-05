# Security Starter

## Descripción

`security-starter` es un Spring Boot Starter encargado de integrar la librería `security-core` con Spring Security.

Su objetivo es permitir que cualquier microservicio pueda validar JWT y autenticar usuarios automáticamente simplemente agregando una dependencia Maven, sin necesidad de escribir filtros, configuraciones de seguridad o lógica de autenticación repetitiva.

Este módulo **no genera tokens**.

La generación de tokens es responsabilidad de `ms-auth` utilizando `security-core`.

La responsabilidad de `security-starter` es:

- Leer el JWT enviado por el cliente.
- Validar el JWT utilizando `security-core`.
- Extraer los claims del token.
- Crear un objeto `Authentication`.
- Registrar el usuario autenticado en el `SecurityContext`.
- Permitir que los controladores accedan al usuario autenticado mediante:
    - `Authentication`
    - `@AuthenticationPrincipal`

---

# Objetivo dentro de la arquitectura

```text
                     ┌─────────────────┐
                     │   security-core │
                     │                 │
                     │ Genera JWT      │
                     │ Valida JWT      │
                     │ Lee Claims      │
                     └────────┬────────┘
                              │
                              │ utilizado por
                              ▼
                     ┌─────────────────┐
                     │ security-starter│
                     │                 │
                     │ Spring Security │
                     │ Authentication  │
                     │ SecurityContext │
                     └────────┬────────┘
                              │
                              │ protege
                              ▼
         ┌─────────────┬─────────────┬─────────────┐
         │             │             │             │
         ▼             ▼             ▼             ▼
   api-gateway   ms-productos  ms-clientes  ms-pedidos
```

---

# Responsabilidades

## Incluye

- Integración con Spring Security.
- Validación automática de JWT.
- Creación de Authentication.
- Creación de SecurityContext.
- Configuración SecurityFilterChain.
- Manejo de errores 401.
- Manejo de errores 403.
- Autoconfiguración Spring Boot.

## No incluye

- Login.
- Registro de usuarios.
- Refresh Token.
- Logout.
- Repositorios.
- Entidades JPA.
- Base de datos.
- Gestión de usuarios.

---

# Dependencia principal

```xml
<dependency>
    <groupId>com.empresa</groupId>
    <artifactId>security-starter</artifactId>
</dependency>
```

---

# Configuración

Cada microservicio debe definir:

```yaml
security:
  jwt:
    secret: my-secret-key
    issuer: empresa
    expiration: 3600000
    refresh-expiration: 604800000
```

---

# Estructura del módulo

```text
security-starter
│
├── autoconfigure
│   └── SecurityAutoConfiguration
│
├── config
│   └── SecurityConfig
│
├── filter
│   └── JwtAuthenticationFilter
│
├── handler
│   ├── JwtAuthenticationEntryPoint
│   └── JwtAccessDeniedHandler
│
├── principal
│   └── JwtPrincipal
│
└── properties
    └── JwtProperties
```

---

# Flujo completo de autenticación

Supongamos la siguiente petición:

```http
GET /productos
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9...
```

---

## Paso 1 - Llega la petición

```text
Cliente
   │
   ▼
API Gateway
   │
   ▼
Microservicio
```

Spring Security intercepta automáticamente la petición.

---

## Paso 2 - JwtAuthenticationFilter

Clase:

```text
JwtAuthenticationFilter
```

Responsabilidades:

- Leer Authorization Header.
- Verificar Bearer Token.
- Extraer JWT.
- Delegar validación al security-core.

Flujo:

```text
Request
   │
   ▼
Authorization Header
   │
   ▼
Bearer Token
```

---

## Paso 3 - Validación mediante security-core

El filtro utiliza:

```java
JwtProvider
```

proveniente de:

```text
security-core
```

Proceso:

```text
JwtAuthenticationFilter
            │
            ▼
      JwtProvider
            │
            ▼
     JwtValidator
            │
            ▼
   Verificación firma
            │
            ▼
 Verificación expiración
```

Si el token es inválido:

```text
InvalidTokenException
```

Si el token expiró:

```text
ExpiredTokenException
```

---

## Paso 4 - Extracción de Claims

Una vez validado:

```text
JwtParser
```

extrae:

```text
userId
username
roles
```

Ejemplo:

```json
{
  "sub":"admin",
  "uid":1,
  "roles":["ADMIN","USER"]
}
```

---

## Paso 5 - Construcción del Principal

Se crea:

```java
JwtPrincipal
```

Ejemplo:

```java
JwtPrincipal(
    1L,
    "admin",
    Set.of("ADMIN","USER")
)
```

---

## Paso 6 - Creación de Authentication

Spring Security necesita un objeto:

```java
Authentication
```

Por ello se crea:

```java
UsernamePasswordAuthenticationToken
```

que contiene:

```text
Principal
Authorities
Credentials(null)
```

Representación:

```text
JwtPrincipal
      │
      ▼
UsernamePasswordAuthenticationToken
```

---

## Paso 7 - Registro en SecurityContext

Finalmente:

```java
SecurityContextHolder
```

almacena el usuario autenticado.

```text
Authentication
        │
        ▼
SecurityContextHolder
```

A partir de este momento el usuario queda autenticado para toda la petición.

---

# Acceso desde Controllers

Una vez autenticado:

## Opción 1

```java
@GetMapping("/me")
public String me(Authentication authentication) {

    return authentication.getName();
}
```

---

## Opción 2

```java
@GetMapping("/me")
public JwtPrincipal me(
        @AuthenticationPrincipal JwtPrincipal principal
) {
    return principal;
}
```

---

# Manejo de errores

## JwtAuthenticationEntryPoint

Responsable de:

```text
401 Unauthorized
```

Ejemplos:

- Token inválido.
- Token ausente.
- Token mal formado.

Respuesta:

```json
{
  "error":"Unauthorized"
}
```

---

## JwtAccessDeniedHandler

Responsable de:

```text
403 Forbidden
```

Ejemplos:

- Usuario autenticado.
- Sin permisos suficientes.

Respuesta:

```json
{
  "error":"Forbidden"
}
```

---

# Autoconfiguración

La clase:

```text
SecurityAutoConfiguration
```

es detectada automáticamente por Spring Boot.

Registra:

```text
JwtProperties
JwtProvider
JwtAuthenticationFilter
JwtAuthenticationEntryPoint
JwtAccessDeniedHandler
SecurityFilterChain
```

No es necesario declarar Beans manualmente.

---

# Relación con security-core

## security-core

Responsable de:

```text
Generar Access Token
Generar Refresh Token
Validar JWT
Leer Claims
```

## security-starter

Responsable de:

```text
Integrar JWT con Spring Security
Crear Authentication
Crear SecurityContext
Proteger endpoints
```

---

# Flujo completo de extremo a extremo

## Login

```text
Cliente
   │
   ▼
POST /auth/login
   │
   ▼
ms-auth
   │
   ▼
security-core
   │
   ▼
Access Token
Refresh Token
```

---

## Consumo de API protegida

```text
Cliente
   │
   ▼
Bearer Token
   │
   ▼
api-gateway
   │
   ▼
JwtAuthenticationFilter
   │
   ▼
security-core
   │
   ▼
Validación JWT
   │
   ▼
Claims
   │
   ▼
Authentication
   │
   ▼
SecurityContext
   │
   ▼
Controller
```

---

# Beneficios

- Reutilización entre microservicios.
- Configuración centralizada.
- Menor duplicación de código.
- Independencia de la implementación JWT.
- Integración automática con Spring Security.
- Compatible con arquitectura basada en microservicios.
- Fácil mantenimiento y evolución.

---

# Resumen

`security-starter` es la capa de integración entre `security-core` y Spring Security.

Mientras `security-core` se ocupa exclusivamente de la lógica JWT, `security-starter` transforma un JWT válido en un usuario autenticado dentro del ecosistema Spring, permitiendo que cualquier microservicio protegido pueda conocer al usuario actual sin consultar bases de datos ni revalidar manualmente el token.