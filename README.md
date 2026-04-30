# kotlin-playground

A Kotlin playground project for experimenting and learning.

## Stack

- Kotlin 2.1.20
- Java 21
- Spring Boot 3.4
- PostgreSQL 15
- Gradle (Kotlin DSL) with version catalogs
- [Kotest](https://kotest.io/) for testing
- [kotlin-logging](https://github.com/oshai/kotlin-logging) + Logback
- Spring Boot Actuator + Prometheus metrics
- Docker & Docker Compose
- Kubernetes (minikube) for local cluster deployment

## Prerequisites

- JDK 21
- Docker & Docker Compose
- PostgreSQL 15 (for local development without Docker)
- [minikube](https://minikube.sigs.k8s.io/) + kubectl (for Kubernetes deployment)

## Build & Run

### Local development

```bash
# Build the project
./gradlew build

# Run tests
./gradlew test

# Run the application (requires a local PostgreSQL instance)
./gradlew :application:bootRun
```

The application starts on `http://localhost:8080` with the `dev` profile by default.

### Docker Compose

Run the full stack (application + PostgreSQL) with a single command:

```bash
# Build and start all services
docker compose up -d

# View logs
docker compose logs -f app

# Stop all services
docker compose down

# Stop and remove volumes (clears database data)
docker compose down -v
```

The application starts on `http://localhost:8080` with the `prod` profile.

The monitoring stack is included:

| Service | URL | Credentials |
|---------|-----|-------------|
| Application | http://localhost:8080 | - |
| Prometheus | http://localhost:9090 | - |
| Grafana | http://localhost:3000 | admin / admin |

Grafana comes pre-configured with a Prometheus datasource and a **Spring Boot dashboard** showing HTTP request rates, response times (p50/p95/p99), status codes, JVM heap/threads/GC, CPU usage, and HikariCP connection pool metrics.

### Docker only

```bash
# Build the image
docker build -t kotlin-playground:latest .

# Run the container (requires an external PostgreSQL)
docker run -p 8080:8080 \
  -e SPRING_DATASOURCE_URL=jdbc:postgresql://host:5432/playground \
  -e SPRING_DATASOURCE_PASSWORD=postgres \
  kotlin-playground:latest
```

### Kubernetes (minikube)

Deploy the full stack into a local Kubernetes cluster:

```bash
# Install minikube (macOS)
brew install minikube

# Deploy everything (starts minikube, builds image, applies all manifests)
./k8s/deploy.sh

# Tear down everything
./k8s/deploy.sh --delete
```

After making code changes, rebuild and redeploy just the app:

```bash
# Rebuild the Docker image inside minikube and roll out new pods
./k8s/deploy.sh --deploy
```

This rebuilds the image using minikube's Docker daemon, restarts the deployment, and waits
for the new pods to become ready. PostgreSQL, Prometheus, and Grafana are left untouched.

#### Ingress

The deploy script enables the minikube NGINX Ingress controller and creates an Ingress
resource that routes traffic by hostname on standard port 80:

| URL | Service |
|-----|---------|
| http://app.local | Application |
| http://grafana.local | Grafana (admin / admin) |
| http://prometheus.local | Prometheus |

The deploy script automatically:
1. Starts `minikube tunnel` in the background (bridges minikube's network to localhost)
2. Adds the required entries to `/etc/hosts` pointing to `127.0.0.1`

Both steps require sudo. If you prefer to do it manually:

```bash
# Add DNS entries
echo "127.0.0.1 app.local grafana.local prometheus.local" | sudo tee -a /etc/hosts

# Start the tunnel (keep running in a separate terminal)
minikube tunnel
```

Useful commands:

```bash
# Check pod status
kubectl get pods -n kotlin-playground

# Follow application logs
kubectl logs -f -l app=kotlin-playground -n kotlin-playground

# Check ingress status
kubectl get ingress -n kotlin-playground

# Open the Kubernetes dashboard
minikube dashboard
```

The deployment creates the following Kubernetes resources:

| Resource | Kind | Purpose |
|----------|------|---------|
| `kotlin-playground` | Namespace | Isolates all resources |
| `postgres-secret` | Secret | Database credentials |
| `postgres-pvc` | PersistentVolumeClaim | Database storage (1Gi) |
| `postgres` | Deployment | PostgreSQL 15 (1 replica) |
| `postgres` | Service (ClusterIP) | Internal DB access |
| `app-config` | ConfigMap | Non-sensitive app config |
| `app-secret` | Secret | JWT secret, API keys |
| `kotlin-playground` | Deployment | App (2 replicas, liveness + readiness probes) |
| `kotlin-playground` | Service (ClusterIP) | Internal app access |
| `prometheus` (RBAC) | ServiceAccount, ClusterRole, ClusterRoleBinding | Prometheus K8s service discovery |
| `prometheus-config` | ConfigMap | Prometheus scrape configuration |
| `prometheus` | Deployment | Prometheus server (1 replica) |
| `prometheus` | Service (ClusterIP) | Internal Prometheus access |
| `grafana-secret` | Secret | Grafana admin password |
| `grafana-datasources` | ConfigMap | Prometheus datasource config |
| `grafana-dashboards` | ConfigMap | Pre-built Spring Boot dashboard |
| `grafana` | Deployment | Grafana server (1 replica) |
| `grafana` | Service (ClusterIP) | Internal Grafana access |
| `kotlin-playground-ingress` | Ingress | Routes app.local, grafana.local, prometheus.local on port 80 |

## Spring Profiles

| Profile | Purpose | Logging | DDL strategy |
|---------|---------|---------|--------------|
| `dev`   | Local development | Human-readable, DEBUG level | `update` |
| `prod`  | Production / Docker Compose | Structured JSON, INFO level | `validate` |

Activate a profile:

```bash
# Environment variable
export SPRING_PROFILES_ACTIVE=dev

# Command line
java -jar app.jar --spring.profiles.active=prod

# In docker-compose.yml (already configured)
environment:
  - SPRING_PROFILES_ACTIVE=prod,jpa
```

## Monitoring

### Grafana + Prometheus

The project includes a full monitoring stack with Prometheus for metrics collection and Grafana for visualization.

**Docker Compose** — starts automatically with `docker compose up -d`. Open Grafana at http://localhost:3000 (admin/admin), and the pre-provisioned "Kotlin Playground - Spring Boot" dashboard is ready to use.

**Kubernetes** — deployed automatically by `./k8s/deploy.sh`. Access via minikube:

```bash
# Get Grafana URL
minikube service grafana -n kotlin-playground --url

# Get Prometheus URL
minikube service prometheus -n kotlin-playground --url
```

The dashboard includes panels for:
- **HTTP**: request rate by endpoint, response time percentiles (p50/p95/p99), status code distribution, success rate
- **JVM**: heap memory usage, thread counts, GC pause rate, CPU usage (system vs process)
- **Database**: HikariCP connection pool (active/idle/pending), connection acquire time

### Health Check Endpoints

Spring Boot Actuator exposes the following endpoints:

| Endpoint | Description |
|----------|-------------|
| `/actuator/health` | Application health status (checks database connectivity) |
| `/actuator/info` | Application information |
| `/actuator/metrics` | Application metrics |
| `/actuator/prometheus` | Prometheus-formatted metrics for scraping |

```bash
# Check health
curl http://localhost:8080/actuator/health

# View available metrics
curl http://localhost:8080/actuator/metrics

# Get a specific metric
curl http://localhost:8080/actuator/metrics/jvm.memory.used

# Prometheus endpoint (for Grafana/Prometheus integration)
curl http://localhost:8080/actuator/prometheus
```

## Environment Variables

All configuration can be overridden via environment variables:

| Variable | Default | Description |
|----------|---------|-------------|
| `SPRING_PROFILES_ACTIVE` | `dev` | Active Spring profile |
| `SPRING_DATASOURCE_URL` | `jdbc:postgresql://localhost:5432/playground` | Database URL |
| `SPRING_DATASOURCE_USERNAME` | `postgres` | Database username |
| `SPRING_DATASOURCE_PASSWORD` | `postgres` | Database password |
| `SERVER_PORT` | `8080` | Application port |
| `JWT_SECRET` | (dev default) | JWT signing key |
| `JWT_EXPIRATION_MS` | `3600000` | JWT token expiration (ms) |
| `API_KEYS` | `my-api-key-1,my-api-key-2` | Comma-separated valid API keys |

## Project Structure

```
kotlin-playground/
├── application/                 # Spring Boot application module
│   └── src/main/
│       ├── kotlin/              # Application source code
│       └── resources/
│           ├── application.yaml         # Base configuration
│           ├── application-dev.yaml     # Dev profile overrides
│           ├── application-prod.yaml    # Prod profile overrides
│           └── logback-spring.xml       # Profile-aware logging config
├── examples/                    # Standalone Kotlin examples
├── Dockerfile                   # Multi-stage Docker build
├── .dockerignore                # Docker build context exclusions
├── config/                      # External service configuration
│   ├── prometheus/              # Prometheus scrape config
│   └── grafana/                 # Grafana provisioning & dashboards
├── docker-compose.yml           # Full stack: app + PostgreSQL + monitoring
├── k8s/                         # Kubernetes manifests
│   ├── namespace.yaml
│   ├── deploy.sh                # One-command deploy/teardown script
│   ├── ingress.yaml             # NGINX Ingress: app.local, grafana.local, prometheus.local
│   ├── postgres/                # DB: Secret, PVC, Deployment, Service
│   ├── app/                     # App: ConfigMap, Secret, Deployment, Service
│   └── monitoring/              # Prometheus + Grafana: RBAC, Config, Deployment, Service
└── build.gradle.kts             # Root build configuration
```
