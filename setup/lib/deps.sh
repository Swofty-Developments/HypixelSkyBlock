#!/usr/bin/env bash
# Dependency checking and gum installation

install_figlet() {
    command -v figlet &>/dev/null && return 0
    local distro
    distro=$(detect_distro)
    case "$distro" in
        arch|manjaro|endeavouros|garuda) sudo pacman -S --noconfirm figlet &>/dev/null ;;
        ubuntu|debian|pop|linuxmint)     sudo apt-get install -y figlet &>/dev/null ;;
        fedora|rhel|centos|rocky|alma)   sudo dnf install -y figlet &>/dev/null ;;
    esac
    return 0
}

install_gum() {
    local gum_dir="$HOME/.local/bin"
    mkdir -p "$gum_dir"

    if command -v gum &>/dev/null; then
        HAS_GUM=true
        return 0
    fi

    if [[ -x "$gum_dir/gum" ]]; then
        export PATH="$gum_dir:$PATH"
        HAS_GUM=true
        return 0
    fi

    log_info "Installing gum TUI toolkit..."
    local arch
    arch=$(uname -m)
    case "$arch" in
        x86_64)  arch="amd64" ;;
        aarch64) arch="arm64" ;;
        *) log_warn "Unsupported arch $arch for gum, using fallback prompts"; return 0 ;;
    esac

    local url="https://github.com/charmbracelet/gum/releases/download/v${GUM_VERSION}/gum_${GUM_VERSION}_Linux_${arch}.tar.gz"
    if curl -fsSL "$url" | tar -xz -C "$gum_dir" gum 2>/dev/null; then
        chmod +x "$gum_dir/gum"
        export PATH="$gum_dir:$PATH"
        HAS_GUM=true
        log_ok "gum installed to $gum_dir/gum"
    else
        log_warn "Could not install gum, using fallback prompts"
    fi
}

detect_distro() {
    if [[ -f /etc/os-release ]]; then
        . /etc/os-release && echo "${ID:-unknown}"
    else
        echo "unknown"
    fi
}

