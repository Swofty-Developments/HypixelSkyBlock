#!/usr/bin/env bash
set -uo pipefail

# ─── curl | bash safety ──────────────────────────────────────────────────────
if [ ! -t 0 ]; then
    TMPDIR=$(mktemp -d /tmp/hypixel-install-XXXXXX)
    cat > "$TMPDIR/install.sh"
    chmod +x "$TMPDIR/install.sh"

    REPO_RAW="https://raw.githubusercontent.com/Swofty-Developments/HypixelSkyBlock/master/setup/lib"
    mkdir -p "$TMPDIR/lib"
    for mod in ui.sh deps.sh config.sh setup.sh docker.sh; do
        curl -fsSL "$REPO_RAW/$mod" -o "$TMPDIR/lib/$mod" 2>/dev/null || true
    done

    exec bash "$TMPDIR/install.sh" "$@" </dev/tty
fi

# ─── Resolve script directory ────────────────────────────────────────────────
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"

# ─── Constants ────────────────────────────────────────────────────────────────
readonly VERSION="1.0.0"
readonly GITHUB_REPO="Swofty-Developments/HypixelSkyBlock"
readonly GITHUB_API="https://api.github.com/repos/${GITHUB_REPO}"
readonly DEFAULT_INSTALL_DIR="$HOME/.hypixel-skyblock"
readonly STATE_FILE=".state.json"
readonly GUM_VERSION="0.14.5"

# ─── Source library modules ──────────────────────────────────────────────────
for lib in ui deps config setup docker; do
    source "${SCRIPT_DIR}/lib/${lib}.sh"
done

# ─── Global error trap (safety net for uncaught errors) ──────────────────────
trap 'on_error ${LINENO}' ERR

# ─── Re-run detection ────────────────────────────────────────────────────────
handle_existing_install() {
    local state_file="${1}/${STATE_FILE}"
    if [[ -f "$state_file" ]]; then
        echo ""
        log_warn "Existing installation detected at $1"
        echo ""
        log_info "Performing clean reinstall..."
        cd "$1"
        docker compose down --rmi all --volumes 2>&1 || true
        cd "$HOME"
        rm -rf "$1"
        log_ok "Previous installation removed"
    fi
}

# ─── Main flows ──────────────────────────────────────────────────────────────
main_install() {
    set_stage "Welcome"
    show_splash
    system_check

    set_stage "Configuration"
    configure_install

    set_stage "Cleanup"
    handle_existing_install "$INSTALL_DIR"

    set_stage "Setup"
    do_setup

    set_stage "Launch"
    do_launch
}

main() {
    local manage=false
    local watch=false
    local install_dir_override=""

    while [[ $# -gt 0 ]]; do
        case "$1" in
            --manage|-m)   manage=true ;;
            --watch|-w)    watch=true ;;
            --dir)         shift; install_dir_override="$1" ;;
            --help|-h)
                echo "Usage: install.sh [OPTIONS]"
                echo ""
                echo "Options:"
                echo "  --manage, -m    Open management dashboard"
                echo "  --watch, -w     Start health monitoring mode"
                echo "  --dir PATH      Specify install directory"
                echo "  --help, -h      Show this help"
                exit 0
                ;;
            *) ;;
        esac
        shift
    done

    set_stage "Dependency Check"
    check_dependencies
    install_gum
    install_figlet

    if [[ -n "$install_dir_override" ]]; then
        INSTALL_DIR="$install_dir_override"
    else
        INSTALL_DIR="$DEFAULT_INSTALL_DIR"
    fi

    if $watch; then
        local sf="${INSTALL_DIR}/${STATE_FILE}"
        [[ -f "$sf" ]] && INSTALL_DIR=$(jq -r '.install_dir' "$sf")
        cd "$INSTALL_DIR"
        do_watch
        exit 0
    fi

    if $manage; then
        set_stage "Management"
        management_dashboard
        exit 0
    fi

    main_install
}

main "$@"
