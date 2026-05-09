# BasicApiCleanArchitecture — Spring Security con Arquitectura Limpia

Ejemplo de arquitectura limpia en un proyecto Spring Webflux. Este proyecto administra un API de usuarios a través de operaciones reactivas **con una capa completa de seguridad JWT implementada siguiendo los principios de Clean Architecture**.

---

## Índice

1. [Pasos para ejecutar el proyecto](#pasos-para-ejecutar-el-proyecto)
2. [Arquitectura del proyecto](#arquitectura-del-proyecto)
3. [Implementación de seguridad](#implementación-de-seguridad)
   - [Decisiones de diseño](#decisiones-de-diseño)
   - [Flujo de autenticación](#flujo-de-autenticación)
   - [Flujo de autorización](#flujo-de-autorización)
   - [Capas involucradas](#capas-involucradas)
   - [Archivos creados y modificados](#archivos-creados-y-modificados)
4. [Endpoints de la API](#endpoints-de-la-api)
5. [Variables de entorno](#variables-de-entorno)

---

## Pasos para ejecutar el proyecto

Clonar este repositorio ejecutando el siguiente comando en la consola:

```bash
git clone https://github.com/FelipeTriana/BasicApiCleanArchitecture.git
```

Ubicarse en la raíz del proyecto:
    
```bash
cd BasicApiCleanArchitecture
```

Verificar que la consola se este ejecutando con la versión 17 de Java: 

```bash
java -versión
```

(Si la versión es diferente se debe modificar la variable de entorno JAVA_HOME para que apunte a la versión 17 de Java)

Ejecutar el siguiente comando desde la raíz del proyecto si la consola es CMD:

```bash
gradlew.bat :applications-app-service:build
```

Para bash o powershell:

```bash
./gradlew :applications-app-service:build
```

Con esto tenemos generado el .jar


Ahora, tambien desde la raíz del proyecto ejecutar en la consola:

```bash
docker-compose up
```

> **Nota de seguridad:** En producción, siempre define `JWT_SECRET` como variable de entorno real antes de levantar los contenedores:
> ```bash
> export JWT_SECRET=MiSecretoSuperSeguroDeAlMenos32Caracteres
> docker-compose up
> ```


Cuando los contenedores esten corriendo el primer paso es registrar un usuario:

```
POST http://localhost:8080/api/auth/register
```

Luego hacer login para obtener el JWT y usarlo en el header `Authorization: Bearer <token>` para los demás endpoints.

> **Nota sobre roles:** Todo usuario registrado recibe el rol `USER` por defecto.  
> Para acceder a los endpoints de administración (`GET /user`, `POST /user`, `DELETE /user/{id}`)  
> debes cambiar el rol manualmente en la base de datos:
> ```bash
> docker exec -it postgres_db_container psql -U root -d cleanexample
> ```
> ```sql
> UPDATE user_data SET role = 'ADMIN' WHERE email = 'tu@email.com';
> ```
> Luego haz login de nuevo para obtener un JWT con el rol `ADMIN` actualizado.

Desde postman se puede importar la siguiente colección para probar los endpoints:

[CleanArq.postman_collection.json](CleanArq.postman_collection.json)

Para obtener el reporte completo de Jacoco de cobertura de pruebas primero correr la tarea de applications-app-service en 
la sección verification llamada: **testCodeCoverageReport**, luego buscar el reporte generado en la ruta: 

applications/app-service/build/reports/jacoco/testCodeCoverageReport/html/index.html

---

## Arquitectura del proyecto

El proyecto sigue **Clean Architecture** con tres grandes anillos concéntricos:

```
┌─────────────────────────────────────────┐
│           Applications (app-service)     │  ← Spring Boot, SecurityConfig, Filtros
│  ┌───────────────────────────────────┐  │
│  │       Infrastructure               │  │  ← Adaptadores (JPA, JWT, BCrypt)
│  │  ┌─────────────────────────────┐  │  │
│  │  │     Domain / UseCase         │  │  │  ← Lógica de negocio pura
│  │  │  ┌───────────────────────┐  │  │  │
│  │  │  │      Domain / Model    │  │  │  │  ← Entidades, Ports (Gateways)
│  │  │  └───────────────────────┘  │  │  │
│  │  └─────────────────────────────┘  │  │
│  └───────────────────────────────────┘  │
└─────────────────────────────────────────┘
```

**Regla fundamental:** las dependencias solo apuntan hacia adentro. El dominio nunca importa Spring, JJWT ni ninguna librería de infraestructura.

---

## Implementación de seguridad

### Decisiones de diseño

#### ¿Por qué JWT y no sesiones de servidor?

Esta API es **stateless**: cada request lleva consigo toda la información de identidad del usuario en el token JWT. Esto permite:

- **Escalabilidad horizontal:** cualquier instancia del servicio puede validar el token sin consultar una sesión centralizada.
- **Desacoplamiento:** el frontend o cualquier cliente HTTP solo necesita almacenar el token (no cookies de sesión).
- **Compatibilidad con microservicios:** el token puede propagarse entre servicios sin infraestructura adicional de sesiones.

#### ¿Por qué se deshabilitó CSRF?

**CSRF (Cross-Site Request Forgery)** es un ataque donde un sitio malicioso envía requests en nombre de un usuario autenticado mediante su cookie de sesión. Al usar JWT en el header `Authorization`, no existe cookie de sesión que un sitio externo pueda usar automáticamente. Por tanto, CSRF es irrelevante y se deshabilita para simplificar la API.

> Regla: deshabilita CSRF solo si tu API es stateless con tokens. Si usas cookies de sesión, DEBES mantener la protección CSRF activa.

#### ¿Por qué BCrypt para los passwords?

BCrypt es un algoritmo de hashing diseñado específicamente para passwords. A diferencia de MD5 o SHA-256:
- Es **lento por diseño** (factor de costo configurable), lo que hace inviable el ataque masivo por diccionario o rainbow tables.
- Genera un **salt aleatorio** por cada hash, así dos usuarios con el mismo password tienen hashes distintos.
- El password en texto plano **jamás toca la base de datos** ni el objeto de dominio `User` (solo el hash resultante).

#### ¿Por qué HS256 y no RS256?

- **HS256** usa una clave simétrica: el mismo secreto firma y verifica. Es simple y suficiente cuando el servicio es monolítico o cuando los microservicios comparten la clave de forma segura.
- **RS256** usa un par público/privado: la clave privada firma y la pública verifica. Ideal cuando necesitas que servicios externos validen tokens sin poder emitirlos. Para este proyecto educativo, HS256 es suficiente y más fácil de razonar.

#### ¿Por qué los Gateways (ports) están en el dominio?

Los puertos (`AuthGateway`, `TokenGateway`, `PasswordGateway`) son **interfaces** en el anillo de dominio. El dominio define *qué* necesita (verificar tokens, codificar passwords) sin saber *cómo* se implementa. Los adaptadores de infraestructura implementan esas interfaces.

Esto permite:
- **Reemplazar implementaciones** (ej. cambiar de JJWT a nimbus-jose-jwt) sin modificar ni un byte del dominio ni de los casos de uso.
- **Testear** los casos de uso con mocks de los gateways, sin levantar ningún servidor ni librería JWT real.

---

### Flujo de autenticación

```
Cliente                  AuthController          AuthUseCase          Gateways / DB
  │                           │                      │                     │
  │  POST /auth/register       │                      │                     │
  │  { name, lastName,         │                      │                     │
  │    email, password }       │                      │                     │
  │──────────────────────────>│                      │                     │
  │                           │  register(User,       │                     │
  │                           │   rawPassword)        │                     │
  │                           │─────────────────────>│                     │
  │                           │                      │  findByEmail(email) │
  │                           │                      │────────────────────>│
  │                           │                      │  Mono.empty()       │
  │                           │                      │<────────────────────│
  │                           │                      │  encode(rawPwd)     │
  │                           │                      │  [boundedElastic]   │
  │                           │                      │─────────────┐       │
  │                           │                      │<────────────┘       │
  │                           │                      │  saveUser(newUser)  │
  │                           │                      │────────────────────>│
  │                           │                      │  User saved         │
  │                           │                      │<────────────────────│
  │  201 { id, name, email,   │                      │                     │
  │        role: "USER" }      │                      │                     │
  │<──────────────────────────│                      │                     │
  │                           │                      │                     │
  │  POST /auth/login          │                      │                     │
  │  { email, password }       │                      │                     │
  │──────────────────────────>│                      │                     │
  │                           │  login(email, pwd)    │                     │
  │                           │─────────────────────>│                     │
  │                           │                      │  findByEmail(email) │
  │                           │                      │────────────────────>│
  │                           │                      │  User found         │
  │                           │                      │<────────────────────│
  │                           │                      │  matches(pwd,hash)  │
  │                           │                      │  [boundedElastic]   │
  │                           │                      │  generateToken(user)│
  │  200 { token, tokenType,  │                      │                     │
  │        expiresIn: 86400 }  │                      │                     │
  │<──────────────────────────│                      │                     │
```

---

### Flujo de autorización

```
Cliente                  JwtAuthWebFilter         SecurityWebFilterChain   Controller
  │                           │                           │                    │
  │  GET /api/user             │                           │                    │
  │  Authorization: Bearer xyz │                           │                    │
  │──────────────────────────>│                           │                    │
  │                           │  validateToken(xyz)        │                    │
  │                           │  → TokenClaims            │                    │
  │                           │  { email, role: ADMIN }   │                    │
  │                           │                           │                    │
  │                           │  contextWrite(auth)        │                    │
  │                           │──────────────────────────>│                    │
  │                           │                           │  hasRole("ADMIN")? │
  │                           │                           │  ✓ Yes             │
  │                           │                           │───────────────────>│
  │                           │                           │                    │
  │  200 [lista de usuarios]  │                           │                    │
  │<─────────────────────────────────────────────────────────────────────────── │
  │                           │                           │                    │
  │  GET /api/user             │                           │                    │
  │  Authorization: Bearer abc │                           │                    │
  │  (token de rol USER)       │                           │                    │
  │──────────────────────────>│                           │                    │
  │                           │  validateToken(abc)        │                    │
  │                           │  { email, role: USER }    │                    │
  │                           │──────────────────────────>│                    │
  │                           │                           │  hasRole("ADMIN")? │
  │                           │                           │  ✗ No              │
  │  403 Forbidden             │                           │                    │
  │<──────────────────────────────────────────────────────│                    │
```

---

### Capas involucradas

#### Domain / Model — Contratos sin dependencias externas

| Archivo | Rol |
|---|---|
| `User.java` | Entidad enriquecida con `email`, `passwordHash`, `role`, `enabled`, `createdAt` |
| `Role.java` | Enum `ADMIN / USER` |
| `TokenClaims.java` | Value object con los claims extraídos de un JWT |
| `AuthGateway.java` | Puerto: `findByEmail(email) → Mono<User>` |
| `TokenGateway.java` | Puerto: `generateToken(User)` / `validateToken(token) → Mono<TokenClaims>` |
| `PasswordGateway.java` | Puerto: `encode(raw)` / `matches(raw, hash)` |
| `BusinessException.Type` | Nuevos tipos: `INVALID_CREDENTIALS`, `USER_ALREADY_EXISTS`, `USER_DISABLED` |

> El dominio no importa Spring, JJWT ni BCrypt. Solo define interfaces que la infraestructura implementa.

#### Domain / UseCase — Lógica de negocio de autenticación

| Archivo | Responsabilidad |
|---|---|
| `AuthUseCase.java` | `register`: valida unicidad, encoda password en hilo boundedElastic, asigna rol USER. `login`: verifica credenciales, genera JWT. |

El password se encoda con `Schedulers.boundedElastic()` porque BCrypt es una operación **bloqueante y CPU-intensive** que no debe correr en el event loop de Netty/WebFlux.

#### Infrastructure / Driven Adapters — Implementaciones

| Archivo | Implementa | Librería |
|---|---|---|
| `UserRepositoryAdapter.java` | `UserGateway` + `AuthGateway` | Spring Data JPA |
| `JwtAdapter.java` | `TokenGateway` | JJWT 0.12.6 |
| `BCryptAdapter.java` | `PasswordGateway` | Spring Security Crypto |
| `UserData.java` | Entidad JPA enriquecida | JPA / Hibernate |
| `UserDataRepository.java` | Repositorio con `findByEmail` | Spring Data |

#### Infrastructure / Entry Points — Controladores y DTOs

| Archivo | Rol |
|---|---|
| `AuthController.java` | `POST /auth/register` y `POST /auth/login` |
| `RegisterRequest.java` | DTO de entrada para registro |
| `LoginRequest.java` | DTO de entrada para login |
| `AuthResponse.java` | DTO de salida: `{ token, tokenType, expiresIn }` |
| `AuthMapper.java` | Conversiones entre DTOs y entidades de dominio |
| `UserDto.java` | Enriquecido con `email` y `role` (nunca expone `passwordHash`) |

#### Applications / App-Service — Configuración de seguridad

| Archivo | Rol |
|---|---|
| `SecurityConfig.java` | `@EnableWebFluxSecurity`: define reglas de autorización por URL, deshabilita CSRF/sesiones, configura CORS |
| `JwtAuthWebFilter.java` | `WebFilter` stateless: extrae Bearer token, lo valida, inyecta el `Authentication` en `ReactiveSecurityContextHolder` |

---

### Archivos creados y modificados

```
domain/model/src/main/java/cleanarchitecture/domain/
├── user/
│   ├── User.java                          ← MODIFICADO (+email, passwordHash, role, enabled, createdAt)
│   ├── Role.java                          ← NUEVO
│   ├── TokenClaims.java                   ← NUEVO
│   └── gateway/
│       ├── AuthGateway.java               ← NUEVO
│       ├── TokenGateway.java              ← NUEVO
│       └── PasswordGateway.java           ← NUEVO
└── common/ex/
    └── BusinessException.java             ← MODIFICADO (+3 tipos)

domain/usecase/src/main/java/cleanarchitecture/usecase/
└── auth/
    └── AuthUseCase.java                   ← NUEVO

infraestructure/driven-adapters/jpa-repository/src/main/java/cleanarchitecture/jpa/
├── user/
│   ├── UserData.java                      ← MODIFICADO (+columnas email, password_hash, role, enabled, created_at)
│   ├── UserDataRepository.java            ← MODIFICADO (+findByEmail)
│   └── UserRepositoryAdapter.java         ← MODIFICADO (implementa AuthGateway)
└── auth/
    ├── JwtAdapter.java                    ← NUEVO
    └── BCryptAdapter.java                 ← NUEVO

infraestructure/entry-points/reactive-web/src/main/java/cleanarchitecture/web/
├── auth/
│   ├── AuthController.java                ← NUEVO
│   ├── dto/
│   │   ├── LoginRequest.java              ← NUEVO
│   │   ├── RegisterRequest.java           ← NUEVO
│   │   └── AuthResponse.java              ← NUEVO
│   └── mapper/
│       └── AuthMapper.java                ← NUEVO
└── user/
    ├── dto/UserDto.java                   ← MODIFICADO (+email, role)
    └── mapper/Mapper.java                 ← MODIFICADO (+email, role en mapeos)

applications/app-service/src/main/java/cleanarchitecture/config/
├── SecurityConfig.java                    ← NUEVO
└── JwtAuthWebFilter.java                  ← NUEVO

applications/app-service/src/main/resources/
└── application.yml                        ← MODIFICADO (+jwt.secret, jwt.expiration-seconds)

applications/app-service/
└── build.gradle                           ← MODIFICADO (+spring-boot-starter-security)

infraestructure/driven-adapters/jpa-repository/
└── build.gradle                           ← MODIFICADO (+jjwt, spring-security-crypto)

docker-compose.yml                         ← MODIFICADO (+JWT_SECRET env var)
```

---

## Endpoints de la API

### Públicos (sin autenticación)

| Método | URL | Body | Respuesta |
|---|---|---|---|
| `POST` | `/api/auth/register` | `{ "name", "lastName", "email", "password" }` | `201 { id, name, lastName, email, role }` |
| `POST` | `/api/auth/login` | `{ "email", "password" }` | `200 { token, tokenType, expiresIn }` |

### Autenticados — Cualquier rol (header: `Authorization: Bearer <token>`)

| Método | URL | Descripción |
|---|---|---|
| `GET` | `/api/user/{id}` | Obtener usuario por ID |

### Autenticados — Solo ADMIN

| Método | URL | Descripción |
|---|---|---|
| `GET` | `/api/user` | Listar todos los usuarios |
| `POST` | `/api/user` | Crear usuario |
| `DELETE` | `/api/user/{id}` | Eliminar usuario |

---

## Variables de entorno

| Variable | Descripción | Valor por defecto (desarrollo) |
|---|---|---|
| `JWT_SECRET` | Clave secreta para firmar JWT. Mínimo 32 caracteres para HS256. | `ChangeThisSecretInProductionMustBe32Chars!!` |
| `SPRING_DATASOURCE_URL` | URL de conexión a PostgreSQL | `jdbc:postgresql://localhost:5432/cleanexample` |

> **IMPORTANTE:** El valor por defecto de `JWT_SECRET` es solo para desarrollo local. En cualquier ambiente productivo o de staging, esta variable DEBE ser inyectada externamente (secrets manager, variables de CI/CD, Kubernetes secrets, etc). Nunca hardcodees secretos en el código.

