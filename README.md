# AquaReminder - Smart Water Reminder App 💧

A beautiful, modern Android app that helps users stay hydrated throughout the day with smart reminders, progress tracking, and streak management.

## 📱 Features

### Core Features
- **Smart Water Tracking**: Track daily water intake with customizable glass sizes
- **Circular Progress Display**: Beautiful visual representation of daily progress
- **Customizable Reminders**: Set reminders between specific hours with adjustable intervals
- **Streak Tracking**: Build and maintain hydration streaks
- **Statistics & History**: View last 7 days with charts and detailed history
- **Dark Mode Support**: Automatic dark mode based on system settings

### Free Version
- Basic water tracking
- Simple reminders
- 7-day history
- Banner ads

### Premium Version (One-time purchase or Subscription)
- ✅ No ads
- ✅ Unlimited history (90+ days)
- ✅ Smart reminder patterns
- ✅ Advanced statistics

## 🎨 Design

- **Clean & Modern UI**: Material Design 3 with blue/aqua gradient theme
- **Smooth Animations**: Delightful progress animations and transitions
- **Intuitive Navigation**: Bottom navigation with 4 main screens
- **Responsive**: Optimized for all Android screen sizes

## 🏗️ Architecture

### Tech Stack
- **Language**: Kotlin
- **Architecture**: MVVM with Repository pattern
- **UI**: XML layouts with ViewBinding
- **Storage**: DataStore for preferences, local JSON for history
- **Notifications**: AlarmManager for precise reminders
- **Charts**: MPAndroidChart for statistics visualization
- **Monetization**: AdMob + Google Play Billing

### Project Structure
```
app/
├── model/              # Data models (WaterIntake, Statistics, etc.)
├── data/               # Data layer (PreferencesManager)
├── ui/                 # UI layer
│   ├── activities/     # Activities (Splash, Welcome, Main)
│   ├── fragments/      # Fragments (Home, Reminders, Stats, Settings)
│   └── adapters/       # RecyclerView adapters
├── notification/       # Notification helpers and receivers
└── utils/              # Utility classes
```

## 📦 Setup Instructions

### Prerequisites
- Android Studio Hedgehog (2023.1.1) or later
- Android SDK 24 or higher
- Kotlin 1.9.0 or higher
- Gradle 8.0 or higher

### Dependencies
Add to your `build.gradle`:
```gradle
dependencies {
    // Core Android
    implementation 'androidx.core:core-ktx:1.12.0'
    implementation 'androidx.appcompat:appcompat:1.6.1'
    implementation 'com.google.android.material:material:1.11.0'
    
    // Lifecycle & ViewModel
    implementation 'androidx.lifecycle:lifecycle-viewmodel-ktx:2.7.0'
    implementation 'androidx.lifecycle:lifecycle-livedata-ktx:2.7.0'
    
    // DataStore
    implementation 'androidx.datastore:datastore-preferences:1.0.0'
    
    // Coroutines
    implementation 'org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3'
    
    // Charts
    implementation 'com.github.PhilJay:MPAndroidChart:v3.1.0'
    
    // Billing
    implementation 'com.android.billingclient:billing-ktx:6.1.0'
    
    // AdMob
    implementation 'com.google.android.gms:play-services-ads:22.6.0'
}
```

### Installation

1. **Clone or copy the project files**
   ```bash
   # All files are in /home/claude/AquaReminder/
   ```

2. **Add to repositories** (in `settings.gradle`):
   ```gradle
   repositories {
       google()
       mavenCentral()
       maven { url 'https://jitpack.io' }
   }
   ```

3. **Configure AdMob** (Optional):
   - Get AdMob App ID from Google AdMob console
   - Add to `AndroidManifest.xml`:
   ```xml
   <meta-data
       android:name="com.google.android.gms.ads.APPLICATION_ID"
       android:value="ca-app-pub-xxxxxxxxxxxxxxxx~yyyyyyyyyy"/>
   ```

4. **Configure Google Play Billing** (Optional):
   - Set up products in Google Play Console
   - Add product IDs in billing implementation

5. **Build and Run**
   ```bash
   ./gradlew assembleDebug
   # or use Android Studio's Run button
   ```

## 🔑 Key Components

### 1. PreferencesManager
Handles all local data storage using DataStore:
- User profile (daily goal, glass size)
- Reminder settings
- Daily intake tracking
- History and statistics

