# Counter2Magic - Event Countdown App Plan

## Overview
Transform the existing Android Compose project into a countdown timer app that shows time remaining until events, with a home screen widget as the primary interface.

**Key Features:**
- Manual event entry + device calendar sync
- Home screen widget showing 2-3 upcoming countdowns (compact list)
- Smart adaptive time display (seconds when close, days when far)
- Configurable reminder notifications

**Visual Style:** Minimal clean - simple typography, white space, large numbers

---

## Architecture

```
com.example.counter2magic/
├── data/
│   ├── local/db/          # Room database (events, reminders)
│   ├── calendar/          # Calendar content resolver
│   └── repository/        # EventRepository
├── domain/
│   ├── model/             # CountdownEvent, TimeRemaining
│   ├── usecase/           # Business logic
│   └── formatter/         # AdaptiveTimeFormatter
├── ui/
│   ├── screens/           # Home, AddEvent, Settings, EventDetail
│   ├── components/        # EventCard, CountdownDisplay
│   ├── navigation/        # Compose Navigation
│   └── theme/             # Updated minimal theme
├── widget/                # Glance widget
└── notification/          # WorkManager reminders
```

---

## Implementation Phases

### Phase 1: Core Data Layer ❌
1. Add Room database with `EventEntity` and `ReminderEntity`
2. Create domain models: `CountdownEvent`, `TimeRemaining`
3. Implement `AdaptiveTimeFormatter` for smart time display:
   - < 1 min: show seconds
   - < 1 hour: show minutes + seconds
   - < 1 day: show hours + minutes
   - < 7 days: show days + hours
   - Otherwise: weeks/months
4. Create `EventRepository` with CRUD operations
5. **Unit tests for all domain logic and formatter**

### Phase 2: UI Layer ❌
1. Update theme for minimal design (light typography, muted colors)
2. Set up Navigation component with 4 screens
3. **HomeScreen**: List of countdown cards with FAB to add
4. **AddEventScreen**: Title, date picker, time picker
5. **EventDetailScreen**: Edit event, configure reminders
6. **EventCard component**: Large number + unit, event title
7. **Unit tests for ViewModels**

### Phase 3: Home Screen Widget (Glance) ❌
1. `CountdownWidget` using Glance (Compose-based widgets)
2. Compact list showing 2-3 upcoming events
3. Each row: event title + countdown
4. Click action opens app to event detail
5. `WidgetUpdateWorker` for periodic refresh (15 min)
6. **Unit tests for widget data logic**

### Phase 4: Notifications ❌
1. `ReminderWorker` with WorkManager for scheduled notifications
2. `NotificationHelper` with dedicated channel
3. `ReminderScheduler` to queue/cancel reminders
4. Default reminder options: 1 day, 1 hour, 30 min, 15 min before
5. **Unit tests for reminder scheduling logic**

### Phase 5: Calendar Integration ⏸️ (PAUSED)
1. `CalendarRepository` to read device calendar events
2. `SyncCalendarEventsUseCase` to import/update events
3. Permission handling for `READ_CALENDAR`
4. Settings toggle to enable/disable sync

---

## Dependencies to Add

```toml
# libs.versions.toml additions
room = "2.6.1"
navigation = "2.7.7"
glance = "1.1.0"
workManager = "2.9.0"
hilt = "2.51"
```

---

## Permissions Required

```xml
<uses-permission android:name="android.permission.POST_NOTIFICATIONS" />
<uses-permission android:name="android.permission.RECEIVE_BOOT_COMPLETED" />
```

---

## Key Files to Modify/Create

**Modify:**
- `app/build.gradle.kts` - Add dependencies
- `gradle/libs.versions.toml` - Version catalog
- `AndroidManifest.xml` - Permissions, widget receiver
- `MainActivity.kt` - Navigation host setup
- `ui/theme/*` - Minimal design updates

**Create:**
- `data/local/db/CountdownDatabase.kt`
- `data/local/db/entity/EventEntity.kt`
- `data/local/db/dao/EventDao.kt`
- `domain/model/CountdownEvent.kt`
- `domain/model/TimeRemaining.kt`
- `domain/formatter/AdaptiveTimeFormatter.kt`
- `ui/screens/home/HomeScreen.kt`
- `ui/screens/addevent/AddEventScreen.kt`
- `ui/screens/settings/SettingsScreen.kt`
- `ui/components/EventCard.kt`
- `ui/components/CountdownDisplay.kt`
- `widget/CountdownWidget.kt`
- `widget/CountdownWidgetReceiver.kt`
- `notification/ReminderWorker.kt`
- `notification/ReminderScheduler.kt`
- `res/xml/countdown_widget_info.xml`

---

## Verification Plan

1. **Unit Tests:**
   - `AdaptiveTimeFormatter` returns correct format for various durations
   - `CalculateTimeRemainingUseCase` handles edge cases (past events, timezone)
   - `ReminderScheduler` calculates correct delay times
   - ViewModels emit correct UI states

2. **Manual Testing:**
   - Create manual event → appears in list with live countdown
   - Add widget to home screen → shows events, updates periodically
   - Set reminder → notification fires at correct time
   - Kill app → widget still works, reminders still fire

3. **Widget Testing:**
   - Resize widget → adapts number of visible events
   - Tap widget → opens app to correct screen

---

## Lessons Learned & Technical Decisions

> This section is updated as development progresses to capture insights for future reference.

### Decisions Made
| Decision | Rationale | Date |
|----------|-----------|------|
| *To be filled during development* | | |

### Challenges & Solutions
| Challenge | Solution | Impact |
|-----------|----------|--------|
| *To be filled during development* | | |

### What Worked Well
- *To be filled during development*

### What Could Be Improved
- *To be filled during development*

### Code Patterns Established
- *To be filled during development*
