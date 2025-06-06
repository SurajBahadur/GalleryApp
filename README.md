
# 📸 GalleryApp

**GalleryApp** is an Android application that displays device media—images and videos—grouped by folders. Users can toggle between grid and list views for a seamless media browsing experience.

Built with a modern Android architecture using **MVVM**, **Hilt**, **Coroutines**, and **ViewBinding**.

---

## 🚀 Features

- Display albums grouped by folders (Camera, WhatsApp, etc.)
- Grid/List toggle support for viewing albums
- Swipe-to-refresh
- Detail screen for album contents
- Storage permission handling for Android 13+

---

## 📁 Project Structure

```
com.suraj.gallery/
│
├── data/                         # Data layer
│   ├── local/                    # Local media querying and helpers
│   ├── model/                    # Data models like Album, MediaItem
│   └── repository/               # Implementation of data fetching logic
│
├── di/                           # Hilt dependency injection modules
│   ├── AppModule.kt              # Provides application-level dependencies
│   ├── RepositoryModule.kt      # Binds interfaces to repository implementations
│   └── UseCaseModule.kt         # Provides domain use cases
│
├── domain/                       # Domain layer
│   ├── model/                    # Domain-level models (same or mapped from data layer)
│   ├── repository/              # Repository interfaces (used by use cases)
│   └── usecase/                 # Business logic like GetAlbumsUseCase
│
├── presentation/ui/             # UI layer
│   ├── albums/                  # AlbumsActivity, ViewModel, Adapter
│   ├── detail/                  # AlbumDetailActivity to show images/videos in a folder
│   └── common/                  # BaseActivity, ViewState, shared UI components
│
├── utils/                       # Utility classes (ImageLoader, PermissionHelper)
│
├── GalleryApplication.kt        # Custom Application class with Hilt setup
├── res/                         # XML layout resources, drawables, etc.
└── AndroidManifest.xml          # App manifest with declared permissions and activities
```

---

## 🧰 Tech Stack

- **Language:** Kotlin
- **Architecture:** MVVM (Model-View-ViewModel)
- **Dependency Injection:** Hilt
- **Image Loading:** Glide
- **Concurrency:** Kotlin Coroutines + Flow
- **Android Components:** ViewModel, LiveData/StateFlow, RecyclerView, ViewBinding

---

## 🛠 How to Use

### 1. Clone the Repository

```bash
git clone https://github.com/SurajBahadur/GalleryApp.git
cd GalleryApp
```

### 2. Open in Android Studio

- Open the project directory in Android Studio.
- Let Gradle sync and resolve dependencies.

### 3. Run the App

- Connect an emulator or Android device with API 23+.
- Hit **Run** ▶️ in Android Studio.

> 🛡️ On Android 13+ (API 33+), it will request **photo/media permissions** using the updated `READ_MEDIA_IMAGES` and `READ_MEDIA_VIDEO`.
