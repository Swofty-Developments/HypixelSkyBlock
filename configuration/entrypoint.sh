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
    cp configuration_files/config.example.yml ./configuration/config.yml
fi

# Update config.yml with the forwarding secret (velocity-secret)
sed -i "s/velocity-secret: .*/velocity-secret: '$secret'/" ./configuration/config.yml

echo "$SERVICE_CMD"
exec $SERVICE_CMD