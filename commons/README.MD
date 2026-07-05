# Commons

## Descripción

`commons` es una librería compartida utilizada por todos los microservicios de la plataforma.

Su objetivo es centralizar componentes reutilizables que son comunes a múltiples servicios, evitando duplicación de código y garantizando consistencia en las respuestas de la API, manejo de errores, excepciones y estructuras compartidas.

Este módulo forma parte de la infraestructura base de la arquitectura y no contiene lógica de negocio específica de ningún dominio.

---

# Objetivo

Resolver uno de los problemas más frecuentes en arquitecturas de microservicios:

- Respuestas inconsistentes entre servicios.
- Manejo de errores diferente en cada API.
- Duplicación de clases comunes.
- Diferentes formatos de paginación.
- Excepciones implementadas de forma distinta.

Sin una librería compartida, cada microservicio termina implementando sus propias respuestas y errores, generando una experiencia inconsistente para los consumidores de la API.

---

# Problema que resuelve

## Sin Commons

### ms-auth

```json
{
  "message": "Usuario creado"
}
```

### ms-productos

```json
{
  "status": true,
  "result": {}
}
```

### ms-clientes

```json
{
  "success": true,
  "data": {}
}
```

### ms-pedidos

```json
{
  "payload": {}
}
```

Cada servicio responde diferente.

Esto provoca:

- Mayor complejidad para frontend.
- Mayor complejidad para integraciones externas.
- Mayor mantenimiento.
- Falta de estandarización.

---

## Con Commons

Todos los servicios responden igual.

### Respuesta Exitosa

```json
{
  "success": true,
  "message": "Operación realizada correctamente",
  "data": {},
  "timestamp": "2026-07-05T10:00:00"
}
```

### Respuesta de Error

```json
{
  "status": 400,
  "error": "Bad Request",
  "message": "Stock insuficiente",
  "details": [],
  "timestamp": "2026-07-05T10:00:00"
}
```

---

# Responsabilidades

## Incluye

- Respuestas estándar.
- Manejo global de errores.
- Excepciones compartidas.
- Objetos de paginación.
- Enumeraciones comunes.
- Constantes reutilizables.

## No incluye

- Entidades JPA.
- Repositorios.
- Servicios.
- Controladores.
- Seguridad.
- JWT.
- Reglas de negocio.

---

# Arquitectura

```text
commons
│
├── response
│   ├── ApiResponse
│   ├── ErrorResponse
│   └── PageResponse
│
├── exception
│   ├── BusinessException
│   └── GlobalExceptionHandler
│
├── constant
│   └── CommonConstants
│
└── enums
    └── StatusCode
```

---

# Componentes

## ApiResponse

Representa una respuesta exitosa estándar.

### Responsabilidad

Encapsular cualquier respuesta de éxito de la API.

### Ejemplo

```json
{
  "success": true,
  "message": "Producto creado",
  "data": {
    "id": 1
  },
  "timestamp": "2026-07-05T10:00:00"
}
```

---

## ErrorResponse

Representa una respuesta estándar de error.

### Responsabilidad

Unificar todas las respuestas de error.

### Ejemplo

```json
{
  "status": 404,
  "error": "Not Found",
  "message": "Producto no encontrado",
  "details": [],
  "timestamp": "2026-07-05T10:00:00"
}
```

---

## PageResponse

Representa resultados paginados.

### Responsabilidad

Unificar la paginación en todos los microservicios.

### Ejemplo

```json
{
  "content": [],
  "page": 0,
  "size": 10,
  "totalElements": 100,
  "totalPages": 10,
  "first": true,
  "last": false
}
```

---

## BusinessException

Excepción base para errores de negocio.

### Ejemplos

```java
throw new BusinessException(
    "Usuario ya registrado"
);
```

```java
throw new BusinessException(
    "Stock insuficiente"
);
```

```java
throw new BusinessException(
    "Cliente no encontrado"
);
```

---

