# 🎬 MovieLog

**MovieLog** is a modern Android application built using **Jetpack Compose**, designed to help users
**track movies, TV series, and anime** efficiently. It provides a seamless experience for searching
content, managing personal watchlists, and tracking progress — all powered by a clean architecture
and scalable design.

---

## 🚀 Features

### 🔐 Authentication

- Firebase Authentication (Email & Password)
- Global auth state management
- Persistent login sessions

### 🔍 Search

- Search Movies, Series, and Anime
- Integrated with:
    - TMDB API (Movies & Series)
    - Jikan API (Anime)
- Clean and responsive UI

### 📚 Library Management

- Add content to library with status:
    - Ongoing
    - Completed
    - To Watch
    - Repository
- Update progress:
    - Movie → Timestamp
    - Series/Anime → Season, Episode, Timestamp
- Delete content
- User-specific data isolation (Firestore)

### 📊 Progress Tracking

- Track watch progress per content
- Supports both movies and episodic content

### 🧭 Navigation

- Bottom Navigation Bar with:
    - Home
    - Search
    - Library
    - Profile
- State-preserving navigation
- Auth-based navigation flow (AuthNavGraph + MainAppNavGraph)

### 🎨 UI/UX

- Built with **Material 3**
- Consistent design system
- Dark Mode support 🌙
- Clean, responsive layouts
- Modular reusable components

### 👤 Profile

- User session management
- Logout functionality
- Theme toggle (Light/Dark)

---

## 🏗️ Architecture

This project follows **Clean Architecture + MVVM** principles.

### 🔹 Key Patterns

- MVVM (Model-View-ViewModel)
- Repository Pattern
- StateFlow for reactive state management
- Single source of truth for UI state

---

## 🧱 Tech Stack

| Layer         | Technology                         |
|---------------|------------------------------------|
| UI            | Jetpack Compose, Material 3        |
| State         | StateFlow, ViewModel               |
| Backend       | Firebase Authentication, Firestore |
| Networking    | Retrofit (TMDB, Jikan APIs)        |
| Image Loading | Coil                               |
| Async         | Kotlin Coroutines                  |

---

## 🔄 Data Flow

UI → ViewModel → Repository → Remote Data Source → Firebase / APIs
↑
Mapper Layer

---

## 🧪 Future Enhancements

🔄 Real-time Firestore sync (snapshot listeners)
💾 Offline caching (Room DB)
🎬 Content Detail Screen
🔔 Notifications & reminders
📈 User analytics (watch stats)
🎨 Dynamic theming (Material You)
🔍 Advanced filters & sorting

--- 

## 🧠 Key Learnings

- Global auth state management with StateFlow
- Navigation architecture in Compose
- Firestore user-scoped data handling
- Clean separation of concerns
- Building scalable UI systems

--- 

## 🤝 Contributing

Contributions are welcome!

1. Fork the repo
2. Create a feature branch
3. Commit changes
4. Open a Pull Request

--- 

## 📜 License

This project is licensed under the MIT License.

---

## 👨‍💻 Author

Ashutosh Kumar Jha

---

## ⭐ Support

If you like this project, consider giving it a ⭐ on GitHub!

