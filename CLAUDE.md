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

- **UI Framework**: Jetpack Compose (declarative UI, no XML layouts)
- **Theme System**: Material 3 with dynamic color support (Android 12+)
- **Package**: `com.example.counter2magic`

### Source Structure

```
app/src/main/java/com/example/counter2magic/
├── MainActivity.kt          # Entry point, Compose setup
└── ui/theme/
    ├── Color.kt             # Color definitions
    ├── Theme.kt             # Light/dark theme configuration
    └── Type.kt              # Typography
```

## Configuration

- **Min SDK**: 30 (Android 11)
- **Target SDK**: 35 (Android 15)
- **Java/Kotlin**: Java 11 compatibility
- **Dependencies**: Managed via `gradle/libs.versions.toml`
