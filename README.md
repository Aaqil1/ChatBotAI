# Context-Aware User Support System

The Context-Aware User Support System delivers a unified experience for customers, virtual agents, and human agents. It captures incoming emails, opens incidents, hosts bot-first live chat with automatic sentiment checks, and notifies human agents when escalation is required.

## Repository Layout

```
backend/                 # Spring Boot microservices (incident, notification, sentiment)
frontend/user-agent-portal/  # React + TypeScript single page application for users & agents
docker/docker-compose.yml    # Multi-container local environment (MongoDB + services)
```

## Features

- **Incident Service (Spring Boot)**
  - Parses customer emails and opens incidents (`POST /parseEmail`).
  - Persists emails, chat history, and incidents in MongoDB.
  - Provides REST + WebSocket APIs for the user chat UI (`GET /incident/{id}`, `POST /botMessage`).
  - Integrates with dummy chatbot and sentiment analysis clients and broadcasts messages to subscribers.
  - Runs an embedded fake SMTP server and scheduled inbox poller that auto-creates incidents from captured emails and replies with chat links.

- **Notification Service (Spring Boot)**
  - Offers form-based authentication for support agents.
  - Stores agents, notifications, and assignments in MongoDB.
  - Provides escalation notifications and incident acceptance endpoints (`GET /notifications`, `POST /acceptChat`).

- **Sentiment Service (Spring Boot)**
  - Supplies a lightweight NLP stub for message analysis (`POST /analyzeMessage`).
  - Saves historical sentiment entries in MongoDB.

- **React Frontend (TypeScript + Vite)**
  - **User Chat UI**: secure deep link access via incident ID, real-time chat via WebSocket, historical context display.
  - **Agent Console**: form login, escalation inbox, live chat with context & sentiment insights.
  - Shared chat window component with STOMP/SockJS client for both personas.

- **Observability & Tooling**
  - Global exception handlers across services.
  - SpringDoc OpenAPI 3 UI exposed on `/swagger-ui.html` for every service.
  - Dockerfiles for all services and a `docker-compose` descriptor for local orchestration.

## Getting Started

### Prerequisites

- Docker & Docker Compose v2
- Node.js 20+ and npm (for local frontend development)
- Java 17+ and Maven 3.9+ (for local backend development)
- MongoDB 6 (if you prefer running services without Docker)

### Local Development (without Docker)

1. **Start MongoDB** locally (`mongodb://localhost:27017`).
2. **Sentiment Service**
   ```bash
   cd backend
   mvn -pl sentiment-service spring-boot:run
   ```
3. **Notification Service** (runs on port `8081`)
   ```bash
   mvn -pl notification-service spring-boot:run
   ```
4. **Incident Service** (runs on port `8080`)
   ```bash
   mvn -pl incident-service spring-boot:run
   ```
5. **Frontend**
   ```bash
   cd ../frontend/user-agent-portal
   npm install
   npm run dev
   ```
6. Open http://localhost:5173/user?incidentId=<YOUR_ID> for the customer view or http://localhost:5173/agent for the agent console.

### Local Development with Docker Compose

The Compose stack provisions MongoDB, all backend services, and the built frontend.

```bash
cd docker
docker compose up --build
```

- Incident API: http://localhost:8080
- Notification API: http://localhost:8081 (login with `agent@example.com` / `password`)
- Sentiment API: http://localhost:8082
- Web UI: http://localhost:3000 (user & agent interfaces)
- Fake SMTP inbox: localhost:2525 (captured by the incident service)

#### Simulate inbound email end-to-end

1. Navigate to http://localhost:3000/email and submit a sample message.
2. The fake SMTP server captures the email; the background poller creates a new incident (e.g., `INC-...`).
3. The service immediately sends a reply email (also visible on the fake SMTP server) containing a deep link to the chat portal.
4. Visit http://localhost:3000/user?incidentId=<INC_ID> to continue the conversation in real time.

### Environment Variables

