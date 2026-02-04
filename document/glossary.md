# Glossary

This glossary defines and standardizes terminology used throughout the android-architecture-practice project. Consistent terminology improves communication and understanding across the codebase and documentation.

## Table of Contents

- [Architectural Terms](#architectural-terms)
- [Project-Specific Terms](#project-specific-terms)
- [Module Structure Terms](#module-structure-terms)
- [Technical Concepts](#technical-concepts)
- [Acronyms and Abbreviations](#acronyms-and-abbreviations)

---

## Architectural Terms

### Clean Architecture

A software design philosophy that separates concerns into distinct layers with strict dependency rules. In this project:

- **Presentation Layer**: UI components (Activities, Fragments) and ViewModels
- **Domain Layer**: Business logic (Repositories, Use Cases)
- **Data Layer**: Data sources (APIs, databases, storage)

**Key Principle**: Dependencies point inward. The inner layers should not know anything about the outer layers.

### MVVM (Model-View-ViewModel)

A presentation layer architectural pattern:

- **Model**: Data layer (Repositories, data sources)
- **View**: UI components (Activities, Fragments) that observe ViewModel
- **ViewModel**: Manages UI state and business logic, survives configuration changes

**Usage in Project**: All features follow MVVM pattern with Hilt ViewModels and LiveData/Flow for state management.

### MVI (Model-View-Intent)

An alternative presentation layer pattern where:

- **Intent**: User actions/intentions
- **Model**: Immutable state representation
- **View**: Renders based on state

**Note**: This project primarily uses MVVM, but MVI can be adopted for complex state management scenarios.

### API-First Design

A design approach where feature interfaces are defined before implementations:

- Each feature has an `api` module defining public interfaces
- Other features depend only on `api` modules, not implementations
- Implementation resides in `main` module

**Benefits**: Loose coupling, independent testing, parallel development

---

## Project-Specific Terms

### StateD

A generic state wrapper class representing three possible states:

- **Loading**: Request in progress
- **Error**: Request failed with error information
- **Success**: Request succeeded (with optional data)

**Usage**: Used in ViewModels to emit UI state via LiveData or Flow.

**Example**:

```kotlin
private val _loginState = MutableLiveData<StateD<User>>()
```

### ServiceContext

A wrapper class around Retrofit Service interfaces that provides:

- Consistent error handling
- Retry mechanisms
- Request/response logging
- Result parsing (to `CallResult` or direct values)

**Methods**:

- `executeApiCall()`: Direct result, throws on failure
- `apiCall()`: Returns `CallResult` for explicit error handling

**Usage in Project**: All network APIs use ServiceContext wrapper.

### StorageManager

An abstraction layer for data persistence that supports:

- **SharedPreferences**: Default implementation
- **MMKV**: High-performance alternative (not recommended for critical user data)

**API**: Provides methods for storing primitives and objects (`putInt`, `putEntity`, etc.)

### Resource Prefix

A unique string identifier prefixed to all resource names within a module.

- **Purpose**: Prevent naming conflicts between modules
- **Example**: `home_` for home feature, `account_` for account feature
- **Applies to**: All resource types (strings, IDs, styles, drawables, etc.)

### BaseUIFragment

A base Fragment class providing:

- Toast message display
- Loading dialog management
- Dialog building APIs
- ViewBinding integration

**When to Use**: For simple screens without complex state management.

### BaseStateFragment

Extends `BaseUIFragment` with multi-state layout support:

- **Loading state**: Shows progress indicator
- **Empty state**: Shows empty view
- **Error state**: Shows error message with retry option
- **Content state**: Shows actual content

**Layout Requirement**: Must include `SimpleMultiStateLayout` with id `base_state_layout`.

### BaseListFragment

Extends `BaseStateFragment` with list-specific capabilities:

- Adapter management
- Pagination support
- Item click handling
- Scroll listeners

**When to Use**: For screens displaying lists of data.

---

## Module Structure Terms

### Base Modules

Business-independent foundation modules that provide core functionality:

- **activity**: Base Activity implementations
- **adapter**: Adapter implementations for RecyclerViews
- **core**: Core utilities and helpers
- **fragment**: Base Fragment classes
- **utils**: General utility functions
- **view**: Custom view implementations (ShapeableView, DView)
- **viewbinding**: ViewBinding utilities

**Characteristics**: No business logic, used by all other modules.

### Common Modules

Business-common modules with app-specific implementations:

- **api**: Common API interfaces and contracts
- **core**: Shared business logic
- **http**: HTTP client and networking (Retrofit/OkHttp setup)
- **ui-dialog**: Dialog components
- **ui-theme**: Theme and styling resources
- **ui-widget**: Reusable UI widgets
- **ui-compose**: Compose UI components

**Characteristics**: Shared across features but app-specific.

### Component Modules

Reusable cross-cutting components:

- **upgrade**: App update/upgrade functionality
- **selector**: Media/file selection
- **uitask**: Global UI task management

**Characteristics**: Independent features used by multiple business features.

### Feature Modules

Business-specific features implementing actual app functionality:

- **account**: User account management (login, profile)
- **home**: Home screen and navigation
- **browser**: In-app browser

**Structure**:

- **api/**: Public interfaces and capabilities
- **main/**: Core implementation (presentation, domain, data)
- **app/** (optional): Feature-specific app module

### Module Substitution

A development feature allowing switching between local and published module artifacts:

- **forceSubstitution**: Forces use of local modules
- **useLocal**: Per-module control
- **Purpose**: Enables development with local base modules while using remote artifacts for others

**Configuration**: `settings.gradle.kts` and `settings.project.json`

---

## Technical Concepts

### Dependency Injection (DI)

A design pattern where objects receive their dependencies from external sources rather than creating them.

**In This Project**: Implemented using **Hilt** (Android's recommended DI framework).

- ViewModels receive Repositories
- Repositories receive APIs and Storage
- Fragments receive ViewModels

**Benefits**: Loose coupling, easier testing, better modularity.

### Repository Pattern

A design pattern that abstracts data access logic.

- **Provides** clean API to data layer
- **Hides** data source complexity (API vs database)
- **Manages** data caching and synchronization

**In This Project**: Each feature has Repository classes that coordinate between APIs and local storage.

### Coroutines

Kotlin's approach to asynchronous programming:

- **Structured concurrency**: Coroutine scopes manage lifecycle
- **Dispatchers**: Control execution thread (IO, Main, Default)
- **Suspension**: Non-blocking async code that looks synchronous

**Usage in Project**:

- Network requests in Repository (with `dispatcherProvider.io()`)
- ViewModel launches coroutines in `viewModelScope`
- Flow collection in Fragments

### Flow

Kotlin's asynchronous data stream:

- **StateFlow**: State holder with current value (like LiveData)
- **SharedFlow**: Event stream (no initial value)
- **Flow**: Cold stream of values

**Usage in Project**: Alternative to LiveData for reactive state management.

### LiveData

Android's observable data holder class:

- **Lifecycle-aware**: Only updates active observers
- **No memory leaks**: Automatically stops observation when stopped
- **Configuration change survival**: Retains data across rotations

**Usage in Project**: Primary state management mechanism (alternative to Flow).

### Non-Transitive R Class

An Android Gradle Plugin feature (enabled by default in AGP 8.0+):

- Each module only generates resources it defines
- Accessing other module's resources requires fully qualified names
- **Example**: `R.string.home_title` instead of `R.string.title`

**Benefits**: Faster builds, smaller resource files, explicit dependencies.

### Module Namespace

The package name that defines a module's R class location (AGP 8.0+):

- **Specified in**: `build.gradle.kts` as `namespace`
- **Replaces**: `package` attribute in `AndroidManifest.xml`
- **Purpose**: Cleaner manifest files, explicit resource ownership

---

## Acronyms and Abbreviations

### AGP - Android Gradle Plugin

The build system plugin for Android projects. Current version: 8.3.2

### API - Application Programming Interface

Defined interfaces for component communication. In this project, feature APIs define capabilities.

### DI - Dependency Injection

See [Dependency Injection](#dependency-injection-di) above.

### DI Container

Object managing dependency creation and lifecycle. In this project: Hilt.

### HTTP - Hypertext Transfer Protocol

Protocol for network communication. Implemented via Retrofit + OkHttp.

### JSON - JavaScript Object Notation

Data format for API requests/responses.

### MMKV

High-performance key-value storage (alternative to SharedPreferences).

### MVVM

See [MVVM](#mvvm-model-view-viewmodel) above.

### ORM - Object-Relational Mapping

Mapping between database tables and Kotlin objects (not heavily used in this project).

### SDK - Software Development Kit

Set of tools for developing Android apps (Android SDK).

### UI - User Interface

Visual components users interact with (Activities, Fragments, Views).

### XML - eXtensible Markup Language

Markup language for layouts, resources, and manifests.

---

## Version Control Terms

### Main Branch

Primary development branch: `dev-example`

### Feature Branch

Temporary branch for feature development.

### Commit Message

Description of changes. Format: `type(scope): description`

### PR - Pull Request

Request to merge changes from one branch to another.

---

## Build System Terms

### Gradle

Build automation system used by Android projects.

### Version Catalog

Centralized dependency management in `gradle/libs.versions.toml`.

### Convention Plugin

Custom Gradle plugin for consistent module configuration.

### Worktree

Git feature allowing multiple working directories. Not used in this project.

---

## Testing Terms (Future)

### Unit Test

Test for individual functions/classes in isolation.

### Instrumented Test

Test requiring Android device/emulator (UI tests, integration tests).

### Test Coverage

Percentage of code executed by tests.

---

## Contributing

To suggest additions or modifications to this glossary:

1. Check if the term is already defined
2. Provide clear definition and usage context
3. Include code examples if applicable
4. Link to related terms

**Remember**: Consistent terminology makes the codebase more maintainable and the team more effective.
