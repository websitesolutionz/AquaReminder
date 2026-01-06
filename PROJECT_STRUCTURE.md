# AquaReminder Project Structure

This document outlines the complete file structure for the AquaReminder Android app.

## 📁 Directory Structure

```
AquaReminder/
│
├── AndroidManifest.xml              # App configuration and permissions
├── build.gradle                     # App-level Gradle build file
├── README.md                        # Comprehensive documentation
│
├── src/main/java/com/aquareminder/app/
│   │
│   ├── AquaReminderApp.kt          # Application class
│   │
│   ├── model/
│   │   └── Models.kt               # Data models (WaterIntake, Statistics, etc.)
│   │
│   ├── data/
│   │   └── PreferencesManager.kt   # DataStore preferences manager
│   │
│   ├── notification/
│   │   ├── NotificationHelper.kt   # Notification scheduling
│   │   └── Receivers.kt            # Broadcast receivers
│   │
│   └── ui/
│       ├── SplashActivity.kt       # Splash screen
│       ├── WelcomeActivity.kt      # First-time setup
│       ├── MainActivity.kt         # Main container activity
│       │
│       ├── fragments/
│       │   ├── HomeFragment.kt     # Main water tracking screen
│       │   ├── RemindersFragment.kt # Reminder settings
│       │   ├── StatisticsFragment.kt # Stats and history
│       │   └── SettingsFragment.kt  # App settings
│       │
│       └── adapters/
│           └── HistoryAdapter.kt   # RecyclerView adapter for history
│
└── res/
    │
    ├── layout/
    │   ├── activity_main.xml        # Main activity layout
    │   ├── activity_welcome.xml     # Welcome screen layout
    │   ├── fragment_home.xml        # Home fragment layout
    │   ├── fragment_reminders.xml   # Reminders fragment layout
    │   ├── fragment_statistics.xml  # Statistics fragment layout
    │   ├── fragment_settings.xml    # Settings fragment layout
    │   ├── item_history.xml         # History item layout
    │   ├── dialog_slider.xml        # Slider dialog layout
    │   └── dialog_number_input.xml  # Number input dialog layout
    │
    ├── drawable/
    │   ├── splash_background.xml    # Splash screen background
    │   ├── bg_gradient.xml          # Gradient background
    │   ├── bg_button_rounded.xml    # Rounded button background
    │   ├── circular_progress.xml    # Circular progress drawable
    │   ├── ic_water_drop.xml        # Water drop icon (app icon)
    │   ├── ic_home.xml              # Home navigation icon
    │   ├── ic_notifications.xml     # Notifications icon
    │   ├── ic_stats.xml             # Statistics icon
    │   ├── ic_settings.xml          # Settings icon
    │   ├── ic_refresh.xml           # Reset icon
    │   ├── ic_check.xml             # Success/goal reached icon
    │   ├── ic_close.xml             # Failed goal icon
    │   └── ic_chevron_right.xml     # Arrow icon
    │
    ├── menu/
    │   └── bottom_navigation_menu.xml # Bottom navigation menu
    │
    ├── values/
    │   ├── strings.xml              # All text strings
    │   ├── colors.xml               # Color definitions
    │   └── themes.xml               # Light theme
    │
    └── values-night/
        └── themes.xml               # Dark theme

```

## 📋 File Descriptions

### Core Application Files

**AquaReminderApp.kt**
- Application class that initializes the app
- Creates notification channels
- Manages global state

**AndroidManifest.xml**
- Declares all activities and permissions
- Configures notification receivers
- Sets app theme and launcher

### Data Layer

**Models.kt**
- `WaterIntake`: Single water intake record
- `DailyRecord`: Daily summary with goal status
- `Statistics`: Aggregated statistics data
- `ReminderSettings`: Notification preferences
- `UserProfile`: User preferences and settings

**PreferencesManager.kt**
- Manages all local data using DataStore
- Handles daily intake tracking
- Stores history and statistics
- Manages user preferences

### UI Layer

#### Activities

