
# 🧠 Android-Task — ANZ Android Coding Exercise

A modular Android application built with **Kotlin**, **Jetpack Compose**, and **Clean Architecture**.  
This project demonstrates best practices in Android development using **multi-module architecture**, **use cases**, and **Jetpack Compose** for a modern, testable, and scalable Android app.

The app displays a list of users fetched from a remote API and allows viewing detailed information for each user.

---

## 🧩 Clean Architecture Overview

The project follows **Clean Architecture principles** with a clear separation of concerns:

- **Presentation Layer** → Jetpack Compose UI + ViewModels (State management)
- **Domain Layer** → Use Cases (Business logic)
- **Data Layer** → Repository + Network (Retrofit API)
- **Model Layer** → Shared entities and data classes

Data flows in one direction:  
`UI → ViewModel → UseCase → Repository → API`,  
and results propagate back via **Flow** to update the UI reactively.

---

## ⚙️ Modules Configuration

Your modular structure is defined in `settings.gradle.kts` as:

```kotlin
include(":app")
include(":core-model")
include(":core-network")
include(":feature-users")


🏗️ Module Breakdown
🟩 1️⃣ :app — Application Layer

Location: app/

Responsibility:

Entry point of the application.

Hosts the main Compose navigation.

Connects all other modules together.

Manages theme, dependency setup, and screen flow.

Contains:

MainActivity.kt

AppTheme.kt

Navigation setup between UsersListScreen and UserDetailScreen.

Depends On:

implementation(project(":core-model"))
implementation(project(":core-network"))
implementation(project(":feature-users"))


The app module is the launcher and orchestrator of the project — it initializes everything and serves as the main entry point.

---
 2️⃣ :core-model — Domain Models Layer

Location: core/model/

Responsibility:

Contains shared data models used across the app.

Pure Kotlin module (no Android dependencies).

Represents the domain layer entities.

Example:

data class User(
    val id: Int,
    val name: String,
    val company: String,
    val email: String,
    val photo: String
)


The core-model module defines the structure of your data — reusable, lightweight, and framework-independent.

🟨 3️⃣ :core-network — Data Source Layer

Location: core/network/

Responsibility:

Handles all networking using Retrofit and OkHttp.

Defines API interfaces and manages HTTP requests and JSON parsing.

Example:

interface UsersApi {
    @GET("users")
    suspend fun getUsers(): List<User>
}


Features:

Uses Retrofit for API calls.

Adds interceptors and error handling.

Parses JSON via GsonConverterFactory.

The core-network module knows how to fetch data from APIs, but it doesn’t know who will use that data.

🟧 4️⃣ :feature-users — Feature Layer

Location: feature/users/

Responsibility:

Implements the entire Users feature:

Use Cases (domain logic)

Repository (data abstraction)

ViewModel (state management)

Compose UI (List + Detail)

Internal Structure:

feature/users/
├── data/
│   ├── repository/
│   │   └── UsersRepositoryImpl.kt
│   └── UsersRepository.kt
├── domain/
│   └── usecase/
│       └── GetUsersUseCase.kt
├── presentation/
│   ├── UsersViewModel.kt
│   ├── UsersListScreen.kt
│   └── UserDetailScreen.kt
└── di/
    └── UsersModule.kt (optional)


Example Repository:

class UsersRepositoryImpl(
    private val api: UsersApi
) : UsersRepository {
    override suspend fun getUsers(): List<User> = api.getUsers()
}


The feature-users module contains the complete Users feature flow — from API call to displaying data in the UI.

### Data Flow Diagram
┌──────────────────────┐
│        :app          │
│  (Entry Point)       │
└──────────┬───────────┘
           │
           ▼
┌──────────────────────┐
│   :feature-users     │
│ (UI + VM + UseCases) │
└──────────┬───────────┘
           │
           ▼
┌──────────────────────┐
│    :core-network     │
│ (API + Retrofit)     │
└──────────┬───────────┘
           │
           ▼
┌──────────────────────┐
│     :core-model      │
│  (Data Classes)      │
└──────────────────────┘


