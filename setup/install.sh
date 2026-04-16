#!/usr/bin/env bash
set -euo pipefail

REPO="Swofty-Developments/HypixelSkyBlock"
APP_NAME="hypixel-setup"
RELEASE_BASE_URL="${HYPIXEL_SETUP_RELEASE_BASE_URL:-https://github.com/${REPO}/releases/latest/download}"
TMP_DIR=""

detect_os() {
  local os
  os="$(uname -s | tr '[:upper:]' '[:lower:]')"
  case "$os" in
    linux) echo "linux" ;;
    *) echo "unsupported" ;;
  esac
}

detect_arch() {
  local arch
  arch="$(uname -m)"
  case "$arch" in
    x86_64|amd64) echo "amd64" ;;
    aarch64|arm64) echo "arm64" ;;
    *) echo "unsupported" ;;
  esac
}

main() {
  local os arch asset_url bin_path
  os="$(detect_os)"
  arch="$(detect_arch)"

  if [[ "$os" == "unsupported" || "$arch" == "unsupported" ]]; then
    echo "Unsupported platform: $(uname -s)/$(uname -m)" >&2
    exit 1
  fi

  asset_url="${RELEASE_BASE_URL}/${APP_NAME}-${os}-${arch}.tar.gz"
  TMP_DIR="$(mktemp -d /tmp/${APP_NAME}-XXXXXX)"
  trap 'rm -rf "$TMP_DIR"' EXIT

  if ! curl -fsSL "$asset_url" -o "$TMP_DIR/${APP_NAME}.tar.gz"; then
    echo "Failed to download ${asset_url}" >&2
    echo "The latest release may not contain ${APP_NAME}-${os}-${arch}.tar.gz yet." >&2
    exit 1
  fi
  tar -xzf "$TMP_DIR/${APP_NAME}.tar.gz" -C "$TMP_DIR"

  bin_path="$TMP_DIR/${APP_NAME}"
  if [[ ! -x "$bin_path" ]]; then
    echo "Release artifact did not contain ${APP_NAME}" >&2
    exit 1
  fi

  exec "$bin_path" "$@"
}

main "$@"
