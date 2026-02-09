#!/usr/bin/env bash
# Phase 3: Clone repo, generate configs, docker-compose, state file

do_setup() {
    clear_screen
    echo ""
    echo -e "${BOLD_CYAN}  ── Setting Up ──${RESET}"
    echo ""

    set_step "Creating install directory"
    mkdir -p "$INSTALL_DIR"

    set_step "Cloning repository"
    log_info "Cloning repository (sparse)..."
    (
        cd "$INSTALL_DIR"
        if [[ ! -d "_repo/.git" ]]; then
            git clone --depth 1 --filter=blob:none --sparse \
                "https://github.com/${GITHUB_REPO}.git" _repo 2>&1 | tail -1
            cd _repo
            git sparse-checkout set configuration 2>&1
        else
            cd _repo && git pull --depth 1 2>&1 | tail -1 || true
        fi
    ) &
    if ! spinner $! "Cloning repository..."; then
        log_err "Failed to clone repository"
        log_info "Check your internet connection and try again"
        exit 1
    fi
    log_ok "Repository cloned"

    local repo_dir="${INSTALL_DIR}/_repo"

    set_step "Copying configuration files"
    log_info "Preparing configuration files..."
    mkdir -p "${INSTALL_DIR}/configuration/skyblock"
    mkdir -p "${INSTALL_DIR}/DockerFiles"

    if [[ ! -d "${repo_dir}/configuration" ]]; then
        log_err "Repository clone incomplete — configuration/ not found"
        log_info "Try deleting ${INSTALL_DIR}/_repo and re-running"
        exit 1
    fi

    cp -r "${repo_dir}/configuration/"* "${INSTALL_DIR}/configuration/" 2>/dev/null || true

    local local_repo="${SCRIPT_DIR}/.."
    if [[ -d "${local_repo}/configuration" ]]; then
        cp -r "${local_repo}/configuration/"* "${INSTALL_DIR}/configuration/" 2>/dev/null || true
    fi

    set_step "Generating config.yml"
    generate_config_yml

    set_step "Configuring velocity.toml"
    generate_velocity_toml

    set_step "Generating Dockerfiles"
    generate_dockerfiles

    set_step "Generating docker-compose.yml"
    generate_docker_compose

    set_step "Saving state"
    save_state

    log_ok "Setup complete"
}

generate_config_yml() {
    cat > "${INSTALL_DIR}/configuration/config.yml" <<EOF
host-name: 0.0.0.0
transfer-timeout: 800
mongodb: mongodb://hypixel_mongo
redis-uri: redis://hypixel_redis:6379
velocity-secret: placeholder
require-auth: false
sandbox: false
integrations:
    spark: false
    anticheat: false
    via-version: false
    sentry-dsn: ''
limbo:
    host-name: nanolimbo
    port: 65535
resource-pack:
    public-url: ''
    pack-hash: ''
resource-packs: {}
EOF

    cp "${INSTALL_DIR}/configuration/config.yml" "${INSTALL_DIR}/configuration/config.example.yml"
    log_ok "Generated config.yml (Docker hostnames)"
}

generate_velocity_toml() {
    local toml_file="${INSTALL_DIR}/configuration/velocity.toml"
    if [[ -f "$toml_file" ]]; then
        sed -i "s|^bind = .*|bind = \"${BIND_IP}:25565\"|" "$toml_file"
        sed -i "s|^online-mode = .*|online-mode = ${ONLINE_MODE}|" "$toml_file"
    else
        log_warn "velocity.toml not found in cloned repo, skipping patch"
    fi
    log_ok "Configured velocity.toml (bind=${BIND_IP}, online-mode=${ONLINE_MODE})"
}


