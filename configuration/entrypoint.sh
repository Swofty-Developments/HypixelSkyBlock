#!/bin/bash

# Copy the Forwarding Secret
cp configuration_files/forwarding.secret ./forwarding.secret

# Copy the example config.yml if it doesn't exist
if [ ! -f ./config.yml ]; then
    cp configuration_files/config.example.yml ./config.yml
fi

# Update config.yml with the forwarding secret (velocity-secret)
secret=$(cat ./forwarding.secret)
sed -i "s/velocity-secret: .*/velocity-secret: '$secret'/" ./config.yml

# Replace the secret in settings.yml
sed -i "s/secret: '.*'/secret: '$secret'/" ./settings.yml

# Set the settings.yml bind: ip: 'localhost' to bind: ip: '0.0.0.0'
sed -i "s/ip: 'localhost'/ip: '0.0.0.0'/" ./settings.yml

echo "$SERVICE_CMD"
exec $SERVICE_CMD