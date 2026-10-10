# 🤖 Hinweise für KI-Agenten (AI Agents Guide)

## ⚡ Automatisiertes Build-, Auto-Versioning- und Release-System
- **Automatische Versionserhöhung:** `versionCode` und `versionName` in `android/app/build.gradle` sowie `package.json` werden bei jedem Push auf den Branch `main` **vollautomatisch durch GitHub Actions erhöht** (z. B. `versionCode + 1`, `6.9.6` -> `6.9.7`).
- **Automatischer APK-Build & Release:** GitHub Actions baut automatisch `assembleDebug` und `assembleRelease`, erstellt das GitHub Release und hängt die APKs an.
- **Automatischer F-Droid Sync:** GitHub Actions stößt nach dem Release vollautomatisch das Repository `Lauju1909/fdroid-repo` an, sodass Neo Store und F-Droid die neue Version sofort erhalten.

## 📌 Was KI-Agenten beachten MÜSSEN:
1. **Keine manuellen Releases oder Versionsnummern-Änderungen erzwingen:** Features und Bugfixes normal committen. Nach dem Push auf `main` übernimmt GitHub Actions das Hochzählen des Version-Codes, das Taggen und das Veröffentlichen des Releases vollautomatisch.
2. **Barrierefreiheit (TalkBack):** Alle Komponenten müssen 100% barrierefrei bleiben (ContentDescription, barrierefreie Fokus-Steuerung, ARIA-Live-Regionen).
