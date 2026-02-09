#!/usr/bin/env bash
# Docker operations: launch, health checks, status, management dashboard

do_launch() {
    clear_screen
    echo ""
    echo -e "${BOLD_CYAN}  ── Launching ──${RESET}"
    echo ""

    cd "$INSTALL_DIR"

    set_step "Cleaning up stale containers"
    log_info "Removing stale containers..."
    for c in $(docker ps -a --format '{{.Names}}' 2>/dev/null | grep -E '^(hypixel_|hypixelcore_|service_|nanolimbo|game_server_builder)'); do
        docker rm -f "$c" &>/dev/null || true
    done
    docker volume rm mongo-data &>/dev/null || true
    docker builder prune -f &>/dev/null || true

    set_step "Building Docker images"
    log_info "Building Docker images (this may take a few minutes)..."
    export DOCKER_BUILDKIT=1
    run_step_stream "Building Docker images" docker compose build --no-cache
    log_ok "Docker images built"

    set_step "Starting MongoDB + Redis"
    log_info "Starting infrastructure (MongoDB + Redis)..."
    run_step "Starting MongoDB + Redis" docker compose up -d mongodb redis
    if ! wait_healthy "hypixel_mongo" 60; then
        log_err "MongoDB failed to become healthy"
        log_info "Check logs: docker logs hypixel_mongo"
        exit 1
    fi
    if ! wait_healthy "hypixel_redis" 30; then
        log_err "Redis failed to become healthy"
        log_info "Check logs: docker logs hypixel_redis"
        exit 1
    fi
    log_ok "Infrastructure healthy"

    set_step "Starting Velocity proxy"
    log_info "Starting Velocity proxy..."
    run_step "Starting Velocity proxy" docker compose up -d proxy
    if ! wait_healthy "hypixel_proxy" 90; then
        log_err "Velocity proxy failed to become healthy"
        log_info "Check logs: docker logs hypixel_proxy"
        exit 1
    fi
    log_ok "Proxy healthy"

    set_step "Starting NanoLimbo"
    log_info "Starting NanoLimbo + image builder..."
    run_step "Starting NanoLimbo + builder" docker compose up -d game_server_builder nanolimbo
    sleep 3
    log_ok "NanoLimbo started"

    if [[ ${#SELECTED_SERVICES[@]} -gt 0 ]]; then
        set_step "Starting services"
        log_info "Starting services..."
        for service in "${SELECTED_SERVICES[@]}"; do
            local svc_lower
            svc_lower=$(echo "$service" | sed 's/^Service//' | tr '[:upper:]' '[:lower:]')
            if ! docker compose up -d "service_${svc_lower}" 2>&1; then
                log_warn "Failed to start service_${svc_lower}, continuing..."
            fi
        done
        sleep 2
        log_ok "Services started"
    fi

    set_step "Starting game servers"
    log_info "Starting game servers..."
    for server in "${SELECTED_SERVERS[@]}"; do
        local name_lower
        name_lower=$(echo "$server" | tr '[:upper:]' '[:lower:]')
        if ! docker compose up -d "hypixelcore_${name_lower}" 2>&1; then
            log_warn "Failed to start hypixelcore_${name_lower}, continuing..."
        fi
    done
    log_ok "Game servers started"

    echo ""
    sleep 3
    show_status_table

    echo ""
    {
        echo -e "${GREEN}${BOLD}Installation Complete!${RESET}"
        echo ""
        echo -e "  Connect with Minecraft: ${BOLD}${BIND_IP}:25565${RESET}"
        echo ""
        echo -e "  Management dashboard:"
        echo -e "    ${CYAN}${INSTALL_DIR}/install.sh --manage${RESET}"
    } | draw_box "Ready"
    echo ""
}

wait_healthy() {
    local container="$1" timeout_secs="${2:-60}"
    local elapsed=0
    while (( elapsed < timeout_secs )); do
        local health
        health=$(docker inspect --format='{{.State.Health.Status}}' "$container" 2>/dev/null || echo "missing")
        case "$health" in
            healthy)
                printf "\r\033[K"
                return 0
                ;;
            unhealthy)
                printf "\r\033[K"
                log_warn "${container} reported unhealthy"
                return 1
                ;;
        esac

        local state
        state=$(docker inspect --format='{{.State.Status}}' "$container" 2>/dev/null || echo "missing")
        if [[ "$state" == "exited" ]]; then
            printf "\r\033[K"
            log_warn "${container} exited unexpectedly"
            return 1
        fi

        sleep 2
        elapsed=$((elapsed + 2))
        printf "\r\033[K  ${YELLOW}⠸${RESET} Waiting for %s... %ds" "$container" "$elapsed"
    done
    printf "\r\033[K"
    log_warn "${container} did not become healthy within ${timeout_secs}s"
    return 1
}

show_status_table() {
    local containers
    containers=$(docker compose ps --format json 2>/dev/null | jq -r '.Name' 2>/dev/null || true)
    [[ -z "$containers" ]] && containers=$(docker compose ps --format '{{.Name}}' 2>/dev/null || true)

    echo ""
    printf "  ${BOLD}%-35s %-12s %-10s %-15s${RESET}\n" "CONTAINER" "STATUS" "HEALTH" "UPTIME"
    printf "  %-35s %-12s %-10s %-15s\n" "$(printf '─%.0s' {1..35})" "$(printf '─%.0s' {1..12})" "$(printf '─%.0s' {1..10})" "$(printf '─%.0s' {1..15})"

    while IFS= read -r name; do
        [[ -z "$name" ]] && continue
        local state health uptime restarts indicator
        state=$(docker inspect --format='{{.State.Status}}' "$name" 2>/dev/null || echo "unknown")
        health=$(docker inspect --format='{{if .State.Health}}{{.State.Health.Status}}{{else}}n/a{{end}}' "$name" 2>/dev/null || echo "n/a")
        restarts=$(docker inspect --format='{{.RestartCount}}' "$name" 2>/dev/null || echo "0")
        uptime=$(docker inspect --format='{{.State.StartedAt}}' "$name" 2>/dev/null || echo "")

        if [[ -n "$uptime" && "$uptime" != "0001-01-01"* ]]; then
            local start_epoch now_epoch diff
            start_epoch=$(date -d "$uptime" +%s 2>/dev/null || echo 0)
            now_epoch=$(date +%s)
            diff=$(( now_epoch - start_epoch ))
            if (( diff < 60 )); then
                uptime="${diff}s"
            elif (( diff < 3600 )); then
                uptime="$((diff/60))m"
            else
                uptime="$((diff/3600))h $((diff%3600/60))m"
            fi
        else
            uptime="-"
        fi

        case "$health" in
            healthy)   indicator="${GREEN}●${RESET}" ;;
            starting)  indicator="${YELLOW}●${RESET}" ;;
            unhealthy) indicator="${RED}●${RESET}" ;;
            *)
                case "$state" in
                    running)  indicator="${GREEN}●${RESET}" ;;
                    exited)   indicator="${RED}●${RESET}" ;;
                    *)        indicator="${DIM}●${RESET}" ;;
                esac
                ;;
        esac

        local restart_note=""
        (( restarts > 0 )) && restart_note=" ${YELLOW}(${restarts} restarts)${RESET}"

        printf "  ${indicator} %-34s %-12s %-10s %-15s%b\n" "$name" "$state" "$health" "$uptime" "$restart_note"
    done <<< "$containers"
}

