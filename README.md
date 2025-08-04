# Schulbegleiterin (School Companion) - Android App

A comprehensive Android application designed to support Schulbegleiterinnen (School Companions) in Germany in their daily work with children who have special needs.

## Features

### 🔐 Authentication
- **Login/Registration**: Secure user authentication with email and password
- **User Profiles**: Complete user profile management
- **Role-based Access**: Support for different user roles

### 📊 Dashboard
- **Overview**: Welcome screen with key information
- **Quick Actions**: Easy access to main functions
- **Assigned Children**: List of children under user's care
- **Upcoming Events**: Calendar preview of important events

### 👥 Children Management
- **Child List**: Complete list of assigned children
- **Child Details**: Comprehensive child information with tabbed interface:
  - **Events**: All past and upcoming events for the child
  - **Monitoring**: Tracked parameters (mood, behavior, learning progress)
  - **Notes**: Free-form notes per day or week
  - **Special Needs**: Individual education plans, diagnoses, support needs
  - **Goals**: Personal development goals with progress tracking
  - **Photos**: Gallery view of uploaded images

### 📅 Events Management
- **Event List**: Full list of all events related to children
- **Event Details**: Comprehensive event information
- **Sort/Filter**: Options to filter by date, child, priority
- **Event Creation**: Add new events with priority levels

### 📈 Monitoring & Assessment
- **Data Entry**: Interface for current monitoring parameters
- **Parameters Tracked**:
  - Emotional state (Very Happy to Very Sad)
  - Behavior patterns (Excellent to Difficult)
  - Learning progress (Excellent to Struggling)
  - Social interaction (Excellent to Difficult)
- **Notes**: Add detailed notes per parameter
- **Historical Data**: View trends over time

### 📝 Notes System
- **Note Management**: Create, edit, and delete notes
- **Categories**: Organize notes by type (Behavior, Academic, Social, etc.)
- **Child-specific**: Notes linked to specific children
- **Date Tracking**: Automatic date stamping

### 📊 Statistics & Analytics
- **Visual Charts**: Graphs and charts for collected data
- **Filters**: Filter by child, date range, parameter
- **Progress Tracking**: Monitor child development over time
- **Work Hours**: Track and visualize work time

### 👤 Profile & Work Tracking
- **User Profile**: View and edit personal information
- **Work Time Tracking**:
  - Start/stop work sessions
  - Track total work hours
  - Session history
- **Settings**: App preferences and configuration

### ⚙️ Settings
- **App Preferences**: Language, notifications, theme
- **User Settings**: Personal app configuration
- **About**: App information and version details

## Technical Architecture

### 🏗️ Architecture Pattern
- **MVVM (Model-View-ViewModel)**: Clean separation of concerns
- **Repository Pattern**: Centralized data access
- **Dependency Injection**: Using Hilt for dependency management

### 📱 UI Framework
- **Jetpack Compose**: Modern declarative UI toolkit
- **Material Design 3**: Latest Material Design guidelines
- **Navigation Compose**: Type-safe navigation

### 🌐 Network Layer
- **Retrofit**: HTTP client for API communication
- **RESTful API**: Standard REST endpoints
- **JSON Serialization**: Gson for data parsing

### 💾 Data Management
- **Room Database**: Local data caching (optional)
- **DataStore**: Preferences storage
- **Flow**: Reactive data streams

### 🔧 Key Technologies
- **Kotlin**: Primary programming language
- **Coroutines**: Asynchronous programming
- **Hilt**: Dependency injection
- **Navigation Compose**: Navigation framework
- **Material 3**: UI components
- **Coil**: Image loading

## Project Structure

```
app/src/main/java/com/example/school_companion/
├── data/
│   ├── api/           # API service interfaces
│   ├── model/         # Data models
│   └── repository/    # Repository classes
├── di/                # Dependency injection modules
├── ui/
│   ├── navigation/    # Navigation components
│   ├── screens/       # UI screens
│   │   ├── auth/      # Authentication screens
│   │   ├── children/  # Children management
│   │   ├── dashboard/ # Main dashboard
│   │   ├── events/    # Events management
│   │   ├── monitoring/# Monitoring screens
│   │   ├── notes/     # Notes management
│   │   ├── profile/   # User profile
│   │   ├── settings/  # App settings
│   │   └── statistics/# Statistics and analytics
│   └── viewmodel/     # ViewModels
└── SchoolCompanionApplication.kt
```

## Setup & Installation

### Prerequisites
- Android Studio Arctic Fox or later
- Android SDK 24+ (API level 24)
- Kotlin 1.9.0+
- JDK 8 or later

### Installation Steps

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd School-companion
   ```

2. **Open in Android Studio**
   - Open Android Studio
   - Select "Open an existing project"
   - Navigate to the project directory and select it

3. **Configure API Base URL**
   - Open `app/src/main/java/com/example/school_companion/di/NetworkModule.kt`
   - Update the `baseUrl` in the `provideRetrofit` function with your backend API URL

4. **Build and Run**
   - Connect an Android device or start an emulator
   - Click "Run" in Android Studio
   - The app will install and launch on your device

### Configuration

#### API Configuration
Update the base URL in `NetworkModule.kt`:
```kotlin
.baseUrl("https://your-api-domain.com/")
```

#### Build Configuration
The app is configured for:
- **Minimum SDK**: 24 (Android 7.0)
- **Target SDK**: 34 (Android 14)
- **Compile SDK**: 34

## API Integration

The app expects a RESTful API with the following endpoints:

### Authentication
- `POST /auth/login` - User login
- `POST /auth/register` - User registration
- `POST /auth/logout` - User logout

### User Management
- `GET /user/profile` - Get user profile
- `PUT /user/profile` - Update user profile

### Children
- `GET /children` - Get all children
- `GET /children/{id}` - Get specific child

### Events
- `GET /events` - Get events (with optional filters)
- `POST /events` - Create event
- `PUT /events/{id}` - Update event
- `DELETE /events/{id}` - Delete event

### Monitoring
- `GET /monitoring` - Get monitoring data
- `POST /monitoring` - Create monitoring entry
- `PUT /monitoring/{id}` - Update monitoring entry

### Notes
- `GET /notes` - Get notes
- `POST /notes` - Create note
- `PUT /notes/{id}` - Update note
- `DELETE /notes/{id}` - Delete note

### Work Sessions
- `GET /work-sessions` - Get work sessions
- `POST /work-sessions/start` - Start work session
- `PUT /work-sessions/{id}/end` - End work session

### Statistics
- `GET /statistics` - Get statistics data

## Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add some amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## License

This project is licensed under the MIT License - see the LICENSE file for details.

## Support

For support and questions:
- Create an issue in the GitHub repository
- Contact the development team
- Check the documentation

## Version History

- **v1.0.0** - Initial release with core functionality
  - Authentication system
  - Dashboard with navigation
  - Children management
  - Basic monitoring and notes
  - Work time tracking
  - Settings and profile management

## Future Enhancements

- [ ] Offline mode with local data sync
- [ ] Push notifications for events and reminders
- [ ] Advanced analytics and reporting
- [ ] Multi-language support
- [ ] Dark mode theme
- [ ] Image upload and gallery management
- [ ] Export functionality for reports
- [ ] Integration with school management systems 