#!/bin/bash

if [ -n "$FORWARDING_SECRET" ]; then
    secret="$FORWARDING_SECRET"
elif [ -f ./configuration_files/forwarding.secret ]; then
    secret=$(cat ./configuration_files/forwarding.secret)
else
    echo "FORWARDING_SECRET is required (env var or configuration_files/forwarding.secret)" >&2
    exit 1
fi

printf '%s' "$secret" > ./forwarding.secret

# Ensure configuration/config.yml exists
mkdir -p ./configuration
if [ ! -f ./configuration/config.yml ]; then
    if [ -f ./configuration_files/config.docker.yml ]; then
        cp ./configuration_files/config.docker.yml ./configuration/config.yml
    else
        cp ./configuration_files/config.example.yml ./configuration/config.yml
    fi
fi

# Mirror any packaged configuration directories the runtime expects
for source_dir in ./configuration_files/*/; do
    [ -d "$source_dir" ] || continue
    dir_name=$(basename "$source_dir")
    if [ ! -d "./configuration/$dir_name" ]; then
        cp -a "$source_dir" "./configuration/$dir_name"
    fi
done

# Update config.yml with the forwarding secret (velocity-secret)
sed -i "s/velocity-secret: .*/velocity-secret: '$secret'/" ./configuration/config.yml

echo "$SERVICE_CMD"
exec $SERVICE_CMD
