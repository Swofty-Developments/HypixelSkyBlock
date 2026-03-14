#!/usr/bin/env bash
# Server/service constants and interactive configuration

SKYBLOCK_SERVERS=(
    "SKYBLOCK_HUB"
    "SKYBLOCK_ISLAND"
    "SKYBLOCK_SPIDERS_DEN"
    "SKYBLOCK_THE_END"
    "SKYBLOCK_CRIMSON_ISLE"
    "SKYBLOCK_DUNGEON_HUB"
    "SKYBLOCK_THE_FARMING_ISLANDS"
    "SKYBLOCK_GOLD_MINE"
    "SKYBLOCK_DEEP_CAVERNS"
    "SKYBLOCK_DWARVEN_MINES"
    "SKYBLOCK_THE_PARK"
    "SKYBLOCK_GALATEA"
    "SKYBLOCK_BACKWATER_BAYOU"
    "SKYBLOCK_JERRYS_WORKSHOP"
)

REQUIRED_SERVERS=("PROTOTYPE_LOBBY")

MINIGAME_SERVERS=(
    "BEDWARS_LOBBY"
    "BEDWARS_GAME"
    "BEDWARS_CONFIGURATOR"
    "MURDER_MYSTERY_LOBBY"
    "MURDER_MYSTERY_GAME"
    "MURDER_MYSTERY_CONFIGURATOR"
    "SKYWARS_LOBBY"
    "SKYWARS_GAME"
    "SKYWARS_CONFIGURATOR"
    "RAVENGARD_LOBBY"
)

ALL_SERVICES=(
    "ServiceDataMutex"
    "ServiceParty"
    "ServiceAPI"
    "ServiceAuctionHouse"
    "ServiceBazaar"
    "ServiceItemTracker"
    "ServiceDarkAuction"
    "ServiceOrchestrator"
    "ServiceFriend"
)

REQUIRED_SERVICES=("ServiceDataMutex" "ServiceParty")

DEFAULT_SERVICES=("ServiceDataMutex" "ServiceParty" "ServiceAPI" "ServiceAuctionHouse" "ServiceBazaar" "ServiceItemTracker")

show_splash() {
    clear_screen
    local splash
    if command -v figlet &>/dev/null; then
        splash=$(figlet -f slant "Hypixel SkyBlock")
    else
        splash=$(cat <<'SPLASH'
    __  __            _           __   _____ __         ____  __           __
   / / / /_  ______  (_)  _____  / /  / ___// /____  __/ __ )/ /___  _____/ /__
  / /_/ / / / / __ \/ / |/_/ _ \/ /   \__ \/ //_/ / / / __  / / __ \/ ___/ //_/
 / __  / /_/ / /_/ / />  </  __/ /   ___/ / ,< / /_/ / /_/ / / /_/ / /__/ ,<
/_/ /_/\__, / .___/_/_/|_|\___/_/   /____/_/|_|\__, /_____/_/\____/\___/_/|_|
      /____/_/                                /____/
SPLASH
)
    fi
    echo -e "${BOLD_CYAN}"
    while IFS= read -r line; do
        center_text "$line"
    done <<< "$splash"
    echo -e "${RESET}"
    center_text "${DIM}Docker TUI Installer v${VERSION}${RESET}"
    echo ""
}