**SplashActivity.kt**
- Shows app logo on launch
- Checks if first-time user
- Navigates to Welcome or Main activity

**WelcomeActivity.kt**
- First-time user setup
- Daily goal configuration
- Glass size selection
- Saves initial preferences

**MainActivity.kt**
- Container for fragments
- Bottom navigation management
- Fragment transaction handling

#### Fragments

**HomeFragment.kt**
- Main water tracking interface
- Circular progress display
- Drink water button
- Real-time intake updates
- Goal achievement celebrations

**RemindersFragment.kt**
- Reminder enable/disable
- Start/end time pickers
- Interval selection
- Sound and vibration settings
- Notification permission handling

**StatisticsFragment.kt**
- Current and best streak display
- Today's intake summary
- 7-day bar chart
- History list with RecyclerView

**SettingsFragment.kt**
- Daily goal modification
- Glass size adjustment
- Dark mode toggle
- Premium upgrade option
- About information

#### Adapters

**HistoryAdapter.kt**
- RecyclerView adapter for history list
- Displays date, amount, and goal status
- Formats dates (Today, Yesterday, etc.)

### Notification System

**NotificationHelper.kt**
- Schedules water reminders using AlarmManager
- Manages notification timing
- Handles reminder cancellation
- Respects quiet hours

**Receivers.kt**
- `WaterReminderReceiver`: Handles alarm triggers
- `BootReceiver`: Reschedules reminders after reboot

### Resources

#### Layouts
- Material Design 3 components
- Constraint and Linear layouts
- Card-based UI design
- Responsive sizing

#### Drawables
- Vector icons (SVG format)
- Gradient backgrounds
- Custom progress drawables
- Themed for light/dark modes

#### Values
- Comprehensive strings (English)
- Color palette (Blue/Aqua theme)
- Light and dark themes
- Material Design styles

## 🔧 Configuration Files

**build.gradle**
- Dependencies declaration
- Build configuration
- ProGuard rules
- Version information

## 📦 Required Dependencies

See `build.gradle` for complete list:
- AndroidX Core and AppCompat
- Material Design Components
- Lifecycle and ViewModel
- DataStore Preferences
- Coroutines
- MPAndroidChart
- Google Play Billing
- AdMob

## 🎯 Key Features by File

### Water Tracking
- `HomeFragment.kt` + `fragment_home.xml`
- `PreferencesManager.kt` (data storage)

### Reminders
- `RemindersFragment.kt` + `fragment_reminders.xml`
- `NotificationHelper.kt` (scheduling)
- `Receivers.kt` (handling)

### Statistics
- `StatisticsFragment.kt` + `fragment_statistics.xml`
- `HistoryAdapter.kt` + `item_history.xml`
- `PreferencesManager.kt` (data retrieval)

### Settings
- `SettingsFragment.kt` + `fragment_settings.xml`
- `PreferencesManager.kt` (data updates)

## 🚀 Build Instructions

1. Copy all files maintaining directory structure
2. Open project in Android Studio
3. Sync Gradle files
4. Build and run on device/emulator

## 📱 Screens Flow

```
SplashActivity
    ↓
WelcomeActivity (first time only)
    ↓
MainActivity
    ├─ HomeFragment (default)
    ├─ RemindersFragment
    ├─ StatisticsFragment
    └─ SettingsFragment
```

## 💾 Data Flow

```
User Action
    ↓
Fragment/Activity
    ↓
PreferencesManager (DataStore)
    ↓
Local Storage
    ↓
UI Update (Flow/LiveData)
```

## 🔔 Notification Flow

```
User Enables Reminders
    ↓
RemindersFragment
    ↓
NotificationHelper
    ↓
AlarmManager
    ↓
WaterReminderReceiver
    ↓
Show Notification
```

---

**Total Files**: ~40 files
**Lines of Code**: ~3,500+ lines
**Programming Language**: Kotlin
**UI Framework**: XML + ViewBinding
**Architecture**: MVVM with Repository

This structure provides a complete, production-ready Android application!