generate_dockerfiles() {
    local gh_release="https://github.com/${GITHUB_REPO}/releases/download/latest"
    local velocity_url="https://fill-data.papermc.io/v1/objects/ef1a852bfae7397e84907837925e7ad21c6312066290edaae401b77f6f423ac3/velocity-3.4.0-SNAPSHOT-558.jar"
    local worlds_url="https://files.catbox.moe/9uas8z.gz"

    cat > "${INSTALL_DIR}/DockerFiles/Dockerfile.proxy" <<'PROXY_EOF'
FROM eclipse-temurin:25-jdk
WORKDIR /app
RUN apt-get update && apt-get install -y jq expect netcat-traditional curl && apt-get clean
PROXY_EOF

    cat >> "${INSTALL_DIR}/DockerFiles/Dockerfile.proxy" <<PROXY_EOF
RUN curl -fSL -o velocity.jar "${velocity_url}"
RUN mkdir -p plugins && \\
    curl -fSL -o plugins/SkyBlockProxy.jar "${gh_release}/SkyBlockProxy.jar"
PROXY_EOF

    cat >> "${INSTALL_DIR}/DockerFiles/Dockerfile.proxy" <<'PROXY_EOF'
COPY ./configuration /app/configuration_files
RUN printf '#!/usr/bin/expect -f\nset timeout 120\nspawn java -jar velocity.jar\nexpect ">"\nsend "shutdown\\r"\nexpect eof\n' > /tmp/run_velocity.exp && \
    chmod +x /tmp/run_velocity.exp && \
    /tmp/run_velocity.exp && \
    rm /tmp/run_velocity.exp
RUN rm -f velocity.toml && \
    cp configuration_files/velocity.toml velocity.toml
RUN mkdir -p configuration && \
    cp configuration_files/config.example.yml ./configuration/config.yml && \
    secret=$(cat forwarding.secret) && \
    sed -i "s/velocity-secret: .*/velocity-secret: '$secret'/" ./configuration/config.yml
EXPOSE 25565
CMD ["sh", "-c", "cp forwarding.secret /app/configuration_files/forwarding.secret && java -jar velocity.jar"]
PROXY_EOF

    local jar_downloads=""
    local all_jars=("HypixelCore" "ServiceAPI" "ServiceAuctionHouse" "ServiceBazaar"
                    "ServiceItemTracker" "ServiceDataMutex" "ServiceParty"
                    "ServiceDarkAuction" "ServiceOrchestrator" "ServiceFriend")
    for i in "${!all_jars[@]}"; do
        local jar="${all_jars[$i]}"
        if (( i == 0 )); then
            jar_downloads="RUN curl -fSL -o ${jar}.jar \"${gh_release}/${jar}.jar\""
        else
            jar_downloads="${jar_downloads} && \\
    curl -fSL -o ${jar}.jar \"${gh_release}/${jar}.jar\""
        fi
    done

    cat > "${INSTALL_DIR}/DockerFiles/Dockerfile.game_server" <<GAME_EOF
FROM eclipse-temurin:25-jdk
WORKDIR /app
RUN apt-get update && apt-get install -y jq curl && apt-get clean
${jar_downloads}
COPY ./configuration /app/configuration_files
RUN mkdir -p configuration && \\
    cp configuration_files/config.yml ./configuration/config.yml
RUN curl -fSL -o /tmp/worlds.tar.gz "${worlds_url}" && \\
    tar -xzf /tmp/worlds.tar.gz -C ./configuration && \\
    rm /tmp/worlds.tar.gz
EXPOSE 25565 65535 8080 20000
RUN cp configuration_files/NanoLimbo-1.10.2.jar ./NanoLimbo-1.10.2.jar && \\
    cp configuration_files/settings.yml ./settings.yml && \\
    cp -a configuration_files/skyblock/. configuration/skyblock/ && \\
    cp configuration_files/entrypoint.sh ./entrypoint.sh && \\
    chmod +x entrypoint.sh && \\
    sed -i "s/ip: 'localhost'/ip: '0.0.0.0'/" ./settings.yml
CMD ["sh", "entrypoint.sh"]
GAME_EOF

    log_ok "Generated Dockerfiles"
}

