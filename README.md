# RailPulse ETA

RailPulse ETA is a prototype railway ETA forecasting system for Smart India Hackathon problem statement SIH26028.

It combines a Java Spring Boot backend, a React + TypeScript dashboard, PostgreSQL persistence with Flyway, a lightweight ML training pipeline, and demo data generation for operational scenarios.

## Features

- Search trains by number or name
- View route schedules and next-station ETA
- Baseline ETA predictions with uncertainty interval
- ML adapter with safe fallback to baseline
- Dashboard for passengers and operations
- Alert generation for delay, stale data, and significant ETA changes
- Demo simulator with labelled simulated data
- OpenAPI/Swagger documentation
- Docker Compose setup

## Tech stack

- Backend: Java 21, Spring Boot 3.3.x, Spring Data JPA, Flyway, Maven
- Frontend: React, TypeScript, Vite, Recharts, React Router
- Database: PostgreSQL
- ML: Python 3.11+, scikit-learn, pandas, NumPy, ONNX, ONNX Runtime
- Monitoring: Spring Actuator, structured logging

## Quick start

### Prerequisites

- Java 21+
- Maven 3.9+
- Node 20+
- PostgreSQL 15+
- Python 3.11+

### 1. Configure environment

Copy `.env.example` and fill values.

### 2. Start PostgreSQL

```bash
docker compose up -d postgres
```

### 3. Run backend

```bash
cd backend
mvn clean install
mvn spring-boot:run
```

API URLs:

- http://localhost:8080/api/health
- http://localhost:8080/swagger-ui.html

### 4. Run frontend

```bash
cd frontend
npm install
npm run dev
```

Frontend default URL:

- http://localhost:5173

### 5. Run ML training demo

```bash
cd ml
python -m venv .venv
source .venv/bin/activate
pip install -r requirements.txt
python training/train_model.py
python evaluation/evaluate_model.py
```

## Project structure

```text
railpulse-eta/
├── backend/
│   ├── pom.xml
│   ├── Dockerfile
│   └── src/
├── frontend/
│   ├── package.json
│   ├── Dockerfile
│   └── src/
├── ml/
│   ├── requirements.txt
│   ├── training/
│   ├── evaluation/
│   └── sample-data/
├── docker-compose.yml
├── .env.example
├── .gitignore
└── README.md
```

## Prototype limitations

- This is an operational prototype using demo data unless a live feed is configured.
- No production-grade railway authority data source is included.
- ML model is optional and falls back to the baseline predictor.
- The app is informational and does not control signaling or safety-critical systems.

## License

MIT