system_check() {
    echo ""
    local results=()
    local all_pass=true

    local ram_kb ram_gb
    ram_kb=$(grep MemTotal /proc/meminfo | awk '{print $2}')
    ram_gb=$(( ram_kb / 1024 / 1024 ))
    if (( ram_gb >= 16 )); then
        results+=("${GREEN}  ✓  ${RESET} RAM            ${ram_gb} GB")
    elif (( ram_gb >= 8 )); then
        results+=("${YELLOW}  ⚠  ${RESET} RAM            ${ram_gb} GB  (16 GB+ recommended)")
    else
        results+=("${RED}  ✗  ${RESET} RAM            ${ram_gb} GB  (16 GB+ recommended)")
        all_pass=false
    fi

    local cores
    cores=$(nproc)
    if (( cores >= 6 )); then
        results+=("${GREEN}  ✓  ${RESET} CPU Cores      ${cores}")
    elif (( cores >= 4 )); then
        results+=("${YELLOW}  ⚠  ${RESET} CPU Cores      ${cores}  (6+ recommended)")
    else
        results+=("${RED}  ✗  ${RESET} CPU Cores      ${cores}  (6+ recommended)")
    fi

    local disk_avail
    disk_avail=$(df -BG "$HOME" | awk 'NR==2 {print $4}' | tr -d 'G')
    if (( disk_avail >= 15 )); then
        results+=("${GREEN}  ✓  ${RESET} Disk Space     ${disk_avail} GB free")
    elif (( disk_avail >= 8 )); then
        results+=("${YELLOW}  ⚠  ${RESET} Disk Space     ${disk_avail} GB free  (15 GB+ recommended)")
    else
        results+=("${RED}  ✗  ${RESET} Disk Space     ${disk_avail} GB free  (15 GB+ recommended)")
        all_pass=false
    fi

    local docker_ver
    docker_ver=$(docker version --format '{{.Server.Version}}' 2>/dev/null || echo "unknown")
    results+=("${GREEN}  ✓  ${RESET} Docker         v${docker_ver}")

    local compose_ver
    compose_ver=$(docker compose version --short 2>/dev/null || echo "unknown")
    results+=("${GREEN}  ✓  ${RESET} Compose        v${compose_ver}")

    {
        echo -e "${BOLD}System Check${RESET}"
        echo ""
        for r in "${results[@]}"; do
            echo -e "$r"
        done
    } | draw_box "System Requirements"

    echo ""
    if ! $all_pass; then
        log_warn "Some requirements are not met. Installation may still work."
        if ! gum_confirm "Continue anyway?"; then
            exit 0
        fi
    fi
}

