# ThreatBeacon – Backend

## 📖 General Overview

**ThreatBeacon Backend** is the operational and intelligence core of the **ThreatBeacon** platform, deployed in a production environment and designed to operate as a central component within a **Security Operations Center (SOC)**.

The system acts as a critical intermediary layer between:

* Security event sources (logs, services, devices).
* Human analysts.
* Physical IoT alert devices (Beacon).

Its primary responsibility is to **transform large volumes of security events into actionable signals**, combining deterministic rules, risk calculation, and AI-assisted analysis.

---

## 🎯 Backend Responsibilities in Production

1. **Security Event Ingestion**
   Continuous reception of real security events from services, applications, or agents through a secure REST API.

2. **Incident Detection and Correlation**
   Real-time analysis based on correlation rules (e.g., brute force, HTTP anomalies, repetitive patterns).

3. **Global SOC Risk Calculation**
   Dynamic evaluation of the security posture (`NORMAL`, `SUSPICIOUS`, `CRITICAL`) based on active incidents.

4. **Physical Beacon (IoT) Orchestration**
   Centralized control of the physical device’s visual (LEDs) and audible (buzzer) state, including synchronization and remote muting.

5. **Generative Artificial Intelligence Assistance**
   Use of **Spring AI + OpenAI** to generate:

   * Executive incident summaries.
   * Actionable recommendations for SOC analysts.
   * Reduced cognitive load and faster response times.

---

## 🛠️ Technology Stack (Production)

### Backend

* **Java 21 (LTS)**
* **Spring Boot 3.x**

  * Spring Web (REST API)
  * Spring Data JPA (Hibernate)
  * Spring Security (authentication)
  * Spring AI (OpenAI integration)

### Persistence

* **PostgreSQL (Supabase / Managed PostgreSQL)**
  Production-grade database with persistence, backups, and high availability.

### Libraries and Utilities

* **Lombok** – Boilerplate code reduction.
* **MapStruct** – Entity ↔ DTO mapping.
* **Jakarta Validation** – Input data validation.

### Infrastructure

* Containerized deployment (**Docker**).
* Compatible with **Railway / Render / Fly.io / AWS / GCP**.

---

## 🏗️ Project Architecture

Layered architecture organized by functional domain:

```text
src/main/java/com/threatbeacon/backend
├── ai            # OpenAI integration (insights and recommendations)
├── api/dto       # API-exposed Data Transfer Objects
├── beacon        # Physical Beacon state control
├── config        # Security and global configuration
├── event         # Event ingestion and persistence
├── incident      # Rule engine and incident lifecycle
├── mapper        # MapStruct mappers
├── risk          # Global risk calculation
└── ThreatBeaconBackendApplication.java
```
## Diagrams
#### UML Class Diagram
![WhatsApp Image 2025-12-14 at 9 16 13 PM](https://github.com/user-attachments/assets/6e981fa7-349a-4106-a102-9133b5ce9bd6)

#### Database Diagram
![WhatsApp Image 2025-12-14 at 9 20 17 PM](https://github.com/user-attachments/assets/4210b3a3-bfa1-4c3f-af8c-9eecf9512391)

#### Architecture Diagram
![WhatsApp Image 2025-12-14 at 9 20 40 PM](https://github.com/user-attachments/assets/7242a0cd-9807-4194-969d-acf174d69b2f)
---

## ⚙️ Core System Logic

* **`IncidentService.processNewEvent(Event event)`**
  Evaluates each incoming event and applies correlation rules to automatically create or update incidents.

* **`RiskService.calculateRiskStatus(boolean buzzerMuted)`**
  Determines the global SOC risk level based on open incidents and their severity.

* **`IncidentInsightService.generateInsight(Long incidentId)`**
  Produces natural language analysis using OpenAI based on the incident context.

---

## 🚀 Production Configuration

### Required Environment Variables

| Variable                 | Description                               |
| ------------------------ | ----------------------------------------- |
| `OPENAI_API_KEY`         | OpenAI API key (required).                |
| `DB_URL`                 | JDBC URL for the production PostgreSQL DB |
| `DB_USER`                | Database user                             |
| `DB_PASSWORD`            | Database password                         |
| `SPRING_PROFILES_ACTIVE` | `prod`                                    |

> ⚠️ No credentials are hardcoded in production.

---

## 📡 API Security and Access

* The API is protected via **HTTP authentication**.
* Access is restricted to:

  * Official frontend.
  * Physical Beacon device.
  * Internal SOC tooling.

> Deployment behind an **API Gateway** or **Reverse Proxy** with TLS termination is strongly recommended.

---

## 🔗 Main Endpoints (Production)

### 1. Event Ingestion

**POST** `/api/events`

```json
{
  "type": "LOGIN_FAILED",
  "source": "auth-service",
  "ip": "192.168.1.50",
  "country": "RU",
  "severity": "MEDIUM",
  "metadata": "user=admin"
}
```

---

### 2. Global Risk Status

**GET** `/api/risk`

```json
{
  "level": "CRITICAL",
  "buzzerMuted": false,
  "timestamp": "2025-09-18T19:22:31Z"
}
```

Used by:

* SOC Frontend.
* Beacon device (polling).

---

### 3. Beacon Control (Mute)

**POST** `/api/beacon/mute`

```json
{ "muted": true }
```

Silences the audible alarm without altering the visual risk state.

---

### 4. AI Insights per Incident

**GET** `/api/incidents/{id}/insights`

Returns contextual analysis and recommendations generated by AI.

---

## 🧪 Controlled Operation and Testing

In staging or validation environments, test events can be injected to verify:

* Incident correlation.
* Risk level transitions.
* Beacon response behavior.
* AI-generated insights.

---

## 📄 Authors and Credits

Backend project developed within the context of **Cybersecurity and Secure Software Development**.

* **Andrés Gonzales** – Backend Lead / AI Integration
* **Samuel Zapata** – Incident Engine/ Script Python
* **Juan Pablo Rico** – Event Persistence
* **Alejandro Usuga** – API & Controllers
