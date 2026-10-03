# Haushaltsbuch Barrierefrei (Android)

[![License: MIT](https://img.shields.io/badge/License-MIT-blue.svg)](LICENSE)
[![Platform: Android](https://img.shields.io/badge/Platform-Android-green.svg)](android)
[![Accessibility: TalkBack Ready](https://img.shields.io/badge/Accessibility-TalkBack%20Ready-brightgreen.svg)]()
[![F-Droid Ready](https://img.shields.io/badge/F--Droid-Ready-blue.svg)](fdroid)

**Haushaltsbuch Barrierefrei** ist eine intuitive, barrierefreie und sichere Finanzverwaltungs-App für Android. Entwickelt für Menschen mit Sehbeeinträchtigungen, Screenreader-Nutzer und alle, die eine klare und datenschutzfreundliche Finanzübersicht suchen.

---

## ✨ Features & Barrierefreiheit

* **Perfekte Screenreader-Unterstützung:** Ausgelegt für TalkBack mit klaren Live-Regionen und semantischer Struktur.
* **OLED-Schwarz-Modus & Kontrast:** Hoher Kontrast, skalierbare Schriften und augenschonendes Design.
* **Flexible Budgets & Zeiträume:** Wöchentlich, monatlich oder nach Gehaltseingang budgetieren.
* **Spartöpfe & Daueraufträge:** Finanzielle Ziele visualisieren und automatisierte Buchungen verwalten.
* **Sicherer Offline-Tresor:** Deine Finanzdaten bleiben auf deinem Gerät. Keine Cloud-Pflicht, kein Tracking.
* **Biometrie & PIN:** Zusätzliche Sicherheit durch Fingerabdruck oder PIN-Code.

---

## 🛠️ Projektstruktur

* `www/`: Barrierefreie Web-App (Vanilla JS, HTML5, CSS3, Sync-Engine)
* `android/`: Natives Android-Projekt mit Capacitor und Biometrie-Integration
* `fastlane/metadata/android/`: Metadaten für F-Droid (Beschreibungen & Icons)
* `fdroid/`: F-Droid Rezept (`de.lauri.finanzapp.yml`)

---

## 🚀 Bauen aus dem Quellcode

```bash
# Abhängigkeiten installieren
npm ci

# Capacitor synchronisieren
npx cap sync android

# Release-APK bauen
cd android
./gradlew assembleRelease
```
Die generierte APK befindet sich in:
`android/app/build/outputs/apk/release/app-release.apk`

---

## 📄 Lizenz

Dieses Projekt steht unter der [MIT-Lizenz](LICENSE).
