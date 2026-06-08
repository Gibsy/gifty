# Gifty 🎁

> A universal gift & reciprocity tracker for every celebration - birthdays, New Year, weddings, anniversaries, holidays and more.
>
> *“Keep every gift and every favor in balance.”*

![Kotlin](https://img.shields.io/badge/Kotlin-2.0-blueviolet)
![Jetpack Compose](https://img.shields.io/badge/Jetpack%20Compose-Material3-blue)
![Min SDK](https://img.shields.io/badge/minSdk-26-orange)
![Architecture](https://img.shields.io/badge/Architecture-Clean%20%2B%20MVVM-green)
![License](https://img.shields.io/badge/License-MIT-lightgrey)

## Screenshots

||||
| --- | --- | --- |
| ![](https://file.garden/abFLf7yYMGYIwx9m/1.png) | ![](https://file.garden/abFLf7yYMGYIwx9m/2.png) | ![](https://file.garden/abFLf7yYMGYIwx9m/3.png) |

## About

At every celebration people give money and gifts, and there's an unspoken rule of reciprocity — you try to give back roughly what you received. Most people track this in notebooks and scattered notes. **Gifty** turns that chaos into a clean digital ledger for any event: birthday, New Year, wedding, anniversary, holiday, etc.

You pick the event type (or create your own), add people with a relationship label, record gifts given and received, and the app keeps a **reciprocity balance** and suggests a fair amount for upcoming events.

## ✨ Features

- **Events** - record your own and others' events with fully customizable event types (Birthday, New Year, Other by default).
- **People & circles** - add people, tag them with a relationship, and group them into customizable circles (Friend, Colleague, Relative by default).
- **Gifts ledger** - track money/gifts given and received per person, with photos and notes.
- **Reciprocity balance** - see who you owe and a suggested amount for future events.
- **Reminders** - daily upcoming-event check plus custom one-off reminders at a chosen time, localized and with the event's emoji icon.
- **Currency selector** - switch between **Ruble (₽)**, **Tenge (₸)** and **Dollar ($)**.
- **3 languages** - full localization in **Russian**, **Kazakh** and **English**.
- **Themes** - System (Auto) / Light / Dark.
- **Onboarding** - First launch lets you pick language and theme and points you to settings.
- **Backup & export** - JSON backup/restore and CSV/PDF export.
- **Analytics** - spending breakdown by event type.

## Architecture

Clean Architecture + MVVM:

```
com.gibs.kadeesebi
├── di/            // Hilt modules
├── data/          // Room: entities, dao, database, mappers, repository impls, settings, backup, export
├── domain/        // model, repository interfaces, use cases, util (Money, Currency)
└── presentation/  // tois, person, analytics, reminders, settings, onboarding, circles, common, theme, navigation, i18n
```

## Tech stack

Kotlin · Jetpack Compose · Material 3 · Hilt · Room · DataStore · WorkManager · CameraX · Coil · Navigation Compose

## Getting started

1. Open the project folder in **Android Studio**.
2. Let Gradle sync and download dependencies.
3. Run on a device or emulator.

```bash
./gradlew assembleDebug
```

## Data & storage

All data is stored **locally** (Room + DataStore), no cloud. Amounts are stored internally in minor units (1/100 of the main unit); the selected currency only changes how they're displayed.

## License

MIT
