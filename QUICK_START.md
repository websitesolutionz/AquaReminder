# 🚀 AquaReminder - Quick Start Guide

## ⚡ Get Started in 5 Minutes

### 1️⃣ Prerequisites
- ✅ Android Studio Hedgehog or later
- ✅ Android SDK 24+
- ✅ Basic Android development knowledge

### 2️⃣ Import Project

**Option A: Using Android Studio**
1. Open Android Studio
2. File → New → Import Project
3. Navigate to `AquaReminder` folder
4. Click "OK" and wait for Gradle sync

**Option B: Command Line**
```bash
cd AquaReminder
./gradlew build
```

### 3️⃣ Essential Configuration

#### Update build.gradle (Project level)
Add this to repositories:
```gradle
allprojects {
    repositories {
        google()
        mavenCentral()
        maven { url 'https://jitpack.io' }  // For MPAndroidChart
    }
}
```

#### No Additional Setup Required!
The app works out of the box with:
- ✅ Local storage (DataStore)
- ✅ Basic notifications
- ✅ All core features

### 4️⃣ Optional: Add AdMob (For Ads)

1. Get AdMob App ID from [AdMob Console](https://admob.google.com/)
2. Add to `AndroidManifest.xml`:
```xml
<application>
    <meta-data
        android:name="com.google.android.gms.ads.APPLICATION_ID"
        android:value="ca-app-pub-YOUR_APP_ID_HERE"/>
</application>
```

3. Update test ad unit IDs in `HomeFragment.kt` (search for "adContainer")

### 5️⃣ Run the App

1. Connect Android device or start emulator
2. Click Run ▶️ in Android Studio
3. App will install and launch automatically

### 🎯 First Launch Experience

**What happens:**
1. Splash screen (2 seconds)
2. Welcome screen for setup
3. Set daily goal (default: 2000ml)
4. Choose glass size (250ml/500ml/custom)
5. Tap "Get Started"
6. Home screen appears!

### 📱 Test the Core Features

**Water Tracking:**
- Tap "Drink Water" button
- Watch progress circle animate
- See percentage increase

**Set Reminders:**
1. Tap Reminders tab
2. Enable reminders toggle
3. Set start time (e.g., 8:00 AM)
4. Set end time (e.g., 10:00 PM)
5. Choose interval (30/60/90 minutes)
6. Tap Save

**View Statistics:**
- Tap Statistics tab
- See current streak
- View 7-day chart
- Check history

**Adjust Settings:**
- Tap Settings tab
- Change daily goal
- Modify glass size
- Toggle dark mode

### 🐛 Troubleshooting

**Problem: Gradle sync fails**
- Solution: Update Gradle plugin version in `build.gradle`

**Problem: MPAndroidChart not found**
- Solution: Add jitpack.io to repositories

**Problem: Notifications not working**
- Solution: Grant notification permission in Settings
- Check: Android 13+ requires explicit permission

**Problem: Dark mode not applying**
- Solution: Restart app after toggling

### 📝 Customize Before Launch

#### Change App Name
`res/values/strings.xml`:
```xml
<string name="app_name">Your App Name</string>
```

#### Change Colors
`res/values/colors.xml`:
```xml
<color name="primary_blue">#YOUR_COLOR</color>
<color name="primary_aqua">#YOUR_COLOR</color>
```

#### Change Icon
Replace files in `res/drawable/ic_water_drop.xml` or add PNG icons in `res/mipmap/`

### 🔑 Important Files to Know

**Starting Point:**
- `SplashActivity.kt` - First screen shown
- `WelcomeActivity.kt` - Setup flow
- `MainActivity.kt` - Main app container

**Core Logic:**
- `PreferencesManager.kt` - All data storage
- `NotificationHelper.kt` - Reminder system
- `HomeFragment.kt` - Main tracking screen

**Styling:**
- `res/values/themes.xml` - Light theme
- `res/values-night/themes.xml` - Dark theme
- `res/values/colors.xml` - Color palette

### 🎨 UI Customization Examples

**Change Button Style:**
```xml
<!-- In any layout file -->
<Button
    style="@style/Widget.AquaReminder.Button.Primary"
    android:text="Custom Button"/>
```

**Change Progress Color:**
Edit `circular_progress.xml`:
```xml
<gradient
    android:startColor="@color/YOUR_START_COLOR"
    android:endColor="@color/YOUR_END_COLOR"/>
```

### 📊 Testing Checklist

- [ ] Install on physical device
- [ ] Test first-time setup flow
- [ ] Add water intake multiple times
- [ ] Verify progress updates
- [ ] Set and receive reminders
- [ ] Check notification appears
- [ ] View statistics screen
- [ ] Test dark mode toggle
- [ ] Try resetting daily progress
- [ ] Test across different screen sizes

### 🚀 Ready to Build Release APK?

```bash
# Generate signed APK
./gradlew assembleRelease

# Find APK at:
# app/build/outputs/apk/release/app-release.apk
```

Don't forget to:
1. Create keystore for signing
2. Update `build.gradle` with signing config
3. Test release build thoroughly

### 📚 Next Steps

1. **Read README.md** - Comprehensive documentation
2. **Check PROJECT_STRUCTURE.md** - Understand architecture
3. **Explore code** - Well-commented and organized
4. **Customize** - Make it your own!
5. **Add features** - See README for enhancement ideas

### 💡 Tips for Success

✅ Start with the basics - run app first, customize later
✅ Test on real device for best results
✅ Use debug build for development
✅ Read inline comments in code
✅ Check logcat for debugging

### 🎉 You're Ready!

The app is fully functional and ready to use. Customize it to match your brand and launch it on Google Play Store!

---

**Questions?** Check the comprehensive README.md or explore the well-documented code.

**Happy Coding!** 💧📱
