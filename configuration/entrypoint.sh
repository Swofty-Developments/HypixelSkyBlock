#!/bin/bash
set -euo pipefail

JAVA_OPTS="${JAVA_OPTS:--XX:+UseG1GC -XX:+UseStringDeduplication -XX:MaxRAMPercentage=70 -XX:InitialRAMPercentage=25 -Dfile.encoding=UTF-8}"

if [ -n "$FORWARDING_SECRET" ]; then
    secret="$FORWARDING_SECRET"
elif [ -f ./configuration_files/forwarding.secret ]; then
    secret=$(cat ./configuration_files/forwarding.secret)
else
    echo "FORWARDING_SECRET is required (env var or configuration_files/forwarding.secret)" >&2
    exit 1
fi

printf '%s' "$secret" > ./forwarding.secret

./configuration_files/bootstrap-config.sh ./configuration

cp ./configuration_files/server.toml ./server.toml

copy_config_dir() {
    local source_dir="$1"
    local target_dir="./configuration/$1"
    if [ -d "./configuration_files/$source_dir" ]; then
        mkdir -p "$target_dir"
        cp -a "./configuration_files/$source_dir/." "$target_dir/"
    fi
}

copy_config_dir "skyblock"
copy_config_dir "achievements"
copy_config_dir "quests"
copy_config_dir "i18n"
copy_config_dir "bedwars"
copy_config_dir "murdermystery"
copy_config_dir "skywars"

for world_dir in ./configuration_files/hypixel_*; do
    if [ -d "$world_dir" ]; then
        world_name="$(basename "$world_dir")"
        mkdir -p "./configuration/$world_name"
        cp -a "$world_dir/." "./configuration/$world_name/"
    fi
done

if [ -n "${WORLD_ARCHIVE_URL:-}" ] && [ ! -f ./configuration/.worlds_loaded ]; then
    curl -fsSL "$WORLD_ARCHIVE_URL" -o /tmp/worlds.tar.gz
    tar -xzf /tmp/worlds.tar.gz -C ./configuration
    rm -f /tmp/worlds.tar.gz
    touch ./configuration/.worlds_loaded
fi

if [ -n "${MANAGEMENT_PORT:-}" ]; then
    export HYPIXEL_MANAGEMENT_PORT="$MANAGEMENT_PORT"
fi

echo "$SERVICE_CMD"
exec sh -lc "$SERVICE_CMD"