check_for_updates() {
    local state_file="${INSTALL_DIR}/${STATE_FILE}"
    [[ ! -f "$state_file" ]] && return 1

    local last_id
    last_id=$(jq -r '.last_release_id // ""' "$state_file" 2>/dev/null)

    local release_info
    release_info=$(curl -fsSL "${GITHUB_API}/releases/latest" 2>/dev/null || true)
    [[ -z "$release_info" ]] && return 1

    local release_id release_tag
    release_id=$(echo "$release_info" | jq -r '.id // ""')
    release_tag=$(echo "$release_info" | jq -r '.tag_name // "unknown"')

    local tmpstate
    tmpstate=$(jq --arg ts "$(date -Iseconds)" '.last_update_check = $ts' "$state_file" 2>/dev/null || cat "$state_file")
    echo "$tmpstate" > "$state_file"

    if [[ -n "$release_id" && "$release_id" != "$last_id" && -n "$last_id" ]]; then
        echo ""
        {
            echo -e "${YELLOW}${BOLD}New version available: ${release_tag}${RESET}"
            echo -e "  Run 'Update' from the management menu to update."
        } | draw_box "Update Available"
        echo ""
        return 0
    fi
    return 1
}

do_update() {
    set_stage "Update"
    log_info "Updating HypixelSkyBlock..."
    cd "$INSTALL_DIR"

    set_step "Stopping containers"
    log_info "Stopping all containers..."
    docker compose down 2>&1 || true

    set_step "Rebuilding images"
    log_info "Rebuilding images (pulling latest JARs)..."
    export DOCKER_BUILDKIT=1
    run_step_stream "Rebuilding Docker images" docker compose build --no-cache
    echo ""

    local release_info release_id
    release_info=$(curl -fsSL "${GITHUB_API}/releases/latest" 2>/dev/null || true)
    release_id=$(echo "$release_info" | jq -r '.id // ""' 2>/dev/null)
    if [[ -n "$release_id" ]]; then
        local tmpstate
        tmpstate=$(jq --arg id "$release_id" '.last_release_id = $id' "${INSTALL_DIR}/${STATE_FILE}" 2>/dev/null || true)
        [[ -n "$tmpstate" ]] && echo "$tmpstate" > "${INSTALL_DIR}/${STATE_FILE}"
    fi

    log_ok "Update complete. Restarting..."
    start_all
}