### 2. NotificationHelper
Manages water reminder notifications:
- Schedules exact alarms using AlarmManager
- Respects quiet hours (start/end time)
- Configurable intervals (30/60/90 minutes)

### 3. Home Fragment
Main screen with circular progress:
- Real-time intake tracking
- Animated progress updates
- Goal achievement celebrations

### 4. Statistics Fragment
Displays hydration analytics:
- Current and best streaks
- Last 7 days bar chart
- History list with goal status

## 🎯 Usage

### First Time Setup
1. App opens to Welcome screen
2. Set daily water goal (default: 2000ml)
3. Choose glass size (250ml, 500ml, or custom)
4. Tap "Get Started"

### Daily Use
1. Tap "Drink Water" button when you drink water
2. Progress updates automatically
3. Get reminded at set intervals
4. Build your streak by meeting daily goals

### Reminders
1. Go to Reminders tab
2. Enable reminders
3. Set start/end times
4. Choose interval (30/60/90 minutes)
5. Tap Save

## 📊 Data Storage

### Local Storage Only
- No account required
- All data stored locally using DataStore
- History saved as JSON in preferences
- Automatic daily reset at midnight

### Data Saved
- Daily water intake
- Last 90 days history (or unlimited for premium)
- Current and best streaks
- User preferences and settings

## 🔔 Notifications

### Permission Handling
- Requests notification permission on Android 13+
- Gracefully handles permission denial
- Reschedules reminders after device reboot

### Notification Features
- Shows between start and end times only
- Repeats at configured interval
- Vibration support
- Tapping opens app to home screen

## 🌙 Dark Mode

- Automatic based on system settings
- Manual toggle in Settings
- Complete theme support for all screens
- Smooth transition between modes

## 🔄 Streak Logic

### How Streaks Work
1. Streak increases when daily goal is met
2. Consecutive days build the streak
3. Missing a day resets streak to 0
4. Best streak is always preserved

## 🎨 Customization

### Colors
Edit `res/values/colors.xml`:
- `primary_blue`: Main brand color
- `primary_aqua`: Secondary color
- `accent_orange`: Streak color

### Strings
Edit `res/values/strings.xml` for all text content

### Themes
Modify `res/values/themes.xml` for styling

## 📱 Supported Android Versions

- **Minimum SDK**: 24 (Android 7.0)
- **Target SDK**: 34 (Android 14)
- **Tested on**: Android 7.0 - 14.0

## 🔒 Permissions

Required permissions in `AndroidManifest.xml`:
```xml
<uses-permission android:name="android.permission.POST_NOTIFICATIONS" />
<uses-permission android:name="android.permission.SCHEDULE_EXACT_ALARM" />
<uses-permission android:name="android.permission.VIBRATE" />
<uses-permission android:name="android.permission.RECEIVE_BOOT_COMPLETED" />
```

## 🚀 Build Variants

### Debug
- Fast build
- Debuggable
- No ProGuard

### Release
- Optimized with ProGuard
- Signed APK required
- Minified code

## 📈 Future Enhancements

Potential features for future versions:
- [ ] Widget for home screen
- [ ] Apple Health / Google Fit integration
- [ ] Custom reminder messages
- [ ] Water intake predictions
- [ ] Social features (share achievements)
- [ ] Multiple drink types (coffee, tea, etc.)
- [ ] Body weight integration for goal calculation
- [ ] Timezone support for travelers

## 🐛 Known Issues

None currently. Please report issues if found!

## 📄 License

This project is provided as-is for educational and development purposes.

## 👨‍💻 Development

### Code Style
- Follow Kotlin coding conventions
- Use meaningful variable names
- Comment complex logic
- Keep functions small and focused

### Testing
- Test on multiple Android versions
- Test dark mode thoroughly
- Test notification permissions
- Verify data persistence

## 🤝 Contributing

Contributions are welcome! Areas for improvement:
- Performance optimizations
- Additional statistics views
- Better chart visualizations
- Improved reminder intelligence

## 📞 Support

For issues or questions:
1. Check the README
2. Review code comments
3. Test in Android Studio

## 🎉 Credits

- Icons: Material Design Icons
- Charts: MPAndroidChart library
- Design inspiration: Modern Android apps

---

**Built with ❤️ using Kotlin and Android Jetpack**

Version 1.0 | © 2025 AquaReminder
