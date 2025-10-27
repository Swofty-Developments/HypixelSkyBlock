#!/bin/bash

# Copy the Forwarding Secret
cp configuration_files/forwarding.secret ./forwarding.secret

# Update resources.json with the forwarding secret
secret=$(cat ./forwarding.secret)
jq --arg secret "$secret" '.["velocity-secret"] = $secret' ./configuration/resources.json > ./configuration/resources.json.tmp
mv ./configuration/resources.json.tmp ./configuration/resources.json

# Replace the secret in settings.yml
sed -i "s/secret: '.*'/secret: '$secret'/" ./settings.yml

# Set the settings.yml bind: ip: 'localhost' to bind: ip: '0.0.0.0'
sed -i "s/ip: 'localhost'/ip: '0.0.0.0'/" ./settings.yml

echo "$SERVICE_CMD"
exec $SERVICE_CMD