start_all() {
    cd "$INSTALL_DIR"
    set_step "Starting all containers"
    log_info "Starting all containers (ordered)..."

    docker compose up -d mongodb redis 2>&1 || true
    wait_healthy "hypixel_mongo" 60 || log_warn "MongoDB may not be healthy"
    wait_healthy "hypixel_redis" 30 || log_warn "Redis may not be healthy"

    docker compose up -d proxy 2>&1 || true
    wait_healthy "hypixel_proxy" 60 || log_warn "Proxy may not be healthy"

    docker compose up -d game_server_builder nanolimbo 2>&1 || true
    sleep 2

    docker compose up -d 2>&1 || true
    sleep 3
    log_ok "All containers started"
}

stop_all() {
    cd "$INSTALL_DIR"
    set_step "Stopping all containers"
    log_info "Stopping all containers..."
    docker compose stop 2>&1 || true
    log_ok "All containers stopped"
}

restart_container() {
    cd "$INSTALL_DIR"
    local containers
    containers=$(docker compose ps --format '{{.Name}}' 2>/dev/null)
    [[ -z "$containers" ]] && { log_warn "No containers found"; return; }

    local chosen
    chosen=$(echo "$containers" | gum_choose --header "Select container to restart")
    [[ -z "$chosen" ]] && return

    log_info "Restarting ${chosen}..."
    docker restart "$chosen" 2>&1 || log_warn "Failed to restart ${chosen}"
    log_ok "${chosen} restarted"
}

view_logs() {
    cd "$INSTALL_DIR"
    local containers
    containers=$(docker compose ps --format '{{.Name}}' 2>/dev/null)
    [[ -z "$containers" ]] && { log_warn "No containers found"; return; }

    local chosen
    chosen=$(echo "$containers" | gum_choose --header "Select container for logs")
    [[ -z "$chosen" ]] && return

    echo -e "\n${BOLD}Logs for ${chosen}${RESET} (last 50 lines, Ctrl+C to exit)\n"
    docker logs --tail 50 -f "$chosen" 2>&1 || true
}

do_uninstall() {
    echo ""
    log_warn "This will remove ALL containers, images, volumes, and the install directory."
    if ! gum_confirm "Are you sure you want to uninstall?"; then
        return
    fi

    cd "$INSTALL_DIR"
    log_info "Stopping and removing all containers, images, and volumes..."
    docker compose down --rmi all --volumes 2>&1 || true

    local dir="$INSTALL_DIR"
    cd "$HOME"
    rm -rf "$dir"
    log_ok "Uninstalled. Goodbye!"
    exit 0
}

