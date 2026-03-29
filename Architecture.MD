app/src/main/java/com/example/movielog/

├── core/                                     # Shared, feature-independent
│   ├── common/
│   │     └── Resource.kt
│   │
│   ├── ui/
│   │   ├── components/
│   │   └── theme/
│   │
│   ├── utils/
│   │     ├── Constants.kt
│   │     └── Extensions.kt
│   │
│   └── navigation/
│         ├── AppNavGraph.kt
│         └── Routes.kt
│
├── features/
│
│   ├── auth/                                 # 🔐 Authentication
│   │   ├── data/
│   │   │   ├── remote/
│   │   │   │     └── FirebaseAuthDataSource.kt
│   │   │   │
│   │   │   ├── repository/
│   │   │   │     └── AuthRepositoryImpl.kt
│   │   │   │
│   │   │   └── mapper/
│   │   │         └── AuthMapper.kt
│   │   │
│   │   ├── domain/
│   │   │   ├── model/
│   │   │   │     └── User.kt
│   │   │   │
│   │   │   └── repository/
│   │   │         └── AuthRepository.kt
│   │   │
│   │   └── presentation/
│   │       ├── viewmodel/
│   │       │     └── AuthViewModel.kt
│   │       │
│   │       ├── screen/
│   │       │     ├── LoginScreen.kt
│   │       │     └── SignupScreen.kt
│   │       │
│   │       └── state/
│   │             └── AuthState.kt
│
│   ├── search/                               # 🔍 Search + External APIs
│   │   ├── data/
│   │   │   ├── remote/
│   │   │   │     ├── TmdbApiService.kt
│   │   │   │     ├── JikanApiService.kt
│   │   │   │     └── SearchRemoteDataSource.kt
│   │   │   │
│   │   │   ├── repository/
│   │   │   │     └── SearchRepositoryImpl.kt
│   │   │   │
│   │   │   └── mapper/
│   │   │         └── ContentMapper.kt
│   │   │
│   │   ├── domain/
│   │   │   ├── model/
│   │   │   │     ├── Content.kt
│   │   │   │     ├── ContentType.kt
│   │   │   │     └── ContentSnapshot.kt
│   │   │   │
│   │   │   └── repository/
│   │   │         └── SearchRepository.kt
│   │   │
│   │   └── presentation/
│   │       ├── viewmodel/
│   │       │     └── SearchViewModel.kt
│   │       │
│   │       ├── screen/
│   │       │     └── SearchScreen.kt
│   │       │
│   │       └── state/
│   │             └── SearchState.kt
│
│   ├── library/                              # 📚 User’s tracked content (MAIN FEATURE)
│   │   ├── data/
│   │   │   ├── remote/
│   │   │   │     └── FirestoreLibraryDataSource.kt
│   │   │   │
│   │   │   ├── repository/
│   │   │   │     └── LibraryRepositoryImpl.kt
│   │   │   │
│   │   │   └── mapper/
│   │   │         └── LibraryMapper.kt
│   │   │
│   │   ├── domain/
│   │   │   ├── model/
│   │   │   │     ├── UserContent.kt
│   │   │   │     ├── SeasonProgress.kt
│   │   │   │     ├── EpisodeProgress.kt
│   │   │   │     ├── Category.kt
│   │   │   │     └── WatchStatus.kt
│   │   │   │
│   │   │   └── repository/
│   │   │         └── LibraryRepository.kt
│   │   │
│   │   └── presentation/
│   │       ├── viewmodel/
│   │       │     └── LibraryViewModel.kt
│   │       │
│   │       ├── screen/
│   │       │     ├── HomeScreen.kt                # Categories (ongoing, completed, etc.)
│   │       │     ├── ContentDetailScreen.kt       # Detail + progress
│   │       │     └── AddContentScreen.kt          # Add from search
│   │       │
│   │       └── state/
│   │             └── LibraryState.kt
│
└── MainActivity.kt