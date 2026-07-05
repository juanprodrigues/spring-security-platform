# Security Core

## Descripción

`security-core` es una librería Java encargada exclusivamente de la generación, validación y procesamiento de tokens JWT.

Su objetivo es encapsular toda la lógica relacionada con JWT para que pueda reutilizarse en múltiples proyectos y frameworks sin depender de Spring Security ni de componentes específicos de infraestructura.

Este módulo forma parte de la arquitectura de microservicios de Empresa y constituye la base sobre la cual se construye el sistema de autenticación y autorización.

---

## Responsabilidades

### Incluye

- Generación de Access Tokens.
- Generación de Refresh Tokens.
- Validación de tokens JWT.
- Verificación de firma digital.
- Verificación de expiración.
- Extracción de Claims.
- Extracción de username.
- Extracción de userId.
- Extracción de roles.

### No incluye

- Spring Security.
- Authentication.
- SecurityContext.
- Controllers.
- Repositories.
- Base de datos.
- Gestión de usuarios.
- Login o autenticación.

---

## Arquitectura

```text
security-core
│
├── config
│   └── JwtConfiguration
│
├── jwt
│   ├── TokenService
│   ├── JwtProvider
│   ├── JwtProviderImpl
│   ├── JwtGenerator
│   ├── JwtGeneratorImpl
│   ├── JwtValidator
│   ├── JwtValidatorImpl
│   ├── JwtParser
│   ├── JwtParserImpl
│   └── JwtProviderFactory
│
├── model
│   ├── JwtUser
│   └── JwtToken
│
├── exception
│   ├── JwtException
│   ├── InvalidTokenException
│   └── ExpiredTokenException
│
└── util
    └── JwtConstants
```

---

## Flujo de generación de tokens

### Paso 1

La aplicación crea un usuario JWT.

```java
JwtUser user = new JwtUser(
    1L,
    "admin",
    Set.of("ADMIN", "USER")
);
```

### Paso 2

Se configura el proveedor JWT.

```java
JwtConfiguration config =
        new JwtConfiguration(
                secret,
                issuer,
                accessExpiration,
                refreshExpiration
        );
```

### Paso 3

Se crea el proveedor.

```java
JwtProvider provider =
        JwtProviderFactory.create(config);
```

### Paso 4

Se genera el Access Token.

```java
JwtToken accessToken =
        provider.generateAccessToken(user);
```

### Resultado

```text
JwtUser
    ↓
JwtGenerator
    ↓
JWT firmado
    ↓
JwtToken
```

---

## Flujo de validación

Cuando una aplicación recibe un JWT:

```text
Bearer TOKEN
```

el flujo es:

```text
JWT recibido
      ↓
JwtValidator
      ↓
Verificación de firma
      ↓
Verificación de expiración
      ↓
Token válido
```

Si la firma es inválida:

```text
InvalidTokenException
```

Si el token expiró:

```text
ExpiredTokenException
```

---

## Flujo de lectura de Claims

Una vez validado el token:

```text
JWT
  ↓
JwtParser
  ↓
Claims
```

Información disponible:

```java
provider.extractUsername(token);

provider.extractUserId(token);

provider.extractRoles(token);
```

---

## Claims utilizados

| Claim | Descripción |
|---------|---------|
| sub | Username |
| uid | Identificador del usuario |
| roles | Roles del usuario |
| type | ACCESS o REFRESH |
| iss | Emisor |
| iat | Fecha de emisión |
| exp | Fecha de expiración |

---

## Tipos de Token

### Access Token

Utilizado para acceder a recursos protegidos.

Contiene:

- userId
- username
- roles

Duración recomendada:

```text
15 - 60 minutos
```

### Refresh Token

Utilizado para solicitar nuevos Access Tokens.

Duración recomendada:

```text
7 - 30 días
```

---

## Integración con otros módulos

### ms-auth

Responsable de:

- Login
- Registro
- Logout
- Refresh Token

Utiliza `security-core` para emitir tokens.

```text
ms-auth
   ↓
security-core
```

### security-starter

Responsable de integrar JWT con Spring Security.

Utiliza `security-core` para:

- Validar tokens.
- Leer claims.
- Construir Authentication.

```text
security-starter
       ↓
security-core
```

---

## Dependencias

- Java 21
- JJWT
- Maven

No requiere Spring Boot.

---

## Principios de diseño

- Single Responsibility Principle (SRP)
- Open/Closed Principle (OCP)
- Dependency Inversion Principle (DIP)
- Clean Architecture
- Framework Agnostic
- Reutilización entre proyectos

---

## Objetivo final

Centralizar toda la lógica JWT en una única librería reutilizable para que cualquier aplicación pueda generar y validar tokens sin depender de Spring Security ni de implementaciones específicas de autenticación.