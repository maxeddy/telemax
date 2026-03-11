# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

Telemax is a Kotlin/Spring Boot web application inspired by Dutch Teletekst. It uses JWT-based stateless authentication with an in-memory user store.

## Build & Run Commands

```bash
./gradlew build          # Build + run tests
./gradlew bootRun        # Run the application (default port 8080)
./gradlew test           # Run all tests
./gradlew test --tests "nl.maxbevelander.telemax.SomeTest"  # Run a single test class
./gradlew test --tests "nl.maxbevelander.telemax.SomeTest.methodName"  # Run a single test method
```

## Tech Stack

- **Kotlin 2.2** on **Java 24**, built with **Gradle 9.3** (Kotlin DSL)
- **Spring Boot 4.0** with WebMVC, Security, Thymeleaf, DevTools
- **Jackson** (kotlin module) for JSON serialization
- **JJWT 0.13** for JWT token generation/validation
- **JUnit 5** for testing

## Architecture

The app is a standard Spring Boot layered architecture under `nl.maxbevelander.telemax`:

- **`security/`** — `JwtUtil` (token create/validate/parse) and `JwtAuthenticationFilter` (extracts JWT from the `X-Telemax-Auth` header, not the standard `Authorization` header)
- **`config/`** — `SecurityConfig` (stateless sessions, CSRF disabled, `/api/public/**` is open, everything else requires auth), `UserDetailsConfig` (in-memory users with BCrypt), custom error handlers that return JSON
- **`controller/`** — `AuthController` (`POST /api/public/auth/login`) and `PublicApiController` (public endpoints)
- **`dto/`** — `LoginRequest`, `LoginResponse`

### Key Design Decisions

- JWT tokens are passed via the **`X-Telemax-Auth`** header (not `Authorization: Bearer`)
- Authentication is **in-memory** — no database; users are hardcoded in `UserDetailsConfig`
- All security error responses are JSON (custom `AccessDeniedHandler` and `AuthenticationEntryPoint`)
- The compiler flag `-Xannotation-default-target=param-property` is set for Kotlin data class annotation handling
