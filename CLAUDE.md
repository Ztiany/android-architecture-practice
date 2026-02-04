# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Context

This is an **internationalized** project with comprehensive English documentation. The codebase demonstrates enterprise-grade Android architecture practices using Clean Architecture with modularization.

**Documentation Language**: All documentation is in English for international accessibility.
**Terminology**: Project uses standardized terminology defined in `document/glossary.md`.

## Build Commands

### Standard Build
```bash
# Build debug APK
./gradlew assembleDebug

# Build release APK
./gradlew assembleRelease

# Clean build
./gradlew clean

# Run lint checks
./gradlew lint

# Run unit tests
./gradlew test

# Run instrumented tests (requires connected device/emulator)
./gradlew connectedAndroidTest
```

### Module Publishing
```bash
# Publish all base modules to MavenLocal (for module substitution)
./gradlew publishAllBaseProjectToMavenLocal
```

### Gradle Daemon Issues
If build fails, try:
```bash
# Stop daemon and retry
./gradlew --stop
./gradlew assembleDebug
```

**Important**: If build fails after pulling changes, set `forceSubstitution = true` in `settings.gradle.kts` (line 45).

## Terminology and Communication

This project uses standardized terminology to ensure clear communication:
- **StateD**: Generic state wrapper (Loading/Error/Success)
- **ServiceContext**: Retrofit wrapper with error handling
- **StorageManager**: Data persistence abstraction
- **API-First Design**: Features expose interfaces before implementations

For complete terminology reference, see `document/glossary.md`. This glossary defines architectural terms, project-specific concepts, module structure, technical concepts, and acronyms used throughout the project.

## Architecture Overview

This is an **enterprise-grade Android architecture practice project** demonstrating Clean Architecture with modularization. The codebase is organized into three main layers:

### Module Structure

```
android-architecture-practice/
├── app/                    # Application shell (minimal code)
├── base/                   # Business-independent foundation modules
│   ├── activity/          # Base Activity classes
│   ├── adapter/           # Adapter implementations
│   ├── core/              # Core utilities
│   ├── fragment/          # Base Fragment classes (core UI framework)
│   ├── utils/             # General utilities
│   ├── view/              # Custom views (ShapeableView, DView)
│   └── viewbinding/       # ViewBinding utilities
├── common/                # Business-common modules
│   ├── api/               # Common API interfaces
│   ├── core/              # Business core logic
│   ├── http/              # HTTP client & networking (Retrofit/OkHttp)
│   ├── ui-dialog/         # Dialog components
│   ├── ui-theme/          # Theme resources
│   ├── ui-widget/         # UI widgets
│   └── ui-compose/        # Compose UI components
├── component/             # Reusable components
│   ├── upgrade/           # App upgrade functionality
│   ├── selector/          # Media/file selector
│   └── uitask/            # UI task manager (global UI task management)
└── feature/               # Business feature modules
    ├── account/           # Account management (api + main)
    ├── home/              # Home feature (api + main + app)
    └── browser/           # Browser feature
```

### Feature Module Pattern

Each feature follows the **API-First Design** pattern:

- **`feature-name/api/`**: Exposes interfaces and capabilities that other features depend on
- **`feature-name/main/`**: Core implementation of the feature
- **`feature-name/app/`** (optional): Feature-specific app module

Features depend on each other through API modules only, not implementations. This enables loose coupling and independent testing.

### Architectural Layers

**1. Presentation Layer (MVVM)**
- ViewModels with Hilt dependency injection
- Fragments/Activities with lifecycle-aware state handling
- State management using `StateD` (Loading/Error/Success states)
- LiveData or Flow for reactive UI updates

**2. Domain Layer**
- Repository pattern for data access abstraction
- Use cases for business logic encapsulation

**3. Data Layer**
- Retrofit + OkHttp for HTTP networking
- Room for local persistence
- StorageManager for preferences/data caching (supports SharedPreferences/MMKV)

### Dependency Injection (Hilt)

The entire project uses Hilt for DI:
- Presentation: Fragments/Activities → ViewModels/Repositories
- Inter-module: Features depend on APIs through interfaces
- Global DI container manages object lifecycles

See `README.md` and `document/glossary.md` for detailed explanations of architecture patterns and terminology.

## Key Technical Patterns

### Network Request Pattern

APIs are defined using Retrofit with `ServiceContext` wrapper:

```kotlin
interface AccountApi {
    @POST("novaAccountLogin")
    suspend fun pwdLogin(@Body loginRequest: LoginRequest): ApiResult<LoginResponse>
}
```

In Repository:
```kotlin
suspend fun pwdLogin(account: String, password: String): User {
    val loginResponse = withContext(dispatcherProvider.io()) {
        accountApi.executeApiCall { pwdLogin(loginRequest) }
    }
    // Process response...
}
```

**ServiceContext methods**:
- `executeApiCall { }`: Direct result, throws on failure
- `apiCall { }`: Returns `CallResult` for handling success/error

### State Management Pattern

Using `StateD` for UI states:

```kotlin
// In ViewModel
private val _loginState = MutableLiveData<StateD<User>>()
val loginState: LiveData<StateD<User>> = _loginState

fun login() {
    _loginState.setLoading()
    viewModelScope.launch {
        try {
            val user = repository.login()
            _loginState.setData(user)
        } catch (e: Exception) {
            _loginState.setError(e)
        }
    }
}

// In Fragment
handleLiveData(viewModel.loginState) {
    onData { user -> /* success */ }
}
```

