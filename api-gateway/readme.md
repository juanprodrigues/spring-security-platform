# 🌐 API Gateway

## 📌 Descripción general

El **API Gateway** es el único punto de entrada al sistema de microservicios.

Su función principal es **recibir todas las solicitudes externas y redirigirlas hacia los microservicios correspondientes**.

Este módulo está construido con **Spring Cloud Gateway** y actúa como capa de enrutamiento centralizada.

---

# 🧠 Responsabilidades del Gateway

## ✔ Funcionales

- Routing de requests hacia microservicios
- Centralización del punto de entrada
- Exposición de rutas públicas y privadas
- Integración con servicios internos:
  - ms-auth
  - ms-productos
  - ms-clientes
  --ms-pedidos

---

## ❌ NO responsabilidades

El Gateway NO debe:

- Manejar lógica de negocio
- Acceder a bases de datos
- Gestionar usuarios
- Validar JWT (en este diseño)
- Contener reglas de autorización complejas

---

# 🏗 Arquitectura del módulo

```
api-gateway
│
├── ApiGatewayApplication.java
├── config
│ └── GatewayConfig
│
└── (sin filtros de seguridad activos)

```
---

# 🔄 Flujo de comunicación
```
CLIENTE
↓
API GATEWAY (8080)
↓
| /auth/** → ms-auth |
| /productos/** → ms-productos |
| /clientes/** → ms-clientes |
| /pedidos/** → ms-pedidos |
```
---

# ⚙️ Configuración de rutas (application.yml)

```yaml
server:
  port: 8080

spring:
  application:
    name: api-gateway

  cloud:
    gateway:
      routes:

        - id: auth-service
          uri: http://localhost:8081
          predicates:
            - Path=/auth/**

        - id: productos-service
          uri: http://localhost:8082
          predicates:
            - Path=/productos/**

        - id: clientes-service
          uri: http://localhost:8083
          predicates:
            - Path=/clientes/**

        - id: pedidos-service
          uri: http://localhost:8084
          predicates:
            - Path=/pedidos/**
 ```           
🔐 Seguridad (decisión arquitectónica)

En esta versión del sistema:

✔ El Gateway NO valida JWT
✔ La validación de seguridad se delega a cada microservicio
✔ Cada servicio usa security-starter

# 📌 Flujo de seguridad real
Cliente envía request con JWT
Gateway solo enruta la solicitud
Microservicio recibe request
security-starter:
valida token
extrae claims
crea Authentication
carga SecurityContext

# 🧩 Integración con microservicios

El Gateway trabaja junto con:
 ```    
ms-auth → autenticación y emisión de JWT
ms-productos → CRUD protegido
ms-clientes → gestión de clientes
ms-pedidos → gestión de pedidos
security-starter → validación de seguridad
 ```    

# 📈 Ventajas del diseño
Punto único de entrada al sistema
Separación clara de responsabilidades
Microservicios desacoplados
Escalabilidad horizontal
Arquitectura simple y mantenible
# ⚠️ Desventajas actuales
El Gateway no bloquea requests inválidas (JWT no validado aquí)
Mayor carga en microservicios
Falta de rate limiting
Falta de observabilidad centralizada
# 🚀 Estado del módulo

- Routing funcional
- Microservicios conectados
- Arquitectura desacoplada
- Gateway operativo