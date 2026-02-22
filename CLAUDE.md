# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

Counter2Magic is an Android application built with Jetpack Compose and Material Design 3. It uses Kotlin DSL for Gradle configuration and a version catalog for dependency management.

## Build Commands

```bash
# Build the project
./gradlew build

# Build debug APK
./gradlew assembleDebug

# Install debug build on connected device
./gradlew installDebug

# Run all unit tests
./gradlew test

# Run a single unit test class
./gradlew testDebugUnitTest --tests "com.example.counter2magic.ExampleUnitTest"

# Run instrumented tests (requires device/emulator)
./gradlew connectedAndroidTest

# Clean build
./gradlew clean
```

## Architecture

### Overview

- **UI Framework**: Jetpack Compose (declarative UI, no XML layouts)
- **Theme System**: Material 3 with dynamic color support (Android 12+)
- **Architecture Pattern**: Clean Architecture with MVVM
- **Dependency Injection**: Hilt
- **Database**: Room with Flow-based DAOs
- **Package**: `com.example.counter2magic`

### Layers

The app follows Clean Architecture with three layers:

- **Data Layer**: Repositories, Room database, entities, DAOs, calendar integration
- **Domain Layer**: Business models, use cases, formatters
- **UI Layer**: Screens, components, ViewModels, navigation, theme

### Source Structure

```
app/src/main/java/com/example/counter2magic/
├── MainActivity.kt              # Entry point, Compose setup
├── Counter2MagicApplication.kt  # Hilt application class
├── data/
│   ├── local/db/
│   │   ├── CountdownDatabase.kt # Room database definition
│   │   ├── entity/
│   │   │   ├── EventEntity.kt   # Database entity for events
│   │   │   └── ReminderEntity.kt
│   │   └── dao/
│   │       ├── EventDao.kt      # Data access object for events
│   │       └── ReminderDao.kt
│   ├── repository/
│   │   ├── EventRepository.kt   # Event data operations
│   │   └── ReminderRepository.kt
│   └── calendar/
│       └── CalendarRepository.kt # Device calendar integration
├── domain/
│   ├── model/
│   │   ├── CountdownEvent.kt    # Core event domain model
│   │   ├── TimeRemaining.kt     # Time calculation model
│   │   └── Reminder.kt
│   ├── usecase/
│   │   ├── CalculateTimeRemainingUseCase.kt
│   │   └── SyncCalendarEventsUseCase.kt
│   └── formatter/
│       └── AdaptiveTimeFormatter.kt # Smart time formatting
├── ui/
│   ├── screens/
│   │   ├── home/
│   │   │   ├── HomeScreen.kt
│   │   │   └── HomeViewModel.kt
│   │   ├── addevent/
│   │   │   ├── AddEventScreen.kt
│   │   │   └── AddEventViewModel.kt
│   │   ├── eventdetail/
│   │   │   ├── EventDetailScreen.kt
│   │   │   └── EventDetailViewModel.kt
│   │   └── settings/
│   │       ├── SettingsScreen.kt
│   │       └── SettingsViewModel.kt
│   ├── components/
│   │   ├── CountdownDisplay.kt  # Animated countdown with flip digits
│   │   └── EventCard.kt         # Event card with urgency indicators
│   ├── navigation/
│   │   ├── AppNavHost.kt        # Navigation graph with transitions
│   │   └── NavRoutes.kt         # Route definitions
│   └── theme/
│       ├── Color.kt             # Color definitions
│       ├── Theme.kt             # Light/dark theme configuration
│       ├── Type.kt              # Typography
│       ├── Urgency.kt           # Urgency enum and colors
│       └── Spacing.kt           # Design tokens (spacing, elevation, radius)
├── notification/
│   ├── ReminderScheduler.kt     # Schedule notifications via WorkManager
│   ├── ReminderWorker.kt        # Background worker for reminders
│   └── BootReceiver.kt          # Reschedule reminders on device boot
├── widget/
│   ├── CountdownWidget.kt       # Glance widget implementation
│   └── CountdownWidgetReceiver.kt
└── di/
    └── DatabaseModule.kt        # Hilt module for database dependencies
```

## Design System

### Urgency System

Events are color-coded based on time remaining (`ui/theme/Urgency.kt`):

| Urgency    | Condition      | Visual Treatment                     |
|------------|----------------|--------------------------------------|
| `PAST`     | Event passed   | Muted gray, reduced opacity (0.6)    |
| `IMMINENT` | < 1 hour       | Red accent, pulsing dot, border glow |
| `TODAY`    | < 24 hours     | Amber accent                         |
| `SOON`     | < 7 days       | Blue accent (default)                |
| `UPCOMING` | > 7 days       | Muted gray                           |

### Design Tokens

Defined in `ui/theme/Spacing.kt`:

```kotlin
CountdownSpacing: xs=4dp, sm=8dp, md=16dp, lg=24dp, xl=32dp
CountdownElevation: card=1dp, cardPressed=4dp
CountdownRadius: sm=8dp, md=12dp, lg=16dp
```

### Animation Patterns

1. **Flip Clock Digits**: Countdown numbers animate with slide-in/slide-out transitions when values change
2. **Card Stagger**: Event cards animate in sequence when the list loads
3. **Screen Transitions**: Fade + horizontal slide (300ms enter, 200ms exit)
4. **Pulsing Urgency Dot**: Imminent events have an animated pulsing indicator (1000ms cycle)

## Key Components

### CountdownDisplay (`ui/components/CountdownDisplay.kt`)

Animated countdown display with multiple variants:
- `CountdownDisplay`: Primary/secondary value layout with animated digits
- `SegmentedCountdownDisplay`: Days/hours/minutes/seconds in separate boxes
- Sizes: `LARGE`, `MEDIUM`, `SMALL`, `COMPACT`
- Supports urgency color override

### EventCard (`ui/components/EventCard.kt`)

Event list item with urgency-aware styling:
- `EventCard`: Full card with countdown display
- `CompactEventCard`: Condensed horizontal layout
- `UrgencyDot`: Colored indicator (pulsing animation for IMMINENT)
- Calendar icon for synced events
- Border glow effect for imminent events

### Navigation (`ui/navigation/AppNavHost.kt`)

Compose Navigation with shared element-style transitions:
- Enter: fade + slide from right (1/3 screen width)
- Pop: fade + slide to right
- Routes: Home → AddEvent, EventDetail, Settings

## Configuration

- **Min SDK**: 30 (Android 11)
- **Target SDK**: 35 (Android 15)
- **Java/Kotlin**: Java 11 compatibility
- **Dependencies**: Managed via `gradle/libs.versions.toml`