check_dependencies() {
    local missing=()

    command -v curl &>/dev/null     || missing+=("curl")
    command -v jq &>/dev/null       || missing+=("jq")
    command -v git &>/dev/null      || missing+=("git")

    if ! command -v docker &>/dev/null; then
        missing+=("docker")
    fi

    if [[ ${#missing[@]} -gt 0 ]]; then
        log_err "Missing required dependencies: ${missing[*]}"
        echo ""

        local distro
        distro=$(detect_distro)

        echo -e "${BOLD}Install instructions:${RESET}"
        echo ""
        for dep in "${missing[@]}"; do
            case "$dep" in
                docker)
                    echo -e "  ${BOLD}Docker Engine:${RESET}"
                    case "$distro" in
                        arch|manjaro|endeavouros|garuda)
                            echo "    sudo pacman -S docker docker-compose"
                            echo "    sudo systemctl enable --now docker"
                            echo "    sudo usermod -aG docker \$USER"
                            echo "    (log out and back in for group change to take effect)"
                            ;;
                        ubuntu|debian|pop|linuxmint)
                            echo "    sudo apt update"
                            echo "    sudo apt install -y ca-certificates curl gnupg"
                            echo "    sudo install -m 0755 -d /etc/apt/keyrings"
                            echo "    curl -fsSL https://download.docker.com/linux/\$(. /etc/os-release && echo \$ID)/gpg | sudo gpg --dearmor -o /etc/apt/keyrings/docker.gpg"
                            echo "    sudo chmod a+r /etc/apt/keyrings/docker.gpg"
                            echo "    echo \"deb [arch=\$(dpkg --print-architecture) signed-by=/etc/apt/keyrings/docker.gpg] https://download.docker.com/linux/\$(. /etc/os-release && echo \$ID) \$(. /etc/os-release && echo \$VERSION_CODENAME) stable\" | sudo tee /etc/apt/sources.list.d/docker.list > /dev/null"
                            echo "    sudo apt update"
                            echo "    sudo apt install -y docker-ce docker-ce-cli containerd.io docker-compose-plugin"
                            echo "    sudo systemctl enable --now docker"
                            echo "    sudo usermod -aG docker \$USER"
                            echo "    (log out and back in for group change to take effect)"
                            ;;
                        fedora|rhel|centos|rocky|alma)
                            echo "    sudo dnf install -y dnf-plugins-core"
                            echo "    sudo dnf config-manager --add-repo https://download.docker.com/linux/fedora/docker-ce.repo"
                            echo "    sudo dnf install -y docker-ce docker-ce-cli containerd.io docker-compose-plugin"
                            echo "    sudo systemctl enable --now docker"
                            echo "    sudo usermod -aG docker \$USER"
                            echo "    (log out and back in for group change to take effect)"
                            ;;
                        *)
                            echo "    See: https://docs.docker.com/engine/install/"
                            ;;
                    esac
                    ;;
                curl)
                    echo -e "  ${BOLD}curl:${RESET}"
                    case "$distro" in
                        arch|manjaro*) echo "    sudo pacman -S curl" ;;
                        ubuntu|debian*) echo "    sudo apt install -y curl" ;;
                        fedora|rhel*)   echo "    sudo dnf install -y curl" ;;
                        *) echo "    Install curl via your package manager" ;;
                    esac
                    ;;
                jq)
                    echo -e "  ${BOLD}jq:${RESET}"
                    case "$distro" in
                        arch|manjaro*) echo "    sudo pacman -S jq" ;;
                        ubuntu|debian*) echo "    sudo apt install -y jq" ;;
                        fedora|rhel*)   echo "    sudo dnf install -y jq" ;;
                        *) echo "    Install jq via your package manager" ;;
                    esac
                    ;;
                git)
                    echo -e "  ${BOLD}git:${RESET}"
                    case "$distro" in
                        arch|manjaro*) echo "    sudo pacman -S git" ;;
                        ubuntu|debian*) echo "    sudo apt install -y git" ;;
                        fedora|rhel*)   echo "    sudo dnf install -y git" ;;
                        *) echo "    Install git via your package manager" ;;
                    esac
                    ;;
            esac
            echo ""
        done
        exit 1
    fi

    if ! docker compose version &>/dev/null 2>&1; then
        log_err "Docker Compose v2 is not available"
        echo ""
        echo -e "  ${BOLD}docker compose${RESET} (v2 plugin) is required but not found."
        echo ""
        echo "  If you installed Docker via your distro's package manager:"
        echo "    sudo pacman -S docker-compose     (Arch/Manjaro)"
        echo "    sudo apt install docker-compose-plugin   (Debian/Ubuntu)"
        echo "    sudo dnf install docker-compose-plugin   (Fedora)"
        echo ""
        echo "  Verify with:  docker compose version"
        exit 1
    fi

    if ! docker info &>/dev/null 2>&1; then
        log_err "Cannot connect to the Docker daemon"
        echo ""

        local docker_err
        docker_err=$(docker info 2>&1 || true)

        if echo "$docker_err" | grep -qi "permission denied"; then
            echo -e "  ${BOLD}Permission denied.${RESET} Your user is not in the 'docker' group."
            echo ""
            echo "  Fix it:"
            echo "    sudo usermod -aG docker \$USER"
            echo ""
            echo "  Then either:"
            echo "    - Log out and log back in, OR"
            echo "    - Run: newgrp docker"
            echo ""
            echo "  Then re-run this installer."
        elif echo "$docker_err" | grep -qi "Is the docker daemon running"; then
            echo -e "  ${BOLD}Docker daemon is not running.${RESET}"
            echo ""
            echo "  Start it:"
            echo "    sudo systemctl start docker"
            echo ""
            echo "  To auto-start on boot:"
            echo "    sudo systemctl enable docker"
            echo ""
            echo "  Verify it's running:"
            echo "    sudo systemctl status docker"
        else
            echo -e "  ${BOLD}Unknown Docker error:${RESET}"
            echo "    $docker_err" | head -5
            echo ""
            echo "  Try:"
            echo "    sudo systemctl start docker"
            echo "    sudo usermod -aG docker \$USER"
            echo "    (then log out and back in)"
        fi
        exit 1
    fi
}
