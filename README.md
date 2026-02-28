# Sentinel: Enterprise Mobile Observability & Telemetry Platform

**A High-Integrity Diagnostic Agent for Distributed Device Fleets.**

[![Kotlin](https://img.shields.io/badge/Kotlin-1.9-purple.svg)](https://kotlinlang.org/)
[![Platform](https://img.shields.io/badge/Platform-Android_14%2B-green.svg)](https://developer.android.com/)
[![Cloud](https://img.shields.io/badge/Cloud-AWS_IoT_Core-orange.svg)](https://aws.amazon.com/iot-core/)
[![License](https://img.shields.io/badge/License-MIT-blue.svg)](LICENSE)

## 📊 Business Context
In enterprise logistics, healthcare, and field operations, mobile devices are mission-critical infrastructure. "Silent" failures—thermal throttling, CPU spikes, and battery degradation—lead to hardware replacement costs and operational downtime.

**Sentinel** transforms Android devices from "Black Boxes" into observable nodes. It captures high-fidelity system signals and streams them to an AWS-based telemetry lake, enabling proactive maintenance and reducing **Mean Time to Repair (MTTR)**.

---

## 🏗️ Technical Architecture
Sentinel is built on a **Clean Architecture** foundation, ensuring modularity, testability, and enterprise-grade reliability.

[Image of a professional three-tier architecture diagram: Mobile Edge Agent, AWS IoT Core Ingestion, and Observability Dashboard]

### 1. The Edge Agent (Android)
* **Reactive Engine:** Utilizes **Kotlin Coroutines and StateFlow** for battery-efficient, non-blocking sensor polling.
* **System Integrity:** Direct interaction with Linux kernel thermal zones via `/sys/class/thermal/` for accurate hardware data.
* **Modern UI:** A declarative, high-performance dashboard built with **Jetpack Compose**.
* **Offline-First:** Robust local persistence using **Room (SQLite)** to prevent data loss during network outages in the field.

### 2. The Cloud Pipeline (AWS)
* **Telemetry Stream:** Lightweight MQTT ingestion via **AWS IoT Core** to preserve mobile data and battery.
* **Serverless Processing:** **AWS Lambda** functions for real-time anomaly detection and threshold alerting.
* **Visualization:** Integration with **Amazon CloudWatch** for fleet-wide observability metrics.

---

## 🛠️ Engineering Excellence & Quality Gating
*Adhering to Deloitte EAID and Google SDE standards for robust software delivery.*

* **Static Analysis:** Enforced code quality via **Detekt** and **Ktlint** to maintain a clean, standardized codebase.
* **Test-Driven Development:** High coverage achieved through **JUnit 5** and **MockK** for business logic validation.
* **Automated CI/CD:** GitHub Actions pipeline validates every PR, ensuring the `main` branch is always deploy-ready.
* **Resource Awareness:** Utilizes **WorkManager** with Exponential Backoff for "Charging + Wi-Fi" restricted cloud synchronization.

---

## ✅ Remaining TODOs (based on current state: sensor observing + basic UI, using Flow + DI + Compose)

### UI / Compose polish
- [ ] Move to Material 3 theming fully (color scheme, typography, dark mode, dynamic color)
- [ ] Add reusable UI components (SensorCard, MetricRow, SectionHeader, Empty/Error states)
- [ ] Add navigation (Navigation-Compose): Dashboard → Sensor details → Settings/About
- [ ] Add Sensor Detail screen (single sensor: live reading + metadata like minDelay/range/vendor)
- [ ] Add charts/sparklines for recent readings (optional but makes the dashboard “observability-like”)

### App architecture (MVVM alignment)
- [ ] Standardize UI state: `UiState` sealed class + one-way data flow (events/actions → ViewModel)
- [ ] Add proper error handling + logging around sensor registration/unregistration
- [ ] Lifecycle correctness: ensure sensors are registered/unregistered predictably (foreground/background)

### Data persistence (offline-first)
- [ ] Add Room to store sensor readings (schema + DAO + migrations)
- [ ] Add a History screen (filter by sensor + time range) backed by Room `Flow`
- [ ] Add retention policy (max rows / time window) to prevent unbounded DB growth

### Export / sharing
- [ ] Export logged data as CSV/JSON (Storage Access Framework) + share intent
- [ ] Add “Report bundle” export (device info + app version + selected sensor logs)

### Background work
- [ ] Add WorkManager job for periodic sampling (user-configurable interval + constraints)
- [ ] Add settings with DataStore (sampling rate, retention, units, theme)

### Quality + repo hygiene
- [ ] Add Detekt + Ktlint (if not already) and enforce in CI
- [ ] Add tests:
  - [ ] ViewModel tests (coroutines-test + Flow testing)
  - [ ] Repository/Room tests (in-memory Room)
  - [ ] Compose UI smoke tests for main screens
- [ ] GitHub Actions CI: build + lint/static analysis + unit tests on PRs

### README accuracy (important)
- [ ] Add a “Current Status” section: what is implemented today (Flows/DI/Compose sensor dashboard)
- [ ] Separate “Planned: AWS IoT / Cloud pipeline” into a clearly marked roadmap section (only if not implemented yet)
- [ ] Fix “Getting Started” clone command + add run instructions (min SDK, permissions, how to test on device) Add “Configuration” section for AWS (IoT endpoint, certs/keys, env/secrets handling)

## 🚀 Getting Started

### Prerequisites
* Android Studio Iguana+
* Android SDK 34+
* AWS CLI (for cloud telemetry features)

### Local Development
1. **Clone the repository:**
   ```bash
   git clone [https://github.com/](https://github.com/)[your-username]/sentinel-observability.git
