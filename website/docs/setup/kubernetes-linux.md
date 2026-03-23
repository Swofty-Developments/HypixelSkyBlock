# Kubernetes Setup on Linux

This guide explains how to run the full server stack on Kubernetes on a Linux machine or Linux-based cluster.

It covers:

- building the container images
- preparing Redis and MongoDB
- deploying the proxy, services, and game servers
- enabling autoscaling based on player load

:::alert note
This setup is for infrastructure orchestration only. Kubernetes starts and scales the proxy, services, and game server
pods. It does not replace the in-game logic handled by the Java services.
:::

## 1. Requirements

Before you start, make sure you have:

- Linux with `bash`
- Docker or another OCI image builder
- Kubernetes 1.29 or newer
- `kubectl`
- a container registry you can push to
- Prometheus installed in the cluster
- KEDA installed in the cluster
- Redis and MongoDB available inside the cluster

Recommended minimum for a real test cluster:

- 8 CPU cores
- 24 GB RAM
- fast SSD storage

## 2. Clone the Repository

```bash
git clone https://github.com/Swofty-Developments/HypixelSkyBlock.git
cd HypixelSkyBlock
```

## 3. Prepare the Linux Host

Install Java 25 and basic tooling:

```bash
sudo apt update
sudo apt install -y git curl jq unzip ca-certificates
```

If you build with Docker on Ubuntu or Debian:

```bash
sudo apt install -y docker.io
sudo systemctl enable --now docker
sudo usermod -aG docker "$USER"
newgrp docker
```

Install `kubectl` if it is not already present:

```bash
curl -LO "https://dl.k8s.io/release/$(curl -L -s https://dl.k8s.io/release/stable.txt)/bin/linux/amd64/kubectl"
sudo install -m 0755 kubectl /usr/local/bin/kubectl
rm kubectl
```

Check that your cluster is reachable:

```bash
kubectl get nodes
```

## 4. Install Prometheus and KEDA

The Kubernetes manifests in this repository expect:

- Prometheus at `http://prometheus-operated.monitoring.svc.cluster.local:9090`
- KEDA CRDs available in the cluster

If you use Helm, one common setup is:

```bash
helm repo add prometheus-community https://prometheus-community.github.io/helm-charts
helm repo add kedacore https://kedacore.github.io/charts
helm repo update
```

Install Prometheus:

```bash
helm install kube-prometheus prometheus-community/kube-prometheus-stack \
  --namespace monitoring \
  --create-namespace
```

Install KEDA:

```bash
helm install keda kedacore/keda \
  --namespace keda \
  --create-namespace
```

## 5. Prepare Redis and MongoDB

You can run Redis and MongoDB inside or outside Kubernetes. The application only needs reachable connection strings.

Example internal service addresses:

- `redis://redis.hypixel.svc.cluster.local:6379`
- `mongodb://mongodb.hypixel.svc.cluster.local:27017`

If you already run them elsewhere, use your own DNS names instead.

## 6. Review the Kubernetes Files

The manifests are in the `k8s/` folder:

- `namespace.yaml`
- `configmap.yaml`
- `secret.example.yaml`
- `proxy.yaml`
- `services.yaml`
- `game-servers.yaml`
- `keda-scaledobjects.yaml`

These files deploy:

- the Velocity proxy
- the Java microservices
- example game-server deployments
- KEDA scaled objects for player-based scaling

## 7. Build the Images

The repository now builds images from source instead of downloading your own JARs during image build.

Set your registry:

```bash
export REGISTRY=ghcr.io/your-org
```

Build and push the proxy image:

```bash
docker build -f DockerFiles/Dockerfile.proxy -t $REGISTRY/hypixel-proxy:latest .
docker push $REGISTRY/hypixel-proxy:latest
```

Build and push the game server image:

```bash
docker build -f DockerFiles/Dockerfile.game_server -t $REGISTRY/hypixel-game:latest .
docker push $REGISTRY/hypixel-game:latest
```

Build and push service images:

```bash
docker build -f DockerFiles/Dockerfile.service \
  --build-arg SERVICE_MODULE=service.api \
  --build-arg SERVICE_JAR=ServiceAPI.jar \
  -t $REGISTRY/hypixel-service-api:latest .
docker push $REGISTRY/hypixel-service-api:latest

docker build -f DockerFiles/Dockerfile.service \
  --build-arg SERVICE_MODULE=service.auctionhouse \
  --build-arg SERVICE_JAR=ServiceAuctionHouse.jar \
  -t $REGISTRY/hypixel-service-auctionhouse:latest .
docker push $REGISTRY/hypixel-service-auctionhouse:latest

docker build -f DockerFiles/Dockerfile.service \
  --build-arg SERVICE_MODULE=service.bazaar \
  --build-arg SERVICE_JAR=ServiceBazaar.jar \
  -t $REGISTRY/hypixel-service-bazaar:latest .
docker push $REGISTRY/hypixel-service-bazaar:latest

docker build -f DockerFiles/Dockerfile.service \
  --build-arg SERVICE_MODULE=service.darkauction \
  --build-arg SERVICE_JAR=ServiceDarkAuction.jar \
  -t $REGISTRY/hypixel-service-darkauction:latest .
docker push $REGISTRY/hypixel-service-darkauction:latest

docker build -f DockerFiles/Dockerfile.service \
  --build-arg SERVICE_MODULE=service.datamutex \
  --build-arg SERVICE_JAR=ServiceDataMutex.jar \
  -t $REGISTRY/hypixel-service-datamutex:latest .
docker push $REGISTRY/hypixel-service-datamutex:latest

docker build -f DockerFiles/Dockerfile.service \
  --build-arg SERVICE_MODULE=service.friend \
  --build-arg SERVICE_JAR=ServiceFriend.jar \
  -t $REGISTRY/hypixel-service-friend:latest .
docker push $REGISTRY/hypixel-service-friend:latest

docker build -f DockerFiles/Dockerfile.service \
  --build-arg SERVICE_MODULE=service.itemtracker \
  --build-arg SERVICE_JAR=ServiceItemTracker.jar \
  -t $REGISTRY/hypixel-service-itemtracker:latest .
docker push $REGISTRY/hypixel-service-itemtracker:latest

docker build -f DockerFiles/Dockerfile.service \
  --build-arg SERVICE_MODULE=service.orchestrator \
  --build-arg SERVICE_JAR=ServiceOrchestrator.jar \
  -t $REGISTRY/hypixel-service-orchestrator:latest .
docker push $REGISTRY/hypixel-service-orchestrator:latest

docker build -f DockerFiles/Dockerfile.service \
  --build-arg SERVICE_MODULE=service.party \
  --build-arg SERVICE_JAR=ServiceParty.jar \
  -t $REGISTRY/hypixel-service-party:latest .
docker push $REGISTRY/hypixel-service-party:latest

docker build -f DockerFiles/Dockerfile.service \
  --build-arg SERVICE_MODULE=service.punishment \
  --build-arg SERVICE_JAR=ServicePunishment.jar \
  -t $REGISTRY/hypixel-service-punishment:latest .
docker push $REGISTRY/hypixel-service-punishment:latest
```

## 8. Edit the Image Names

Open these files and replace `ghcr.io/your-org/...` with your real registry paths:

- `k8s/proxy.yaml`
- `k8s/services.yaml`
- `k8s/game-servers.yaml`

## 9. Create the Namespace and Base Configuration

Apply the namespace and config map:

```bash
kubectl apply -f k8s/namespace.yaml
kubectl apply -f k8s/configmap.yaml
```

Copy the example secret file and set real values:

```bash
cp k8s/secret.example.yaml /tmp/hypixel-secret.yaml
```

Edit these values:

- `HYPIXEL_MONGODB`
- `HYPIXEL_REDIS_URI`
- `HYPIXEL_VELOCITY_SECRET`
- `FORWARDING_SECRET`

Apply the secret:

```bash
kubectl apply -f /tmp/hypixel-secret.yaml
```

:::alert warning
Use a long random value for `HYPIXEL_VELOCITY_SECRET` and `FORWARDING_SECRET`. The proxy and game servers must use the
same forwarding secret.
:::

## 10. Deploy the Proxy and Services

Apply the proxy:

```bash
kubectl apply -f k8s/proxy.yaml
```

Apply the services:

```bash
kubectl apply -f k8s/services.yaml
```

Check the rollout:

```bash
kubectl get pods -n hypixel
kubectl rollout status deployment/hypixel-proxy -n hypixel
kubectl rollout status deployment/service-orchestrator -n hypixel
```

## 11. Deploy the Game Servers

Apply the example game deployments:

```bash
kubectl apply -f k8s/game-servers.yaml
```

This file already includes example deployments for:

- `PROTOTYPE_LOBBY`
- `BEDWARS_LOBBY`
- `BEDWARS_GAME`
- `SKYBLOCK_HUB`

Check the rollout:

```bash
kubectl get deployments -n hypixel
kubectl get pods -n hypixel -l app=bedwars-game
```

## 12. Enable Autoscaling

Apply the KEDA scaled objects:

```bash
kubectl apply -f k8s/keda-scaledobjects.yaml
```

These scaled objects use proxy metrics exported on port `9090`.

The current model is simple:

- the proxy reports player counts per `ServerType`
- Prometheus scrapes those metrics
- KEDA queries Prometheus
- deployments scale up when player load increases

Check that KEDA created HPAs:

```bash
kubectl get scaledobjects -n hypixel
kubectl get hpa -n hypixel
```

## 13. Expose the Proxy

For local testing, port-forward the proxy:

```bash
kubectl port-forward -n hypixel svc/hypixel-proxy 25565:25565
```

For a real environment, change the proxy service to use:

- `LoadBalancer`, or
- `NodePort`, or
- an Ingress or TCP load balancer provided by your platform

## 14. Check Health and Metrics

Every pod exposes:

- `/healthz`
- `/readyz`
- `/metrics`

Example checks:

```bash
kubectl port-forward -n hypixel deployment/hypixel-proxy 9090:9090
curl http://127.0.0.1:9090/healthz
curl http://127.0.0.1:9090/readyz
curl http://127.0.0.1:9090/metrics
```

For a game server pod:

```bash
kubectl get pods -n hypixel -l app=bedwars-game
kubectl port-forward -n hypixel <pod-name> 9090:9090
curl http://127.0.0.1:9090/metrics
```

## 15. Add More Server Types

To add another game-server deployment:

1. Copy one deployment from `k8s/game-servers.yaml`
2. Change `metadata.name`
3. Change `app`
4. Change `hypixel/server-type`
5. Change the `SERVICE_CMD` value to the new `ServerType`
6. Adjust CPU and memory limits
7. Add a matching `ScaledObject` if you want autoscaling

Example:

```yaml
env:
  - name: SERVICE_CMD
    value: java $JAVA_OPTS -jar HypixelCore.jar SKYWARS_GAME
```

## 16. Update the Deployment

After a code change:

1. rebuild the changed image
2. push it to your registry
3. restart the deployment

Example:

```bash
docker build -f DockerFiles/Dockerfile.game_server -t $REGISTRY/hypixel-game:latest .
docker push $REGISTRY/hypixel-game:latest
kubectl rollout restart deployment/bedwars-game -n hypixel
```

## 17. Troubleshooting

If pods do not become ready:

```bash
kubectl describe pod -n hypixel <pod-name>
kubectl logs -n hypixel <pod-name>
```

If the proxy cannot see game servers:

- confirm the game pods have `HYPIXEL_ADVERTISED_HOST` set to the pod IP
- confirm port `25565` is open inside the pod
- confirm Redis is reachable
- confirm the forwarding secret matches on both sides

If autoscaling does not work:

- confirm Prometheus is scraping `/metrics`
- confirm KEDA can reach Prometheus
- confirm the metric names in `k8s/keda-scaledobjects.yaml` match the proxy output

## 18. Useful Commands

List all Hypixel resources:

```bash
kubectl get all -n hypixel
```

Watch pod changes:

```bash
kubectl get pods -n hypixel -w
```

Restart a deployment:

```bash
kubectl rollout restart deployment/hypixel-proxy -n hypixel
```

Delete everything from this namespace:

```bash
kubectl delete namespace hypixel
```

## Summary

The Kubernetes flow is:

1. build and push the images
2. configure secrets and cluster services
3. deploy proxy and Java services
4. deploy game-server workloads
5. enable KEDA autoscaling
6. expose the proxy to players

Once that is done, Kubernetes manages the process lifecycle, restarts unhealthy pods, and scales selected server types
from live player metrics.