Each service accepts configuration through environment variables (used in Docker Compose):

| Service | Variable | Default | Description |
|---------|----------|---------|-------------|
| All services | `MONGODB_HOST`, `MONGODB_PORT`, `MONGODB_DATABASE` | `mongodb`, `27017`, `context_support` | MongoDB connection details |
| Incident Service | `SENTIMENT_URL`, `NOTIFICATION_URL` | `http://sentiment-service:8082`, `http://notification-service:8081` | External service locations |
| Incident Service | `SMTP_HOST`, `SMTP_PORT`, `SMTP_BIND_HOST` | `localhost`, `2525`, `0.0.0.0` | JavaMail outbound host/port and bind address for the fake SMTP server |
| Incident Service | `SMTP_FROM` | `noreply@contextsupport.ai` | Sender address used in auto replies |
| Incident Service | `EMAIL_INBOX_ADDRESS`, `EMAIL_INBOX_ENABLED`, `EMAIL_INBOX_POLL_MS` | `support@contextsupport.ai`, `true`, `5000` | Fake inbox recipient, toggle, and poll interval (ms) |
| Incident Service | `PORTAL_BASE_URL` | `http://localhost:3000` | Base URL used in chat deep links |
| Frontend | `VITE_INCIDENT_API`, `VITE_NOTIFICATION_API`, `VITE_SENTIMENT_API` | `http://localhost:8080`, etc. | API endpoints consumed by the SPA |

### API Reference

Each Spring Boot service exposes Swagger UI at `/swagger-ui.html`.

Key endpoints:

- `POST /parseEmail` – persist inbound email, create/update incidents.
- `GET /incident/{id}` – retrieve incident details and chat history.
- `POST /botMessage` – relay user messages to the chatbot stub and persist responses.
- `POST /test-email` – send a sample message into the fake SMTP inbox (for local testing).
- `GET /notifications` – agent-facing notification queue (auth required).
- `POST /acceptChat` – claim an incident and acknowledge notification (auth required).
- `POST /analyzeMessage` – sentiment analysis stub for user/agent messages.

### Authentication

- Agent authentication uses Spring Security form login (session-based).
- Default credentials (seeded on startup): `agent@example.com` / `password`.
- The frontend posts credentials to `/login`, and subsequent API calls reuse the session cookie.

### Placeholder Integrations

- **Chatbot** – `BotIntegrationService` logs requests and crafts a simple acknowledgement string. Replace with DialogFlow/Rasa calls as needed.
- **Sentiment Analysis** – `SentimentAnalyzer` implements a keyword-based scorer. Replace with SpaCy or Azure Text Analytics clients.
- **Notification Dispatch** – `NotificationPublisher` demonstrates REST-style service-to-service escalation. Extend to integrate with messaging or email providers.

### Testing & Quality

- Build the entire backend suite:
  ```bash
  cd backend
  mvn clean verify
  ```
- Frontend lint/build:
  ```bash
  cd frontend/user-agent-portal
  npm run build
  ```

### MongoDB Collections

- `incidents`: incident metadata (status, timestamps, priority).
- `messages`: chat history with sender type and sentiment score.
- `emails`: original inbound email metadata (`incidentId`, `customerDetails`, `subject`, `body`, `attachments`, `from`).
- `notifications`: agent escalation notifications.
- `agents`: registered agents with assigned incidents.
- `sentiments`: persisted sentiment analysis results.

### Incident Workflow Overview

1. Email arrives via `/parseEmail` **or** the fake SMTP inbox → incident created/updated, email persisted, and a reply is dispatched with a chat deep link.
2. User visits `/user?incidentId=...` → WebSocket & REST fetch conversation history.
3. Messages analyzed via sentiment service; high negativity escalates notifications.
4. Agent logs in via `/agent` → sees escalation queue, accepts chat, and continues conversation with full context.
5. All communications stored in MongoDB for auditing and reporting.

### License

This repository is provided for demonstration purposes without a specific license. Contact the authors for production use.
