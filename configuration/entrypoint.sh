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


# Clear any previously created Screens (Starting freshhh)
screen -wipe

# Start services in separate screen sessions
screen -dmS HypixelCore_ISLAND java --enable-preview -jar HypixelCore.jar SKYBLOCK_ISLAND
sleep 20
screen -dmS HypixelCore_HUB java --enable-preview -jar HypixelCore.jar SKYBLOCK_HUB
screen -dmS HypixelCore_FARMING java --enable-preview -jar HypixelCore.jar SKYBLOCK_THE_FARMING_ISLANDS
screen -dmS PROTOTYPE_LOBBY java --enable-preview -jar HypixelCore.jar PROTOTYPE_LOBBY
screen -dmS BEDWARS_LOBBY java --enable-preview -jar HypixelCore.jar BEDWARS_LOBBY
screen -dmS NanoLimbo java -jar NanoLimbo-1.9.8.jar
screen -dmS ServiceAPI java -jar ServiceAPI.jar
screen -dmS ServiceAuctionHouse java -jar ServiceAuctionHouse.jar
screen -dmS ServiceBazaar java -jar ServiceBazaar.jar
screen -dmS ServiceItemTracker java -jar ServiceItemTracker.jar
screen -dmS ServiceParty java -jar ServiceParty.jar
echo "Started all services with a total of: $(screen -ls | grep -c 'Detached') screens"

# Keep the container running
tail -f /dev/null