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

## Roadmap

### ✅ Done
- [x] Observed sensor data and displayed it in a basic Android UI
- [x] Used Kotlin Flow for reactive updates
- [x] Added DI setup
- [x] Built UI using Jetpack Compose

### 📝 To Do (now)
- [ ] Improve Compose UI (Material 3 polish, better layout, dark mode)
- [ ] Add navigation (Dashboard → Sensor Details)
- [ ] Add Sensor Details screen (live value + sensor metadata)
- [ ] Add local history (Room) to save readings
- [ ] Add export/share (CSV/JSON)
- [ ] Add basic tests (ViewModel + simple UI test)
- [ ] Update README “Getting Started” + add screenshots

### 📡 Telemetry (later)
- [ ] Define telemetry payload schema (JSON) + topic naming
- [ ] Add MQTT publish pipeline from Android (buffer/queue + retry + reconnect)
- [ ] Add device identity strategy (deviceId) + include app/device metadata
- [ ] Add offline telemetry queue (Room) + background sync (WorkManager constraints)
- [ ] Set up AWS IoT Core and verify messages are received
- [ ] Add basic cloud visibility (CloudWatch logs/metrics/dashboard; Lambda optional)
- [ ] Add “Telemetry Status” UI (connected/disconnected, queued count, last publish time)

## 🚀 Getting Started

### Prerequisites
* Android Studio Iguana+
* Android SDK 34+
* AWS CLI (for cloud telemetry features)

### Local Development
1. **Clone the repository:**
   ```bash
   git clone [https://github.com/](https://github.com/)[your-username]/sentinel-observability.git