generate_docker_compose() {
    local compose_file="${INSTALL_DIR}/docker-compose.yml"

    local mongo_ports=""
    local redis_ports=""
    if $EXPOSE_PORTS; then
        mongo_ports='    ports:
      - "27017:27017"'
        redis_ports='    ports:
      - "6379:6379"'
    fi

    cat > "$compose_file" <<COMPOSE_HEAD
services:
  mongodb:
    image: mongo:8.0.9
    container_name: hypixel_mongo
    environment:
      MONGO_INITDB_DATABASE: Minestom
      MONGO_URL: hypixel_mongo
    volumes:
      - ./configuration/mongo-init.sh:/docker-entrypoint-initdb.d/mongo-init.sh:ro
      - mongodb-data:/data/db
      - ./configuration/:/csv
${mongo_ports}
    networks:
      - hypixel_network
    healthcheck:
      test: |
        mongosh --eval "db.adminCommand('ping').ok" --quiet > /dev/null 2>&1 || exit 1
      interval: 10s
      timeout: 5s
      retries: 5
    command: ["mongod", "--quiet"]

  redis:
    image: redis:latest
    container_name: hypixel_redis
${redis_ports}
    networks:
      - hypixel_network
    healthcheck:
      test: ["CMD", "redis-cli", "ping"]
      interval: 10s
      timeout: 5s
      retries: 5

  proxy:
    build:
      context: .
      dockerfile: "./DockerFiles/Dockerfile.proxy"
    container_name: hypixel_proxy
    ports:
      - "25565:25565"
    depends_on:
      mongodb:
        condition: service_healthy
      redis:
        condition: service_healthy
    volumes:
      - ./configuration:/app/configuration_files
    networks:
      - hypixel_network
    healthcheck:
      test: ["CMD", "nc", "-z", "localhost", "25565"]
      interval: 10s
      timeout: 5s
      retries: 5
      start_period: 10s

  game_server_builder:
    build:
      context: .
      dockerfile: "./DockerFiles/Dockerfile.game_server"
    image: game_server_prepared
    container_name: game_server_builder
    volumes:
      - ./configuration:/app/configuration_files
    networks:
      - hypixel_network

  nanolimbo:
    image: game_server_prepared
    container_name: nanolimbo
    restart: "unless-stopped"
    environment:
      SERVICE_CMD: java -jar NanoLimbo-1.10.2.jar
    depends_on:
      proxy:
        condition: service_healthy
      game_server_builder:
        condition: service_started
    volumes:
      - ./configuration:/app/configuration_files
    networks:
      - hypixel_network

COMPOSE_HEAD

    for server in "${SELECTED_SERVERS[@]}"; do
        local name_lower
        name_lower=$(echo "$server" | tr '[:upper:]' '[:lower:]')
        cat >> "$compose_file" <<GAME_SERVER
  hypixelcore_${name_lower}:
    image: game_server_prepared
    container_name: hypixelcore_${name_lower}
    restart: "unless-stopped"
    environment:
      SERVICE_CMD: java -jar HypixelCore.jar ${server}
    depends_on:
      proxy:
        condition: service_healthy
      game_server_builder:
        condition: service_started
    volumes:
      - ./configuration:/app/configuration_files
    networks:
      - hypixel_network

GAME_SERVER
    done

    for service in "${SELECTED_SERVICES[@]}"; do
        local svc_lower
        svc_lower=$(echo "$service" | sed 's/^Service//' | tr '[:upper:]' '[:lower:]')

        local svc_cmd="java -jar ${service}.jar"
        local svc_ports=""
        if [[ "$service" == "ServiceAPI" && -n "${API_PORT:-}" ]]; then
            svc_cmd="java -jar ${service}.jar --port=${API_PORT}"
            svc_ports="    ports:
      - \"${API_PORT}:${API_PORT}\""
        fi

        {
            echo "  service_${svc_lower}:"
            echo "    image: game_server_prepared"
            echo "    container_name: service_${svc_lower}"
            echo "    restart: \"unless-stopped\""
            echo "    environment:"
            echo "      SERVICE_CMD: ${svc_cmd}"
            echo "    depends_on:"
            echo "      proxy:"
            echo "        condition: service_healthy"
            echo "      game_server_builder:"
            echo "        condition: service_started"
            echo "    volumes:"
            echo "      - ./configuration:/app/configuration_files"
            [[ -n "$svc_ports" ]] && echo "$svc_ports"
            echo "    networks:"
            echo "      - hypixel_network"
            echo ""
        } >> "$compose_file"
    done

    cat >> "$compose_file" <<COMPOSE_TAIL
volumes:
  mongodb-data:
    driver: local
    name: mongo-data

networks:
  hypixel_network:
    driver: bridge
COMPOSE_TAIL

    log_ok "Generated docker-compose.yml (${#SELECTED_SERVERS[@]} servers, ${#SELECTED_SERVICES[@]} services)"
}

save_state() {
    local servers_json services_json
    servers_json=$(printf '%s\n' "${SELECTED_SERVERS[@]}" | jq -R . | jq -s .)
    services_json=$(printf '%s\n' "${SELECTED_SERVICES[@]}" | jq -R . | jq -s .)

    cat > "${INSTALL_DIR}/${STATE_FILE}" <<STATE
{
    "version": "${VERSION}",
    "install_dir": "${INSTALL_DIR}",
    "bind_ip": "${BIND_IP}",
    "online_mode": ${ONLINE_MODE},
    "selected_servers": ${servers_json},
    "selected_services": ${services_json},
    "expose_ports": ${EXPOSE_PORTS},
    "api_port": "${API_PORT:-}",
    "last_release_id": "",
    "last_update_check": "",
    "installed_at": "$(date -Iseconds)"
}
STATE

    cp "$(realpath "${BASH_SOURCE[0]%/*}/../install.sh")" "${INSTALL_DIR}/install.sh" 2>/dev/null || true
    mkdir -p "${INSTALL_DIR}/lib"
    cp "$(realpath "${BASH_SOURCE[0]%/*}")"/*.sh "${INSTALL_DIR}/lib/" 2>/dev/null || true
    chmod +x "${INSTALL_DIR}/install.sh" 2>/dev/null || true
}
