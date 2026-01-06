# 📱 AquaReminder - App Flow & Features Overview

## 🎨 App Screens Overview

### 1. SPLASH SCREEN
```
┌─────────────────────┐
│                     │
│                     │
│        💧          │
│   AquaReminder      │
│                     │
│                     │
│   [Loading...]      │
│                     │
└─────────────────────┘
```
- Shows app logo with blue gradient
- Displays for 2 seconds
- Checks if first-time user
- Auto-navigates to Welcome or Home

---

### 2. WELCOME SCREEN (First Time Only)
```
┌─────────────────────┐
│   💧 Welcome to     │
│   AquaReminder      │
│                     │
│ Stay hydrated,      │
│ stay healthy        │
│                     │
│ ┌─────────────────┐ │
│ │ Daily Goal      │ │
│ │ ████─────       │ │
│ │ 2000 ml         │ │
│ └─────────────────┘ │
│                     │
│ ┌─────────────────┐ │
│ │ Glass Size      │ │
│ │ ◉ 250 ml        │ │
│ │ ○ 500 ml        │ │
│ │ ○ Custom        │ │
│ └─────────────────┘ │
│                     │
│ [  Get Started  ]   │
└─────────────────────┘
```
**User Actions:**
- Slide to set daily goal (1000-5000ml)
- Select glass size (250ml, 500ml, or custom)
- Tap "Get Started" to save and continue

**What Gets Saved:**
- Daily water goal
- Glass size preference
- First-time flag set to false

---

### 3. HOME SCREEN (Main Screen)
```
┌─────────────────────┐
│ Today's Intake  🔄 │
│                     │
│   ┌───────────┐     │
│  ╱             ╲    │
│ │      💧       │   │
│ │               │   │
│ │     1500      │   │
│ │   / 2000 ml   │   │
│ │               │   │
│ │      75%      │   │
│  ╲             ╱    │
│   └───────────┘     │
│   [Progress Ring]   │
│                     │
│  [💧 Drink Water]   │
│                     │
│ You're doing great! │
│                     │
│ ┌─────────────────┐ │
│ │ [Ad Banner]     │ │
│ └─────────────────┘ │
│                     │
│ [🏠][🔔][📊][⚙️]  │
└─────────────────────┘
```
**Features:**
- Circular progress indicator (animated)
- Current intake / Daily goal
- Percentage display
- Large "Drink Water" button
- Motivational messages
- Reset button (top-right)
- Ad banner (free version only)

**User Interactions:**
- Tap "Drink Water" → Adds configured glass size
- Tap reset icon → Confirms and resets today's progress
- Progress animates smoothly
- Shows celebration when goal reached

---

### 4. REMINDERS SCREEN
```
┌─────────────────────┐
│ Reminders           │
│                     │
│ ┌─────────────────┐ │
│ │ Enable          │ │
│ │ Reminders    ☑️ │ │
│ └─────────────────┘ │
│                     │
│ ┌─────────────────┐ │
│ │ Schedule        │ │
│ │                 │ │
│ │ Start: 08:00 ▶ │ │
│ │ End:   22:00 ▶ │ │
│ │                 │ │
│ │ Interval:       │ │
│ │ ○ 30 minutes    │ │
│ │ ◉ 60 minutes    │ │
│ │ ○ 90 minutes    │ │
│ └─────────────────┘ │
│                     │
│ ┌─────────────────┐ │
│ │ Sound       ☑️  │ │
│ │ Vibration   ☑️  │ │
│ └─────────────────┘ │
│                     │
│    [   Save   ]     │
│                     │
│ [🏠][🔔][📊][⚙️]  │
└─────────────────────┘
```
**Features:**
- Enable/disable toggle
- Time pickers for start/end
- Interval selection (30/60/90 min)
- Sound and vibration toggles
- Save button

**How It Works:**
1. Enable reminders
2. Set start time (when reminders begin)
3. Set end time (when reminders stop)
4. Choose interval between reminders
5. Tap Save
6. Notifications scheduled automatically

---

### 5. STATISTICS SCREEN
```
┌─────────────────────┐
│ Statistics          │
│                     │
│ ┌─────────────────┐ │
│ │ Current Streak  │ │
│ │   🔥 7 Days     │ │
│ │  Best: 12 days  │ │
│ └─────────────────┘ │
│                     │
│ ┌────────┬────────┐ │
│ │ Today  │Average │ │
│ │1800 ml │2100 ml │ │
│ └────────┴────────┘ │
│                     │
│ ┌─────────────────┐ │
│ │ Last 7 Days     │ │
│ │                 │ │
│ │ ▌▌▌▌▌▌▌ [Chart]│ │
│ │                 │ │
│ └─────────────────┘ │
│                     │
│ ┌─────────────────┐ │
│ │ History         │ │
│ │ Today  2000ml ✓ │ │
│ │ Jan 4  1800ml ✗ │ │
│ │ Jan 3  2200ml ✓ │ │
│ └─────────────────┘ │
│                     │
│ [🏠][🔔][📊][⚙️]  │
└─────────────────────┘
```
**Features:**
- Current streak (consecutive days)
- Best streak ever
- Today's intake card
- Average daily intake card
- 7-day bar chart
- Scrollable history list

**Streak Logic:**
- +1 day when goal reached
- Resets to 0 if goal missed
- Best streak always preserved

---

