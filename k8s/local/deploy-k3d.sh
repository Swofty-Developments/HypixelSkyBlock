#!/usr/bin/env bash
set -euo pipefail

# THIS FILE IS FOR A MINIMAL LOCAL TESTING DEPLOYMENT. IT IS NOT MEANT TO BE USED FOR PRODUCTION OR FULL LOCAL DEPLOYMENTS.

CLUSTER_NAME="${CLUSTER_NAME:-hypixel-local}"
NAMESPACE="${NAMESPACE:-hypixel-local}"
MANIFEST_PATH="${MANIFEST_PATH:-k8s/local/minimal.yaml}"
K3D_SERVER_CONTAINER="k3d-${CLUSTER_NAME}-server-0"
BUILD_FLAGS="${BUILD_FLAGS:---no-cache}"

if ! command -v k3d >/dev/null 2>&1; then
    echo "k3d is required" >&2
    exit 1
fi

if ! command -v docker >/dev/null 2>&1; then
    echo "docker is required" >&2
    exit 1
fi

if ! k3d cluster get "$CLUSTER_NAME" >/dev/null 2>&1; then
    k3d cluster create "$CLUSTER_NAME" --agents 1 --wait -p "25565:25565@loadbalancer"
fi

if ! docker ps --format '{{.Names}}' | grep -qx "$K3D_SERVER_CONTAINER"; then
    echo "expected k3d server container $K3D_SERVER_CONTAINER is not running" >&2
    exit 1
fi

docker build $BUILD_FLAGS -f DockerFiles/Dockerfile.proxy -t hypixel-local/proxy:dev .
docker build $BUILD_FLAGS -f DockerFiles/Dockerfile.game_server -t hypixel-local/game-server:dev .
docker build $BUILD_FLAGS -f DockerFiles/Dockerfile.service \
    --build-arg SERVICE_MODULE=service.auctionhouse \
    --build-arg SERVICE_JAR=ServiceAuctionHouse.jar \
    -t hypixel-local/service-auctionhouse:dev .
docker build $BUILD_FLAGS -f DockerFiles/Dockerfile.service \
    --build-arg SERVICE_MODULE=service.bazaar \
    --build-arg SERVICE_JAR=ServiceBazaar.jar \
    -t hypixel-local/service-bazaar:dev .
docker build $BUILD_FLAGS -f DockerFiles/Dockerfile.service \
    --build-arg SERVICE_MODULE=service.datamutex \
    --build-arg SERVICE_JAR=ServiceDataMutex.jar \
    -t hypixel-local/service-datamutex:dev .
docker build $BUILD_FLAGS -f DockerFiles/Dockerfile.service \
    --build-arg SERVICE_MODULE=service.itemtracker \
    --build-arg SERVICE_JAR=ServiceItemTracker.jar \
    -t hypixel-local/service-itemtracker:dev .

k3d image import \
    -c "$CLUSTER_NAME" \
    hypixel-local/proxy:dev \
    hypixel-local/game-server:dev \
    hypixel-local/service-auctionhouse:dev \
    hypixel-local/service-bazaar:dev \
    hypixel-local/service-datamutex:dev \
    hypixel-local/service-itemtracker:dev

docker exec -i "$K3D_SERVER_CONTAINER" kubectl apply -f - < "$MANIFEST_PATH"
docker exec "$K3D_SERVER_CONTAINER" kubectl -n "$NAMESPACE" rollout status deploy/mongodb --timeout=180s
docker exec "$K3D_SERVER_CONTAINER" kubectl -n "$NAMESPACE" rollout status deploy/redis --timeout=180s
docker exec "$K3D_SERVER_CONTAINER" kubectl -n "$NAMESPACE" rollout status deploy/hypixel-proxy --timeout=300s
docker exec "$K3D_SERVER_CONTAINER" kubectl -n "$NAMESPACE" rollout status deploy/service-auctionhouse --timeout=300s
docker exec "$K3D_SERVER_CONTAINER" kubectl -n "$NAMESPACE" rollout status deploy/service-bazaar --timeout=300s
docker exec "$K3D_SERVER_CONTAINER" kubectl -n "$NAMESPACE" rollout status deploy/service-datamutex --timeout=300s
docker exec "$K3D_SERVER_CONTAINER" kubectl -n "$NAMESPACE" rollout status deploy/service-itemtracker --timeout=300s
docker exec "$K3D_SERVER_CONTAINER" kubectl -n "$NAMESPACE" rollout status deploy/prototype-lobby --timeout=300s
docker exec "$K3D_SERVER_CONTAINER" kubectl -n "$NAMESPACE" rollout status deploy/skyblock-hub --timeout=300s

docker exec "$K3D_SERVER_CONTAINER" kubectl -n "$NAMESPACE" get pods -o wide
