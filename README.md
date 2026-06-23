# SOA Demo — Diplomado Arquitectura de Software

Demostración práctica de una arquitectura **SOA (Service-Oriented Architecture)** compuesta por microservicios independientes integrados mediante un **ESB (Enterprise Service Bus)** construido con Apache Camel.

---

## Tabla de contenidos

- [Visión general](#visión-general)
- [Herramientas y tecnologías](#herramientas-y-tecnologías)
- [Arquitectura](#arquitectura)
  - [Diagrama de componentes](#diagrama-de-componentes)
  - [Diagrama de capas](#diagrama-de-capas)
  - [Flujo de la API compuesta](#flujo-de-la-api-compuesta)
- [Proyectos](#proyectos)
  - [employee-service](#employee-service)
  - [department-service](#department-service)
  - [exchange-service](#exchange-service)
  - [camel-esb-service](#camel-esb-service)
  - [soa-client](#soa-client)
- [Puertos y URLs](#puertos-y-urls)
- [Cómo ejecutar](#cómo-ejecutar)
  - [Opción A — Docker (recomendado)](#opción-a--docker-recomendado)
  - [Opción B — Maven local](#opción-b--maven-local)

---

## Visión general

El proyecto modela un sistema de gestión de personal en el que:

1. **Servicios de negocio** (`employee-service`, `department-service`, `exchange-service`) exponen APIs REST independientes con su propia base de datos o lógica.
2. El **ESB** (`camel-esb-service`) actúa como bus de integración: enruta peticiones, orquesta llamadas paralelas/secuenciales a los servicios de negocio y expone una API unificada al exterior.
3. El **cliente web** (`soa-client`) consume únicamente el ESB —nunca los servicios internos directamente— mostrando los resultados en una interfaz Thymeleaf.

---

## Herramientas y tecnologías

| Tecnología | Versión | Uso |
|---|---|---|
| Java | 21 | Lenguaje de programación |
| Spring Boot | 3.5.0 | Framework base de todos los servicios |
| Apache Camel | 4.8.0 | Motor de integración y rutas del ESB |
| Spring Data JPA | (Boot) | Persistencia de datos ORM |
| H2 Database | (Boot) | Base de datos embebida en memoria |
| Lombok | (Boot) | Reducción de boilerplate (getters, builders, etc.) |
| Thymeleaf | (Boot) | Motor de plantillas HTML del cliente web |
| SpringDoc OpenAPI | 2.8.x | Documentación y Swagger UI |
| Hawtio | 4.4.1 | Consola web de monitoreo de rutas Camel vía JMX |
| Spring Actuator | (Boot) | Endpoints de salud y métricas |
| Bean Validation | (Boot) | Validación de entidades (`@NotBlank`, `@Email`, etc.) |
| Maven | 3.x | Gestión de dependencias y build |

---

## Arquitectura

### Diagrama de componentes

```
┌─────────────────────────────────────────────────────────────────────┐
│                          soa-client  :8084                          │
│                    (Thymeleaf + RestClient)                         │
└────────────────────────────┬────────────────────────────────────────┘
                             │  HTTP GET /soa/employee-profile/{id}
                             ▼
┌─────────────────────────────────────────────────────────────────────┐
│                    camel-esb-service  :8092                         │
│                                                                     │
│  ┌───────────────────────┐   ┌──────────────────────────────────┐  │
│  │  REST DSL (Camel)     │   │  Routes                          │  │
│  │  /soa/empleados/{id}  │──▶│  EmployeeRoute                   │  │
│  │  /soa/departments/{id}│──▶│  DepartmentRoute                 │  │
│  │  /soa/exchange/usd    │──▶│  ExchangeRoute                   │  │
│  │  /soa/employee-profile│──▶│  EmployeeProfileRoute            │  │
│  │       /{id}           │   │    + EmployeeProfileProcessor     │  │
│  └───────────────────────┘   └──────────────────────────────────┘  │
│  Swagger UI: /swagger-ui.html    Hawtio: /hawtio                   │
└──────┬──────────────────┬─────────────────┬────────────────────────┘
       │                  │                 │
       ▼                  ▼                 ▼
┌──────────────┐ ┌──────────────┐ ┌──────────────────┐
│employee-svc  │ │department-svc│ │  exchange-svc    │
│   :8082      │ │   :8083      │ │    :8091         │
│  H2 in-mem   │ │  H2 in-mem   │ │  (tasa fija)     │
│  /api/       │ │  /api/       │ │  /api/exchange/  │
│  empleados   │ │  departments │ │  usd             │
└──────────────┘ └──────────────┘ └──────────────────┘
```

### Diagrama de capas

```
┌─────────────────────────────────────────────────┐
│          CAPA DE PRESENTACIÓN                   │
│   soa-client (Thymeleaf, Spring MVC)            │
└────────────────────┬────────────────────────────┘
                     │
┌────────────────────▼────────────────────────────┐
│          CAPA DE INTEGRACIÓN (ESB)              │
│   camel-esb-service                             │
│   · Enrutamiento   · Orquestación               │
│   · Transformación · API Gateway                │
└──────┬──────────────┬──────────────┬────────────┘
       │              │              │
┌──────▼──────┐ ┌─────▼──────┐ ┌────▼────────────┐
│  CAPA DE    │ │  CAPA DE   │ │  CAPA DE        │
│  NEGOCIO    │ │  NEGOCIO   │ │  NEGOCIO        │
│ employee-   │ │department- │ │ exchange-       │
│ service     │ │ service    │ │ service         │
│  · Service  │ │ · Service  │ │ · Service       │
│  · Controller│ │ · Controller│ │ · Controller  │
└──────┬──────┘ └─────┬──────┘ └─────────────────┘
       │              │
┌──────▼──────┐ ┌─────▼──────┐
│  CAPA DE    │ │  CAPA DE   │
│  DATOS      │ │  DATOS     │
│  JPA + H2   │ │  JPA + H2  │
└─────────────┘ └────────────┘
```

### Flujo de la API compuesta

`GET /soa/employee-profile/{id}` orquesta tres servicios en secuencia:

```
Cliente  →  ESB  →  1. employee-service  GET /api/empleados/{id}
                         ↓  EmployeeDTO (nombre, cargo, salarioPEN, departmentId)
                    2. department-service  GET /api/departments/{departmentId}
                         ↓  DepartmentDTO (nombre del departamento)
                    3. exchange-service    GET /api/exchange/usd
                         ↓  ExchangeRateDTO (rate PEN→USD)
                    4. EmployeeProfileProcessor
                         salarioUSD = salarioPEN × rate
                         ↓  EmployeeProfileResponse
         ←  ESB  ←  { id, nombre, apellido, cargo, departamento,
                       salarioPEN, tipoCambio, salarioUSD }
```

---

## Proyectos

### employee-service

| Atributo | Valor |
|---|---|
| Puerto | `8082` |
| Base de datos | H2 en memoria (`employeedb`) |
| Consola H2 | `http://localhost:8082/h2-console` |
| Swagger UI | `http://localhost:8082/swagger-ui.html` |

**Funcionalidad:** CRUD completo de empleados con validación de datos.

**Entidad `Employee`:**

| Campo | Tipo | Descripción |
|---|---|---|
| `id` | Long | Identificador autogenerado |
| `nombre` | String | Nombre (obligatorio) |
| `apellido` | String | Apellido (obligatorio) |
| `email` | String | Email único y válido |
| `cargo` | String | Puesto de trabajo |
| `departamento` | String | Nombre del departamento |
| `departmentId` | Long | FK al departamento (usado por el ESB) |
| `salario` | Double | Salario en PEN |

**Endpoints REST:**

| Método | URL | Descripción |
|---|---|---|
| `GET` | `/api/empleados` | Listar todos los empleados |
| `GET` | `/api/empleados/{id}` | Obtener empleado por ID |
| `POST` | `/api/empleados` | Crear nuevo empleado |
| `PUT` | `/api/empleados/{id}` | Actualizar empleado |
| `DELETE` | `/api/empleados/{id}` | Eliminar empleado |

---

### department-service

| Atributo | Valor |
|---|---|
| Puerto | `8083` |
| Base de datos | H2 en memoria (`departmentdb`) |
| Consola H2 | `http://localhost:8083/h2-console` |
| Swagger UI | `http://localhost:8083/swagger-ui.html` |

**Funcionalidad:** CRUD completo de departamentos.

**Entidad `Department`:**

| Campo | Tipo | Descripción |
|---|---|---|
| `id` | Long | Identificador autogenerado |
| `nombre` | String | Nombre del departamento |

**Endpoints REST:**

| Método | URL | Descripción |
|---|---|---|
| `GET` | `/api/departments` | Listar todos los departamentos |
| `GET` | `/api/departments/{id}` | Obtener departamento por ID |
| `POST` | `/api/departments` | Crear nuevo departamento |
| `PUT` | `/api/departments/{id}` | Actualizar departamento |
| `DELETE` | `/api/departments/{id}` | Eliminar departamento |

---

### exchange-service

| Atributo | Valor |
|---|---|
| Puerto | `8091` |
| Base de datos | Sin persistencia |
| Swagger UI | `http://localhost:8091/swagger-ui.html` |

**Funcionalidad:** Provee el tipo de cambio PEN → USD. Retorna una tasa fija (`0.2805`) simulando un servicio de cotización de divisas.

**Endpoints REST:**

| Método | URL | Descripción |
|---|---|---|
| `GET` | `/api/exchange/usd` | Obtener tipo de cambio PEN→USD |

**Respuesta de ejemplo:**
```json
{
  "base": "PEN",
  "target": "USD",
  "rate": 0.2805
}
```

---

### camel-esb-service

| Atributo | Valor |
|---|---|
| Puerto | `8092` |
| Swagger UI | `http://localhost:8092/swagger-ui.html` |
| Hawtio Console | `http://localhost:8092/hawtio` |
| OpenAPI spec | `http://localhost:8092/api-doc` |

**Funcionalidad:** Enterprise Service Bus que centraliza la integración entre servicios. Actúa como proxy, router y orquestador mediante el DSL de Apache Camel.

#### Rutas Camel

| Ruta | Route ID | Endpoint expuesto | Servicio destino |
|---|---|---|---|
| `EmployeeRoute` | `employee-route` | `GET /soa/empleados/{id}` | `employee-service` |
| `DepartmentRoute` | `department-route` | `GET /soa/departments/{id}` | `department-service` |
| `ExchangeRoute` | `exchange-route` | `GET /soa/exchange/usd` | `exchange-service` |
| `EmployeeProfileRoute` | `employee-profile-route` | `GET /soa/employee-profile/{id}` | Los tres servicios |
| `RestConfigurationRoute` | — | Configuración global REST DSL | — |

#### API compuesta (EmployeeProfileRoute)

El endpoint `GET /soa/employee-profile/{id}` es la ruta principal del ESB. Ejecuta en secuencia:

1. Llama a `employee-service` para obtener datos del empleado.
2. Usa el `departmentId` del empleado para llamar a `department-service`.
3. Llama a `exchange-service` para obtener el tipo de cambio.
4. El `EmployeeProfileProcessor` calcula `salarioUSD = salarioPEN × rate` y construye la respuesta final.

**Respuesta de ejemplo:**
```json
{
  "id": 1,
  "nombre": "Juan",
  "apellido": "Pérez",
  "cargo": "Desarrollador Senior",
  "departamento": "Tecnología",
  "salarioPEN": 5000.0,
  "tipoCambio": 0.2805,
  "salarioUSD": 1402.5
}
```

#### Configuración de servicios backend (application.yml)

```yaml
services:
  employee:
    url: http://localhost:8082
  exchange:
    url: http://localhost:8091
  department:
    url: http://localhost:8083
```

---

### soa-client

| Atributo | Valor |
|---|---|
| Puerto | `8084` |
| Interfaz web | `http://localhost:8084` |

**Funcionalidad:** Cliente web con interfaz Thymeleaf que permite buscar el perfil completo de un empleado por ID. Se conecta **exclusivamente al ESB**; nunca accede directamente a los servicios internos.

**Componentes principales:**

- `EmployeeProfileClient`: Usa `RestClient` de Spring 6 para llamar al ESB (`GET /soa/employee-profile/{id}`).
- `HomeController`: Controlador MVC que recibe el parámetro `id`, invoca el cliente y pasa el resultado al template.
- `index.html`: Formulario de búsqueda y visualización del perfil con Thymeleaf.

---

## Puertos y URLs

| Servicio | Puerto | Swagger UI | Otros |
|---|---|---|---|
| `employee-service` | `8082` | `/swagger-ui.html` | H2: `/h2-console` |
| `department-service` | `8083` | `/swagger-ui.html` | H2: `/h2-console` |
| `soa-client` | `8084` | — | Web: `/` |
| `exchange-service` | `8091` | `/swagger-ui.html` | — |
| `camel-esb-service` | `8092` | `/swagger-ui.html` | Hawtio: `/hawtio`, OpenAPI: `/api-doc` |

---

## Cómo ejecutar

### Opción A — Docker (recomendado)

Requisitos: **Docker** y **Docker Compose** instalados.

```bash
# Desde la raíz del repositorio
docker compose up --build
```

Docker Compose levanta los 5 servicios en el orden correcto, resuelve las dependencias entre contenedores y configura automáticamente las URLs internas de red:

| Variable de entorno inyectada | Valor en contenedor |
|---|---|
| `SERVICES_EMPLOYEE_URL` | `http://employee-service:8082` |
| `SERVICES_DEPARTMENT_URL` | `http://department-service:8083` |
| `SERVICES_EXCHANGE_URL` | `http://exchange-service:8091` |
| `ESB_BASE_URL` | `http://camel-esb-service:8092` |

Una vez levantado, abrir `http://localhost:8084` en el navegador.

Para detener y eliminar los contenedores:

```bash
docker compose down
```

Para reconstruir las imágenes después de cambios en el código:

```bash
docker compose up --build --force-recreate
```

> **Estructura de archivos Docker**
> ```
> soa-demo/
> ├── docker-compose.yml          ← orquestación completa
> ├── employee-service/Dockerfile
> ├── department-service/Dockerfile
> ├── exchange-service/Dockerfile
> ├── camel-esb-service/Dockerfile
> └── soa-client/Dockerfile
> ```
>
> Todos los `Dockerfile` usan **multi-stage build**: la primera etapa compila con `maven:3.9-eclipse-temurin-21-alpine` y la segunda genera una imagen ligera con `eclipse-temurin:21-jre-alpine`.

---

### Opción B — Maven local

Iniciar cada servicio con Maven en el orden recomendado (los tres servicios de negocio primero, luego el ESB, finalmente el cliente):

```bash
# 1. Employee Service
cd employee-service
mvn spring-boot:run

# 2. Department Service
cd department-service
mvn spring-boot:run

# 3. Exchange Service
cd exchange-service
mvn spring-boot:run

# 4. ESB (Apache Camel)
cd camel-esb-service
mvn spring-boot:run

# 5. Cliente Web
cd soa-client
mvn spring-boot:run
```

Una vez levantados, abrir `http://localhost:8084` en el navegador e ingresar el ID de un empleado para obtener su perfil completo.

