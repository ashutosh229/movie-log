# MovieLog

MovieLog is a modern Android application for tracking movies, TV series, and anime in a single personal library. It is built with Jetpack Compose and follows a feature-oriented Clean Architecture + MVVM structure, with Firebase handling authentication and user-scoped persistence.

The app is designed around a simple idea: discovery and tracking should feel fast, clear, and personal. Users can search across multiple content sources, add titles to their own library, track progress for both films and episodic content, and manage account-level settings such as theme and password recovery.

## Overview

MovieLog helps users:

- search movies and TV shows through TMDB
- search anime through Jikan
- save titles into a private library tied to their own account
- organize content by watch status
- track progress for movies and episodic content
- manage account details, theme preferences, password resets, and logout/account deletion flows

## Key Features

### Authentication

- Email/password sign up and login with Firebase Authentication
- Persistent authenticated sessions
- Forgot password flow from the login screen
- Password reset flow from the in-app account settings
- Logout confirmation flow
- Account deletion flow with re-authentication and data cleanup

### Search

- Unified discovery flow for:
- movies
- TV series
- anime
- TMDB-backed search for movies and TV
- Jikan-backed search for anime
- Search results can be added directly into the user library

### Personal Library

- User-scoped Firestore library storage
- Watch status support:
- Ongoing
- To Watch
- Completed
- Repository
- Progress tracking support:
- movies by timestamp
- series/anime by season, episode, and timestamp
- Update and delete actions for saved entries

### Account and Settings

- Account hub with menu-based navigation
- Profile section for viewing basic account information and updating the display name
- Settings section for:
- dark mode
- password change via reset email
- account deletion
- Theme preference persisted locally across app restarts

### UI

- Jetpack Compose UI with Material 3
- Bottom navigation based app shell
- Feature-oriented reusable component structure
- Light and dark theme support
- Scalable account/settings layout for future additions

## Tech Stack

| Area | Technology |
|---|---|
| Language | Kotlin |
| UI | Jetpack Compose, Material 3 |
| State | StateFlow, ViewModel |
| Navigation | Navigation Compose |
| Backend | Firebase Authentication, Cloud Firestore |
| Networking | Retrofit, OkHttp |
| Image Loading | Coil |
| Async | Kotlin Coroutines |

## Architecture

This project follows a feature-first structure with Clean Architecture and MVVM principles.

Core design choices:

- presentation logic lives in feature-specific screens, state models, and view models
- domain contracts define repositories and business-facing models
- data layers implement repositories and connect to Firebase or external APIs
- shared concerns such as theming, navigation, auth helpers, and reusable UI live under `core`

Typical flow:

```text
UI -> ViewModel -> Repository -> Remote Data Source -> Firebase / External API
```

This separation keeps the app easier to test, scale, and extend as new features are added.

## Project Structure

```text
app/src/main/java/com/example/movielog/
|
|-- core/
|   |-- auth/
|   |-- common/
|   |-- navigation/
|   |-- network/
|   |-- ui/
|   |   |-- components/
|   |   `-- theme/
|   `-- utils/
|
|-- features/
|   |-- auth/
|   |-- library/
|   |-- profile/
|   `-- search/
|
`-- MainActivity.kt
```

## Production-Oriented Notes

The repository already includes several patterns that move it toward a production-ready foundation:

- user data is isolated under `users/{uid}` in Firestore
- account-sensitive flows such as deletion require explicit confirmation
- password reset is handled through email-based Firebase flows instead of direct client-side password mutation
- navigation is split into auth and main app graphs
- theme preference is persisted locally instead of staying only in memory

There are also still some areas that can be improved further for production hardening:

- move API keys and runtime config into a secure configuration strategy
- replace broad network logging with environment-aware logging rules
- add unit tests and UI tests for key flows
- improve offline behavior and caching
- add stronger Firestore field validation rules

## Getting Started

### Prerequisites

- Android Studio
- JDK 11+
- A Firebase project with Authentication and Firestore enabled
- A TMDB API key
- An Android device or emulator

### Firebase Setup

1. Create a Firebase project.
2. Enable Email/Password authentication.
3. Enable Cloud Firestore.
4. Add your Android app to Firebase.
5. Download `google-services.json`.
6. Place it inside:

```text
app/google-services.json
```

### Firestore Rules

MovieLog uses both the user profile document and the user library subcollection. A minimal rule set should allow users to access only their own documents.

Example:

```firestore
rules_version = '2';
service cloud.firestore {
  match /databases/{database}/documents {

    match /users/{userId} {
      allow read, write: if request.auth != null && request.auth.uid == userId;
    }

    match /users/{userId}/library/{docId} {
      allow read, write: if request.auth != null && request.auth.uid == userId;
    }
  }
}
```

### API Setup

The app uses:

- TMDB for movies and TV series
- Jikan for anime

The repository currently includes base URLs in the network layer and a `config.json` file under the app source tree, but TMDB requests still require a valid API key at runtime. If you plan to open source or deploy this project publicly, move secret handling to a safer configuration mechanism before release.

### Build and Run

1. Open the project in Android Studio.
2. Sync Gradle.
3. Make sure Firebase is configured correctly.
4. Run the app on an emulator or physical device.

If you prefer the command line:

```bash
./gradlew assembleDebug
```

On Windows:

```powershell
.\gradlew.bat assembleDebug
```

## Current App Modules

### Auth

Handles:

- registration
- login
- password recovery
- auth state observation

### Search

Handles:

- content discovery across external providers
- content normalization into app-facing models
- search result presentation

### Library

Handles:

- user content persistence
- watch status organization
- progress updates
- delete/update operations

### Profile

Handles:

- profile observation
- display name updates
- password reset from the signed-in account
- logout flow
- account deletion

## UX Highlights

- menu-style account screen designed for future expansion
- simplified user-facing profile details
- progress shown only where it provides value
- persistent theme selection across sessions
- confirmation dialogs for destructive or session-ending actions

## Roadmap

Planned or valuable future improvements include:

- richer content detail screens
- advanced filters and sorting
- analytics and watch insights
- recommendations
- offline caching with Room
- stronger loading/error instrumentation
- improved image-loading performance
- automated testing coverage

## Contributing

Contributions, suggestions, and improvements are welcome.

A good contribution flow:

1. Fork the repository
2. Create a feature branch
3. Make focused changes
4. Verify the app builds successfully
5. Open a pull request with a clear summary

## License

This project is licensed under the MIT License. See the [LICENSE](C:/Users/akuma/AndroidStudioProjects/MovieLog/LICENSE) file for details.

## Author

Ashutosh Kumar Jha

## Support

If you find the project useful, starring the repository is a simple way to support it.
