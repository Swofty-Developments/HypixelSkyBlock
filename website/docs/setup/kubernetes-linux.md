# Kubernetes Setup on Linux

This guide explains the supported Kubernetes workflow for Linux hosts and Linux-based clusters.

The canonical flow is the Go setup tool in `setup/`.
It renders profile-driven manifests into `k8s-rendered`, installs missing dependencies on fresh hosts (with
confirmation),
and executes staged deployment.

## 1. Requirements

Before you start, make sure you have:

- Linux with `bash`
- Kubernetes 1.29 or newer
- access to a target cluster (`k3d`, `minikube`, or existing cluster)

Recommended minimum for a real test cluster:

- 8 CPU cores
- 24 GB RAM
- fast SSD storage

## 2. Clone the Repository

```bash
git clone https://github.com/Swofty-Developments/HypixelSkyBlock.git
cd HypixelSkyBlock
```

## 3. Run the Guided Kubernetes Setup

Run full setup:

```bash
./setup/install.sh --action k8s-full
```

Disable stage-level progress UI when needed:

```bash
./setup/install.sh --action k8s-full --k8s-stage-progress-ui=false
```

What happens during `k8s-full`:

1. profile and target selection (wizard)
2. dependency check and optional auto-install on Linux
3. staged execution:
   - ensure cluster
   - install monitoring (optional)
   - build/load images
   - render and apply manifests
   - rollout and status checks

If a staged run fails, rerunning `k8s-full` resumes from the last successful stage when profile-critical inputs did not
change.

Dependency bootstrap uses Bubble Tea/Bubbles progress bars for missing tool installation.

## 4. Dependency Installation Behavior

Missing dependencies are detected from selected action and runtime target.

For Linux hosts, the setup tool can install missing tools after confirmation:

- `docker`
- `kubectl`
- `helm`
- `k3d` (when target is `k3d`)
- `minikube` (when target is `minikube`)
- `nerdctl` (when target is `standard` and local build path needs it)

If you want check-only behavior:

```bash
./setup/install.sh --action k8s-full --auto-install-deps=false
```

## 5. Manifest Generation and Apply

Render only:

```bash
./setup/install.sh --action k8s-render
```

Rendered output is written to:

- `~/.hypixel-skyblock/k8s-rendered` (default install dir)

Apply rendered output manually:

```bash
kubectl apply -f ~/.hypixel-skyblock/k8s-rendered
```

The setup tool manages:

- namespace
- config map
- secret
- proxy deployment/service
- service deployments
- game-server deployments
- autoscaling manifests (when enabled)
- managed datastore manifests (when enabled)

## 6. Image Strategy

Image behavior is profile-driven:

- `image_tag` controls generated image tags.
- `image_registry` controls registry prefix for all generated images.
- `image_pull_policy` controls pull behavior.

Defaults:

- `k3d` and `minikube`: `Never` (local build and import workflow)
- `standard` cluster target: `IfNotPresent`

For `standard` target with pull-based policies (`IfNotPresent` or `Always`), `image_registry` is required.

You can override pull policy in the setup wizard.

## 7. Monitoring and Autoscaling

When `install_monitoring` is enabled, setup installs:

- `kube-prometheus-stack`
- `keda`

When `enable_autoscaling` is enabled, setup renders KEDA `ScaledObject` resources that query proxy metrics through the
configured Prometheus address.

Check autoscaling objects:

```bash
kubectl get scaledobjects -n hypixel
kubectl get hpa -n hypixel
```

## 8. Health and Verification

Quick checks:

```bash
kubectl get pods -n hypixel
kubectl get deployments -n hypixel
```

Proxy health endpoints:

```bash
kubectl port-forward -n hypixel deployment/hypixel-proxy 9090:9090
curl http://127.0.0.1:9090/healthz
curl http://127.0.0.1:9090/readyz
curl http://127.0.0.1:9090/metrics
```

Expose proxy for local testing:

```bash
kubectl port-forward -n hypixel svc/hypixel-proxy 25565:25565
```

## 9. Day-2 Changes

After code changes:

1. rerun full setup, or
2. rebuild/redeploy specific components and run rollout restart.

Typical command:

```bash
kubectl rollout restart deployment/hypixel-proxy -n hypixel
```

## 10. Troubleshooting

If pods do not become ready:

```bash
kubectl describe pod -n hypixel <pod-name>
kubectl logs -n hypixel <pod-name>
```

If autoscaling does not react:

- confirm Prometheus can scrape proxy metrics
- confirm KEDA can reach the configured Prometheus address
- confirm `enable_autoscaling` is enabled in the profile

If dependency auto-install fails:

- rerun as root, or
- configure passwordless sudo for non-interactive install mode, or
- rerun with `--auto-install-deps=false` and install tools manually

If setup stops mid-run:

- rerun `./setup/install.sh --action k8s-full`
- verify the profile still matches your intended cluster and image settings

## Summary

The supported Kubernetes flow is now setup-driven:

1. run setup (`k8s-full`)
2. let setup render and apply generated manifests
3. verify rollout and metrics
4. use profile-driven updates for future changes

This keeps Kubernetes deployment consistent, scalable, and maintainable across fresh VPS and local cluster environments.
