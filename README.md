# True Screen Time

An Android screen-time telemetry application inspired by [TrueScreenTime](https://github.com/DaanyaalSobani/truescreentime), featuring a continuous **morning-to-night chronological session timeline** with explicit **START** and **END** timestamps for every foreground app session.

## ✨ Features

- **Explicit Start & End Session Timestamps:** Every app session card clearly displays `[ START 08:14 AM ] ➔ [ END 08:39 AM ]` in tabular monospace typography (`JetBrains Mono`) alongside exact session duration badges.
- **Morning-to-Night Grouped Timeline:** Sessions are organized into **Morning (6 AM–12 PM)**, **Afternoon (12 PM–5 PM)**, **Evening (5 PM–9 PM)**, and **Night (9 PM–6 AM)** sections with period subtotals, session counts, and phone unlock totals.
- **Idle Lock Gap Tracking:** Automatically calculates and displays how long your phone was locked between sessions (e.g., `Phone locked for 42m`).
- **24-Hour Panoramic Activity Ribbon:** Color-coded full-day canvas ribbon with one-tap period filtering and stacked category split bars.
- **Session Inspector Sheet:** Tap any session to view second-level timestamps (`08:14:22 AM`), prior inactivity duration, and filter to all sessions of that app.
- **Live Device + Sample Day Engine:** Uses Android's `UsageStatsManager.queryEvents()` for real device session tracking, with a one-tap Sample Day preview mode.

## 📦 Multi-Architecture APK Releases (`arm64-v8a`, `universal`, `armeabi-v7a`, `x86_64`)

This repository includes a GitHub Actions workflow (`.github/workflows/android-release.yml`) that automatically compiles and publishes signed release APKs on every push to `main`/`master` or tag `v*`:

- `TrueScreenTime-arm64-v8a.apk` — 64-bit ARM (modern Android phones)
- `TrueScreenTime-universal.apk` — Universal APK (all architectures)
- `TrueScreenTime-armeabi-v7a.apk` — 32-bit ARM
- `TrueScreenTime-x86_64.apk` — 64-bit x86 (emulators / Chromebooks)
- `SHA256SUMS.txt` — Checksums for all release artifacts

### Building Multi-ABI APKs Locally

```bash
gradle :app:assembleRelease -PenableAbiSplits=true
```