### Base Fragment Classes

- **`BaseUIFragment`**: Basic UI with toast/dialog support
- **`BaseStateFragment`**: Multi-state layout support (loading/empty/error)
  - Requires `SimpleMultiStateLayout` with id `base_state_layout`
  - Optional `ScrollChildSwipeRefreshLayout` with id `base_refresh_layout`
- **`BaseListFragment`**: List handling capabilities

### Storage System

Using `StorageManager` for data persistence:

```kotlin
private val userStorage = storageManager.newStorage("storage_id")

// Store
userStorage.putEntity("key", userEntity)

// Retrieve
val user = userStorage.getEntity("key", User::class.java)
```

**Implementation**: Uses SharedPreferences by default. MMKV available but not recommended for critical user data due to lack of backup mechanism.

## Build System

### Gradle Configuration

- **AGP**: 8.3.2
- **Kotlin**: 1.9.24
- **JDK**: 11
- **Version Catalog**: `gradle/libs.versions.toml`

### Custom Gradle Plugins

Located in `plugins/convention/src/main/kotlin/com/android/app/build/`:

- `ModularizationApplicationPlugin`: For application modules
- `ModularizationLibraryPlugin`: For library modules
- `ModularizationAPIPlugin`: For API modules (feature interfaces)
- `CommonLibraryPlugin`: For common library setup
- `ComposeFeaturePlugin`: For Compose feature modules
- `FinalApplicationPlugin`: For final app configuration

### Module Substitution System

The project supports switching between local and remote modules via `settings.project.json`:

- Controlled by `DependencySubstitutionPlugin` in `settings.gradle.kts`
- `forceSubstitution = true`: Forces use of local modules
- `useLocal: true`: Per-module control
- Enables development with local base modules while using published artifacts for others

### Resource Prefixes

Each module has a unique resource prefix (configured in build.gradle.kts). All resources within a module must start with that prefix.

Example: `home_` for home feature, `account_` for account feature.

## Development Workflow

### Adding a New Feature

Use the feature generator script (Windows only):

```bash
cd scripts
./feature_generator.exe feature_name="new_feature" target_path="../feature/new_feature/"
```

Or manually create the structure following existing features:
1. Create `feature/new_feature/api/` for interfaces
2. Create `feature/new_feature/main/` for implementation
3. Add includes to `settings.gradle.kts`

### Module Dependencies

**Golden Rule**: Features should only depend on:
- `base/*` modules
- `common/*` modules
- Other feature `:feature/*/api` modules (never `main`)

**Never** depend directly on another feature's implementation.

## UI Components

### Custom Drawable System

The project extends [android-drawable-view](https://github.com/Ztiany/android-drawable-view) to create drawables via XML attributes instead of drawable XML files:

- **ShapeableView**: Basic shapes (no gradient)
- **DView**: Advanced shapes with gradient support

This reduces the need for drawable XML resources.

### Dialog Usage

```kotlin
showConfirmDialog {
    message = "Confirm message"
    negativeText = "Cancel"
    positiveText = "Confirm"
    positiveListener = { /* handle confirm */ }
    cancelableTouchOutside = false
}
```

## Coding Standards

From `document/coding-standards.md`:

1. **Format code** before committing
2. **Minimize visibility**: private → internal → public
3. **Module-private code** stays within the module; only truly shared code goes to common modules
4. **Resource placement**:
   - Images in `drawable/` (not `mipmap/`, except launcher icons)
   - Use xxhdpi for icons
   - Module-private resources in the module, common resources in common modules
5. **All resources** (including Styles) must use the module's resource prefix

**Additional Guidelines**:
- Naming conventions: PascalCase for classes, camelCase for functions/variables
- Follow Android Studio settings for auto-import and line length
- Use conventional commit messages: `feat:`, `fix:`, `docs:`, `refactor:`, `test:`, `chore:`

See `document/coding-standards.md` for complete Android Studio configuration and development guidelines.

## Testing

Test structure follows standard Android project layout:
- Unit tests: `src/test/`
- Instrumented tests: `src/androidTest/`

See `sample/tradition` and `sample/compose` modules for feature implementation examples.

## Development Tools

The project includes debug tools:
- **LeakCanary**: Memory leak detection
- **Stetho**: Network debugging
- **DoraemonKit**: Development toolkit
- **Radiography**: UI inspection tool

## Important Files

### Configuration Files
- `settings.gradle.kts`: Module declarations and dependency substitution
- `settings.project.json`: Module substitution configuration
- `gradle/libs.versions.toml`: Centralized dependency versions
- `build.gradle.kts`: Root build configuration with forced dependency versions

### Documentation Files
- `README.md`: Comprehensive project overview with architecture, quick start, and usage guides
- `document/coding-standards.md`: English coding standards and Android Studio configuration
- `document/glossary.md`: Project terminology and definitions for consistent communication
- `CLAUDE.md`: This file - provides context for AI assistants working on this project

### Reference Implementations
- `sample/tradition`: View-based implementation examples
- `sample/compose`: Compose-based implementation examples

## Project Resources

For detailed information on:
- **Architecture and patterns**: See README.md sections on Architecture Overview and Core Capabilities
- **Coding conventions**: See document/coding-standards.md
- **Terminology**: See document/glossary.md
- **Quick reference**: This CLAUDE.md file provides a condensed overview for AI assistants
