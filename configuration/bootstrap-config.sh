#!/usr/bin/env bash
set -euo pipefail

CONFIG_DIR="${1:-./configuration}"
CONFIG_FILE="${CONFIG_DIR}/config.yml"

mkdir -p "$CONFIG_DIR"
if [ ! -f "$CONFIG_FILE" ]; then
    cp ./configuration_files/config.example.yml "$CONFIG_FILE"
fi

replace_config_value() {
    local key="$1"
    local value="$2"
    sed -i "s|^${key}:.*|${key}: ${value}|" "$CONFIG_FILE"
}

replace_config_string() {
    local key="$1"
    local value="$2"
    sed -i "s|^${key}:.*|${key}: '${value}'|" "$CONFIG_FILE"
}

if [ -n "${HYPIXEL_HOST_NAME:-}" ]; then
    replace_config_value "host-name" "$HYPIXEL_HOST_NAME"
fi

if [ -n "${FORWARDING_SECRET:-}" ]; then
    replace_config_string "velocity-secret" "$FORWARDING_SECRET"
fi

if [ -n "${HYPIXEL_MONGODB:-}" ]; then
    replace_config_value "mongodb" "$HYPIXEL_MONGODB"
fi

if [ -n "${HYPIXEL_REDIS_URI:-}" ]; then
    replace_config_value "redis-uri" "$HYPIXEL_REDIS_URI"
    replace_config_value "redis-url" "$HYPIXEL_REDIS_URI"
fi

if [ -n "${HYPIXEL_ADVERTISED_HOST:-}" ]; then
    replace_config_string "advertised-host" "$HYPIXEL_ADVERTISED_HOST"
fi

if [ -n "${HYPIXEL_ADVERTISED_PORT:-}" ]; then
    replace_config_value "advertised-port" "$HYPIXEL_ADVERTISED_PORT"
fi

if [ -n "${HYPIXEL_MANAGEMENT_ENABLED:-}" ]; then
    replace_config_value "  enabled" "$HYPIXEL_MANAGEMENT_ENABLED"
fi

if [ -n "${HYPIXEL_MANAGEMENT_HOST_NAME:-}" ]; then
    replace_config_value "  host-name" "$HYPIXEL_MANAGEMENT_HOST_NAME"
fi

if [ -n "${HYPIXEL_MANAGEMENT_PORT:-}" ]; then
    replace_config_value "  port" "$HYPIXEL_MANAGEMENT_PORT"
fi
