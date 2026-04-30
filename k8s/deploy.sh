#!/usr/bin/env bash
set -euo pipefail

NAMESPACE="kotlin-playground"
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PROJECT_DIR="$(cd "$SCRIPT_DIR/.." && pwd)"

# --- Delete mode ---
if [[ "${1:-}" == "--delete" ]]; then
  echo "Deleting namespace $NAMESPACE and all resources..."
  kubectl delete namespace "$NAMESPACE" --ignore-not-found
  kubectl delete clusterrole prometheus --ignore-not-found
  kubectl delete clusterrolebinding prometheus --ignore-not-found
  echo "Done."
  exit 0
fi

# --- Quick deploy mode: rebuild app image and roll out ---
if [[ "${1:-}" == "--deploy" ]]; then
  echo "=== Quick deploy: rebuilding app and rolling out ==="

  echo "Building Docker image inside minikube..."
  eval "$(minikube docker-env)"
  docker build -t kotlin-playground:latest "$PROJECT_DIR"
  eval "$(minikube docker-env --unset)"

  echo "Restarting deployment to pick up new image..."
  kubectl rollout restart deployment/kotlin-playground -n "$NAMESPACE"

  echo "Waiting for rollout to complete..."
  kubectl rollout status deployment/kotlin-playground -n "$NAMESPACE" --timeout=180s

  echo ""
  echo "=== Deploy complete ==="
  kubectl get pods -n "$NAMESPACE" -l app=kotlin-playground
  exit 0
fi

# --- 1. Start minikube if not running ---
if ! minikube status --format='{{.Host}}' 2>/dev/null | grep -q "Running"; then
  echo "Starting minikube..."
  minikube start --driver=docker --memory=4096 --cpus=2
else
  echo "minikube is already running."
fi

# --- 1b. Enable NGINX Ingress controller ---
if ! minikube addons list | grep -q "ingress.*enabled"; then
  echo "Enabling ingress addon..."
  minikube addons enable ingress
  echo "Waiting for ingress controller to be ready..."
  kubectl wait --for=condition=ready pod -l app.kubernetes.io/component=controller \
    -n ingress-nginx --timeout=120s
else
  echo "Ingress addon is already enabled."
fi

# --- 2. Build Docker image using minikube's Docker daemon ---
echo "Building Docker image inside minikube..."
eval "$(minikube docker-env)"
docker build -t kotlin-playground:latest "$PROJECT_DIR"
eval "$(minikube docker-env --unset)"

# --- 3. Apply namespace ---
echo "Creating namespace..."
kubectl apply -f "$SCRIPT_DIR/namespace.yaml"

# --- 4. Deploy PostgreSQL ---
echo "Deploying PostgreSQL..."
kubectl apply -f "$SCRIPT_DIR/postgres/"

echo "Waiting for PostgreSQL to be ready..."
kubectl wait --for=condition=ready pod -l app=postgres \
  -n "$NAMESPACE" --timeout=120s

# --- 5. Deploy application ---
echo "Deploying application..."
kubectl apply -f "$SCRIPT_DIR/app/"

echo "Waiting for application to be ready..."
kubectl wait --for=condition=ready pod -l app=kotlin-playground \
  -n "$NAMESPACE" --timeout=180s

# --- 6. Deploy monitoring (Prometheus + Grafana) ---
echo "Deploying monitoring stack..."
kubectl apply -f "$SCRIPT_DIR/monitoring/prometheus-rbac.yaml"
kubectl apply -f "$SCRIPT_DIR/monitoring/prometheus-config.yaml"
kubectl apply -f "$SCRIPT_DIR/monitoring/prometheus-deployment.yaml"
kubectl apply -f "$SCRIPT_DIR/monitoring/prometheus-service.yaml"

kubectl create configmap grafana-dashboards \
  --from-file="$PROJECT_DIR/config/grafana/dashboards/" \
  -n "$NAMESPACE" --dry-run=client -o yaml | kubectl apply -f -
kubectl apply -f "$SCRIPT_DIR/monitoring/grafana-config.yaml"
kubectl apply -f "$SCRIPT_DIR/monitoring/grafana-secret.yaml"
kubectl apply -f "$SCRIPT_DIR/monitoring/grafana-deployment.yaml"
kubectl apply -f "$SCRIPT_DIR/monitoring/grafana-service.yaml"

echo "Waiting for monitoring to be ready..."
kubectl wait --for=condition=ready pod -l app=prometheus \
  -n "$NAMESPACE" --timeout=120s
kubectl wait --for=condition=ready pod -l app=grafana \
  -n "$NAMESPACE" --timeout=120s

# --- 7. Apply Ingress ---
echo "Applying Ingress..."
kubectl apply -f "$SCRIPT_DIR/ingress.yaml"

# --- 8. Configure /etc/hosts ---
# On macOS with Docker driver, minikube ip is not routable from the host.
# minikube tunnel bridges the network and makes ingress available on 127.0.0.1.
INGRESS_IP="127.0.0.1"
HOSTS_ENTRIES=("app.local" "grafana.local" "prometheus.local")

NEEDS_HOSTS_UPDATE=false
for host in "${HOSTS_ENTRIES[@]}"; do
  if ! grep -q "^$INGRESS_IP.*$host" /etc/hosts 2>/dev/null; then
    NEEDS_HOSTS_UPDATE=true
    break
  fi
done

if $NEEDS_HOSTS_UPDATE; then
  echo ""
  echo "Updating /etc/hosts (requires sudo)..."
  for host in "${HOSTS_ENTRIES[@]}"; do
    sudo sed -i '' "/$host/d" /etc/hosts 2>/dev/null || true
  done
  echo "$INGRESS_IP ${HOSTS_ENTRIES[*]}" | sudo tee -a /etc/hosts > /dev/null
  echo "  Added: $INGRESS_IP ${HOSTS_ENTRIES[*]}"
fi

# --- 9. Start minikube tunnel ---
# The tunnel needs sudo because it binds to privileged port 80.
if ! pgrep -f "minikube tunnel" > /dev/null 2>&1; then
  echo ""
  echo "Starting minikube tunnel (requires sudo, binds port 80)..."
  sudo nohup minikube tunnel > /tmp/minikube-tunnel.log 2>&1 &
  sleep 3
  echo "  minikube tunnel started (log: /tmp/minikube-tunnel.log)"
else
  echo "minikube tunnel is already running."
fi

# --- 10. Print access info ---
echo ""
echo "=== Deployment complete ==="
echo ""
echo "Pods:"
kubectl get pods -n "$NAMESPACE"
echo ""
echo "Services:"
kubectl get services -n "$NAMESPACE"
echo ""
echo "Ingress:"
kubectl get ingress -n "$NAMESPACE"
echo ""
echo "Access via Ingress (port 80):"
echo "  Application:  http://app.local"
echo "  Grafana:      http://grafana.local   (admin / admin)"
echo "  Prometheus:   http://prometheus.local"
echo ""
echo "NOTE: These URLs require 'minikube tunnel' to be running."
echo "      It was started automatically. To restart manually:"
echo "      minikube tunnel"
echo ""
echo "Useful commands:"
echo "  kubectl get pods -n $NAMESPACE              # List pods"
echo "  kubectl logs -f -l app=kotlin-playground -n $NAMESPACE  # Follow app logs"
echo "  kubectl get ingress -n $NAMESPACE           # Check ingress status"
echo "  minikube dashboard                           # Open K8s dashboard"
echo "  ./k8s/deploy.sh --deploy                     # Rebuild & redeploy app"
echo "  ./k8s/deploy.sh --delete                     # Tear down everything"