## GlobalExceptionHandler

Centraliza el manejo de excepciones.

### Responsabilidad

Transformar excepciones Java en respuestas HTTP consistentes.

### Flujo

```text
Exception
    │
    ▼
GlobalExceptionHandler
    │
    ▼
ErrorResponse
    │
    ▼
JSON
```

---

## CommonConstants

Centraliza constantes compartidas.

Ejemplos:

```java
SUCCESS
ERROR
CREATED
UPDATED
DELETED
```

---

## StatusCode

Enumeración común para estados de respuesta.

Ejemplos:

```java
SUCCESS
ERROR
VALIDATION_ERROR
NOT_FOUND
UNAUTHORIZED
FORBIDDEN
```

---

# Flujo completo

Supongamos una petición:

```http
GET /productos/1
```

---

## Paso 1

El Controller recibe la solicitud.

```text
Controller
```

---

## Paso 2

El Service ejecuta la lógica.

```text
Service
```

---

## Paso 3

El Repository consulta la base de datos.

```text
Repository
```

---

## Caso Exitoso

```text
Repository
    │
    ▼
Service
    │
    ▼
ApiResponse
    │
    ▼
Controller
    │
    ▼
Cliente
```

Respuesta:

```json
{
  "success": true,
  "message": "Producto encontrado",
  "data": {}
}
```

---

## Caso Error

```text
Repository
    │
    ▼
Service
    │
    ▼
BusinessException
    │
    ▼
GlobalExceptionHandler
    │
    ▼
ErrorResponse
    │
    ▼
Cliente
```

Respuesta:

```json
{
  "status": 400,
  "message": "Producto no encontrado"
}
```

---

# Integración con los demás módulos

```text
                    ┌──────────────┐
                    │   commons    │
                    └──────┬───────┘
                           │
      ┌────────────────────┼────────────────────┐
      │                    │                    │
      ▼                    ▼                    ▼
 ┌───────────┐      ┌───────────┐      ┌───────────┐
 │ ms-auth   │      │ productos │      │ clientes  │
 └───────────┘      └───────────┘      └───────────┘
                           │
                           ▼
                    ┌───────────┐
                    │ pedidos   │
                    └───────────┘
```

Todos reutilizan las mismas estructuras.

---

# Ventajas

## Consistencia

Todas las APIs responden de la misma forma.

---

## Reutilización

Las clases comunes se implementan una sola vez.

---

## Menor mantenimiento

Los cambios se realizan en un único lugar.

---

## Menos código duplicado

Se evita copiar clases entre proyectos.

---

## Mejor experiencia para frontend

El frontend conoce un único formato de respuesta.

---

## Escalabilidad

Nuevos microservicios heredan automáticamente el estándar.

---

# Desventajas

## Dependencia compartida

Todos los servicios dependen de la versión de `commons`.

---

## Cambios incompatibles

Modificar estructuras compartidas puede afectar múltiples servicios.

---

## Mayor coordinación

Las actualizaciones requieren control de versiones adecuado.

---

# Buenas prácticas

- Mantener el módulo pequeño.
- Evitar agregar lógica de negocio.
- Evitar dependencias innecesarias.
- Mantener compatibilidad hacia atrás.
- Versionar correctamente los cambios.

---

# Dependencias

- Java 21
- Spring Boot 3.x
- Lombok

---

# Relación con otros módulos

| Módulo | Relación |
|----------|----------|
| security-core | Independiente |
| security-starter | Independiente |
| ms-auth | Utiliza commons |
| ms-productos | Utiliza commons |
| ms-clientes | Utiliza commons |
| ms-pedidos | Utiliza commons |

---

# Resumen

`commons` proporciona un conjunto de componentes reutilizables que permiten mantener consistencia entre todos los microservicios de la plataforma.

Su función principal es centralizar respuestas, excepciones, paginación, constantes y estructuras compartidas, reduciendo duplicación de código y simplificando el mantenimiento de la arquitectura.