configure_install() {
    clear_screen
    echo ""
    echo -e "${BOLD_CYAN}  ── Configuration ──${RESET}"
    echo ""

    INSTALL_DIR=$(gum_input --header "Install directory" --value "$DEFAULT_INSTALL_DIR" --placeholder "$DEFAULT_INSTALL_DIR")
    INSTALL_DIR="${INSTALL_DIR:-$DEFAULT_INSTALL_DIR}"
    INSTALL_DIR="${INSTALL_DIR/#\~/$HOME}"
    log_ok "Install directory: ${INSTALL_DIR}"

    echo ""
    BIND_IP=$(gum_input --header "Bind IP for Velocity proxy" --value "0.0.0.0" --placeholder "0.0.0.0")
    BIND_IP="${BIND_IP:-0.0.0.0}"
    log_ok "Bind IP: ${BIND_IP}"

    echo ""
    if gum_confirm "Enable Mojang authentication (online-mode)?"; then
        ONLINE_MODE=true
    else
        ONLINE_MODE=false
    fi
    log_ok "Online mode: ${ONLINE_MODE}"

    echo ""
    echo -e "${BOLD_CYAN}  SkyBlock Servers${RESET}"
    echo -e "  ${DIM}PROTOTYPE_LOBBY is always required${RESET}"
    local sb_defaults
    sb_defaults=$(IFS=','; echo "SKYBLOCK_HUB,SKYBLOCK_ISLAND")
    SELECTED_SB_SERVERS=()
    while IFS= read -r line; do
        [[ -n "$line" ]] && SELECTED_SB_SERVERS+=("$line")
    done < <(gum_filter_multi "Select SkyBlock servers:" "$sb_defaults" "${SKYBLOCK_SERVERS[@]}")

    echo ""
    echo -e "${BOLD_CYAN}  Minigame Servers${RESET}"
    SELECTED_MG_SERVERS=()
    while IFS= read -r line; do
        [[ -n "$line" ]] && SELECTED_MG_SERVERS+=("$line")
    done < <(gum_filter_multi "Select Minigame servers (optional):" "" "${MINIGAME_SERVERS[@]}")

    SELECTED_SERVERS=("${REQUIRED_SERVERS[@]}" "${SELECTED_SB_SERVERS[@]}" "${SELECTED_MG_SERVERS[@]}")

    # Deduplicate in case user somehow added a required server
    local -A seen
    local deduped=()
    for s in "${SELECTED_SERVERS[@]}"; do
        if [[ -z "${seen[$s]+x}" ]]; then
            seen["$s"]=1
            deduped+=("$s")
        fi
    done
    SELECTED_SERVERS=("${deduped[@]}")

    log_ok "Selected ${#SELECTED_SERVERS[@]} server(s) (includes PROTOTYPE_LOBBY)"

    echo ""
    echo -e "${BOLD_CYAN}  Services${RESET}"
    echo -e "  ${DIM}DataMutex and Party are always required${RESET}"
    local svc_defaults
    svc_defaults=$(IFS=','; echo "${DEFAULT_SERVICES[*]}")
    SELECTED_SERVICES=()
    while IFS= read -r line; do
        [[ -n "$line" ]] && SELECTED_SERVICES+=("$line")
    done < <(gum_filter_multi "Select services:" "$svc_defaults" "${ALL_SERVICES[@]}")

    for req in "${REQUIRED_SERVICES[@]}"; do
        local found=false
        for s in "${SELECTED_SERVICES[@]}"; do
            [[ "$s" == "$req" ]] && found=true && break
        done
        if ! $found; then
            SELECTED_SERVICES+=("$req")
        fi
    done
    log_ok "Selected ${#SELECTED_SERVICES[@]} service(s)"

    API_PORT=""
    for s in "${SELECTED_SERVICES[@]}"; do
        if [[ "$s" == "ServiceAPI" ]]; then
            echo ""
            API_PORT=$(gum_input --header "API service port" --value "8080" --placeholder "8080")
            API_PORT="${API_PORT:-8080}"
            log_ok "API port: ${API_PORT}"
            break
        fi
    done

    echo ""
    if gum_confirm "Expose MongoDB/Redis ports to host? (default: sandboxed)"; then
        EXPOSE_PORTS=true
    else
        EXPOSE_PORTS=false
    fi
    log_ok "Expose DB ports: ${EXPOSE_PORTS}"

    local server_count=${#SELECTED_SERVERS[@]}
    local service_count=${#SELECTED_SERVICES[@]}
    local est_ram=$(( 512 + 256 + 128 + server_count * 512 + service_count * 256 ))
    local est_ram_gb
    est_ram_gb=$(awk "BEGIN{printf \"%.1f\", $est_ram/1024}")

    echo ""
    {
        echo -e "${BOLD}Installation Summary${RESET}"
        echo ""
        echo -e "  Directory:      ${BOLD}${INSTALL_DIR}${RESET}"
        echo -e "  Bind IP:        ${BIND_IP}"
        echo -e "  Online mode:    ${ONLINE_MODE}"
        echo -e "  Servers:        ${#SELECTED_SERVERS[@]} selected"
        for s in "${SELECTED_SERVERS[@]}"; do
            echo -e "    ${GREEN}●${RESET} ${s}"
        done
        echo -e "  Services:       ${#SELECTED_SERVICES[@]} selected"
        for s in "${SELECTED_SERVICES[@]}"; do
            echo -e "    ${GREEN}●${RESET} ${s}"
        done
        echo -e "  Expose ports:   ${EXPOSE_PORTS}"
        [[ -n "$API_PORT" ]] && \
        echo -e "  API port:       ${API_PORT}"
        echo -e "  Est. RAM usage: ${YELLOW}~${est_ram_gb} GB${RESET}"
    } | draw_box "Summary"

    echo ""
    if ! gum_confirm "Proceed with installation?"; then
        log_info "Installation cancelled."
        exit 0
    fi
}