do_watch() {
    log_info "Watch mode: monitoring containers every 30s (Ctrl+C to exit)"
    while true; do
        clear_screen
        echo -e "${BOLD_CYAN}  ── Container Health Monitor ──${RESET}"
        cd "$INSTALL_DIR"
        show_status_table

        local crashed
        crashed=$(docker compose ps --format json 2>/dev/null | jq -r 'select(.State == "exited") | .Name' 2>/dev/null || true)
        if [[ -n "$crashed" ]]; then
            echo ""
            log_warn "Crashed containers detected:"
            echo "$crashed" | while IFS= read -r c; do
                echo -e "    ${RED}●${RESET} $c"
            done
        fi

        echo -e "\n  ${DIM}Last check: $(date '+%H:%M:%S') — refreshing in 30s${RESET}"
        sleep 30
    done
}

make_admin() {
    echo ""
    log_warn "The player MUST be logged out before changing their rank."
    echo ""

    local username
    username=$(gum_input --header "Minecraft username to promote" --placeholder "username")
    [[ -z "$username" ]] && return

    local username_lower
    username_lower=$(echo "$username" | tr '[:upper:]' '[:lower:]')

    local result
    result=$(docker exec hypixel_mongo mongosh --quiet Minestom --eval "
        var doc = db.profiles.findOne({ignLowercase: '\"${username_lower}\"'});
        if (doc) {
            printjson({found: true, uuid: doc._id, ign: doc.ign, rank: doc.rank});
        } else {
            printjson({found: false});
        }
    " 2>&1)

    local found
    found=$(echo "$result" | jq -r '.found' 2>/dev/null)

    if [[ "$found" != "true" ]]; then
        log_err "Player '${username}' not found in database"
        log_info "They must have joined the server at least once"
        return
    fi

    local current_rank current_ign
    current_rank=$(echo "$result" | jq -r '.rank' 2>/dev/null)
    current_ign=$(echo "$result" | jq -r '.ign' 2>/dev/null)

    echo -e "  Found: ${BOLD}${current_ign}${RESET}"
    echo -e "  Current rank: ${current_rank}"
    echo ""

    if ! gum_confirm "Promote ${username} to STAFF?"; then
        return
    fi

    docker exec hypixel_mongo mongosh --quiet Minestom --eval "
        db.profiles.updateOne(
            {ignLowercase: '\"${username_lower}\"'},
            {\\\$set: {rank: '\"STAFF\"'}}
        );
    " &>/dev/null

    log_ok "${username} has been promoted to STAFF"
    log_info "They need to rejoin the server for the rank to take effect"
}

management_dashboard() {
    local state_file="${INSTALL_DIR}/${STATE_FILE}"
    if [[ ! -f "$state_file" ]]; then
        die "No installation found at ${INSTALL_DIR}. Run without --manage to install."
    fi

    INSTALL_DIR=$(jq -r '.install_dir' "$state_file")
    cd "$INSTALL_DIR"

    while true; do
        clear_screen
        show_splash
        check_for_updates || true
        show_status_table

        echo ""
        local choice
        choice=$(gum_choose --header "Management Menu" \
            "Start All" \
            "Stop All" \
            "Restart Container" \
            "View Logs" \
            "Make Admin" \
            "Check for Updates" \
            "Reconfigure" \
            "Uninstall" \
            "Watch Mode" \
            "Quit")

        case "$choice" in
            "Start All")           start_all ;;
            "Stop All")            stop_all ;;
            "Restart Container")   restart_container ;;
            "View Logs")           view_logs ;;
            "Make Admin")          make_admin ;;
            "Check for Updates")   do_update ;;
            "Reconfigure")
                stop_all
                docker compose down --rmi all --volumes 2>&1 || true
                cd "$HOME"
                rm -rf "$INSTALL_DIR"
                main_install
                return
                ;;
            "Uninstall")           do_uninstall ;;
            "Watch Mode")          do_watch ;;
            "Quit"|"")             log_info "Goodbye!"; exit 0 ;;
        esac

        echo ""
        echo -e "${DIM}Press Enter to return to dashboard...${RESET}"
        read -r
    done
}
