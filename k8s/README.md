# Kubernetes Deployment

This stack assumes:

- Kubernetes owns pod lifecycle for proxies, services, and game servers.
- Pods expose `/healthz`, `/readyz`, and `/metrics` on port `9090`.
- Game servers advertise their pod IP back to Velocity by setting `HYPIXEL_ADVERTISED_HOST` from the downward API.
- KEDA scales lobby/game deployments from proxy-exported Prometheus metrics.

Apply order:

```bash
kubectl apply -f k8s/namespace.yaml
kubectl apply -f k8s/configmap.yaml
kubectl apply -f k8s/secret.example.yaml
kubectl apply -f k8s/proxy.yaml
kubectl apply -f k8s/services.yaml
kubectl apply -f k8s/game-servers.yaml
kubectl apply -f k8s/keda-scaledobjects.yaml
```

Build examples:

```bash
docker build -f DockerFiles/Dockerfile.proxy -t ghcr.io/your-org/hypixel-proxy:latest .
docker build -f DockerFiles/Dockerfile.game_server -t ghcr.io/your-org/hypixel-game:latest .
docker build -f DockerFiles/Dockerfile.service \
  --build-arg SERVICE_MODULE=service.orchestrator \
  --build-arg SERVICE_JAR=ServiceOrchestrator.jar \
  -t ghcr.io/your-org/hypixel-service-orchestrator:latest .
```

Autoscaling model:

- Velocity publishes `hypixel_proxy_server_type_players`, `hypixel_proxy_server_type_capacity`, and
  `hypixel_proxy_server_type_replicas`.
- KEDA reads those metrics from Prometheus and scales the matching deployment.
- Keep one spare pod by querying `sum(players) + threshold`.

To add another `ServerType`, copy one deployment in `k8s/game-servers.yaml`, change `SERVER_TYPE`, and optionally add a
`ScaledObject` in `k8s/keda-scaledobjects.yaml`.