### 6. SETTINGS SCREEN
```
┌─────────────────────┐
│ Settings            │
│                     │
│ ┌─────────────────┐ │
│ │ Daily Goal      │ │
│ │ 2000 ml       ▶ │ │
│ └─────────────────┘ │
│                     │
│ ┌─────────────────┐ │
│ │ Glass Size      │ │
│ │ 250 ml        ▶ │ │
│ └─────────────────┘ │
│                     │
│ ┌─────────────────┐ │
│ │ Dark Mode    ☑️ │ │
│ └─────────────────┘ │
│                     │
│ ┌─────────────────┐ │
│ │ ⭐ Premium      │ │
│ │ • No ads        │ │
│ │ • Unlimited     │ │
│ │ • Smart AI      │ │
│ │ [Go Premium]    │ │
│ └─────────────────┘ │
│                     │
│ ┌─────────────────┐ │
│ │ About           │ │
│ │ Version 1.0     │ │
│ └─────────────────┘ │
│                     │
│ [🏠][🔔][📊][⚙️]  │
└─────────────────────┘
```
**Features:**
- Change daily goal (tap to edit)
- Change glass size (tap to edit)
- Dark mode toggle
- Premium upgrade card
- About information

---

## 🔔 Notification System

### Notification Appearance
```
┌─────────────────────────────┐
│ 💧 Time to drink water!     │
│ Stay hydrated and healthy   │
│                             │
│ [Tap to open app]           │
└─────────────────────────────┘
```

### Notification Behavior
- Appears at set intervals
- Only between start and end times
- Vibrates if enabled
- Makes sound if enabled
- Tapping opens app to Home screen
- Dismissed automatically after 5 seconds

---

## 🔄 App Flow Diagram

```
START
  │
  ├─ First Time User?
  │   ├─ YES → Welcome Screen
  │   │         ↓
  │   │      Set Goal & Glass
  │   │         ↓
  │   └──────→ Home Screen
  │   
  └─ Returning User
       ↓
    Home Screen (Default)
       │
       ├─ Tap Drink Water → Add Intake
       │                      ↓
       │                   Update Progress
       │                      ↓
       │                   Save to Storage
       │                      ↓
       │                 Animate UI Update
       │                      ↓
       │             Check if Goal Reached
       │                      ↓
       │              Show Celebration (if yes)
       │
       ├─ Navigate to Reminders
       │       ↓
       │   Configure Schedule
       │       ↓
       │   Save Settings
       │       ↓
       │   Schedule Alarms
       │
       ├─ Navigate to Statistics
       │       ↓
       │   Load History
       │       ↓
       │   Calculate Streaks
       │       ↓
       │   Display Charts
       │
       └─ Navigate to Settings
               ↓
           Modify Preferences
               ↓
           Save Changes
               ↓
           Apply Immediately
```

---

## 💾 Data Flow

```
USER ACTION
    ↓
UI LAYER (Fragment)
    ↓
VIEWMODEL/LOGIC
    ↓
PREFERENCES MANAGER
    ↓
DATASTORE (Local Storage)
    ↓
FLOW EMISSION
    ↓
UI UPDATE (Automatic)
```

---

## 🎯 Key User Journeys

### Journey 1: First Time User
1. Opens app → Splash
2. Auto-navigate → Welcome
3. Set goal → 2000ml
4. Choose glass → 250ml
5. Tap Get Started → Home
6. See empty progress → 0%
7. Tap Drink Water → +250ml
8. See progress → 12.5%
9. Continue throughout day

### Journey 2: Daily Usage
1. Opens app → Home (shows yesterday's data)
2. Data auto-resets at midnight
3. Start new day at 0ml
4. Receives reminder at 8:00 AM
5. Tap notification → Opens app
6. Tap Drink Water → Add intake
7. Repeat throughout day
8. Reaches goal → Celebration!
9. Streak increases

### Journey 3: Customization
1. Navigate to Settings
2. Tap "Daily Goal"
3. Adjust slider → 2500ml
4. Tap Save
5. Goal updates everywhere
6. Navigate to Reminders
7. Change interval → 30 minutes
8. Tap Save
9. New schedule applied

---

## 🎨 Visual Design Principles

### Color Scheme
- **Primary**: Blue (#2196F3) - Trust, water
- **Secondary**: Aqua (#00BCD4) - Fresh, hydration
- **Accent**: Orange (#FF9800) - Energy, streaks
- **Success**: Green (#4CAF50) - Achievement

### Typography
- **Headlines**: Bold, 24-32sp
- **Body**: Regular, 14-16sp
- **Captions**: Light, 12sp

### Spacing
- **Cards**: 16dp margin
- **Padding**: 16-20dp internal
- **Buttons**: 60-70dp height

### Animation
- **Progress**: 800ms ease-out
- **Button tap**: 100ms scale
- **Screen transitions**: 300ms fade

---

## 📊 Statistics Explained

### Current Streak
- Days where goal was met consecutively
- Resets to 0 on first missed day
- Shows fire emoji 🔥

### Best Streak
- Highest streak ever achieved
- Never decreases
- Personal record

### Average Daily
- Sum of last 7 days / 7
- Updates daily
- Helps track consistency

### 7-Day Chart
- Bar chart showing daily intake
- Color-coded by goal achievement
- Scrollable on mobile

---

## 🔐 Data Privacy

**All data stored locally:**
- ✅ No account required
- ✅ No cloud sync
- ✅ No data collection
- ✅ Complete privacy
- ✅ Works offline

**What's stored:**
- Daily intake amounts
- History (last 90 days free, unlimited premium)
- Reminder preferences
- User settings

---

## 🚀 Premium Features

**Free Version:**
- ✅ Basic tracking
- ✅ Simple reminders
- ✅ 7-day history
- ⚠️ Banner ads

**Premium Version ($2.99/mo or $9.99 lifetime):**
- ✅ No ads
- ✅ Unlimited history
- ✅ Smart reminders
- ✅ Advanced statistics
- ✅ Priority support

---

This visual overview helps understand the complete app experience from user perspective!
