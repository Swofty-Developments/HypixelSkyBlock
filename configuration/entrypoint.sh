#!/bin/bash

# Copy the Forwarding Secret
cp configuration_files/forwarding.secret ./forwarding.secret

# Update resources.json with the forwarding secret
secret=$(cat ./forwarding.secret)
jq --arg secret "$secret" '.["velocity-secret"] = $secret' ./configuration/resources.json > ./configuration/resources.json.tmp
mv ./configuration/resources.json.tmp ./configuration/resources.json

# Replace the secret in settings.yml
sed -i "s/secret: '.*'/secret: '$secret'/" ./settings.yml


# Clear any previously created Screens (Starting freshhh)
screen -wipe

# Start services in separate screen sessions
screen -dmS HypixelCore_ISLAND java --enable-preview -jar HypixelCore.jar ISLAND
sleep 20
screen -dmS HypixelCore_HUB java --enable-preview -jar HypixelCore.jar HUB
screen -dmS HypixelCore_FARMING java --enable-preview -jar HypixelCore.jar THE_FARMING_ISLANDS
screen -dmS NanoLimbo java -jar NanoLimbo-1.9.1-all.jar
screen -dmS PrototypeLobby java -jar HypixelCore.jar PROTOTYPE_LOBBY
screen -dmS PrototypeLobby java -jar HypixelCore.jar BEDWARS_LOBBY
screen -dmS PrototypeLobby java -jar HypixelCore.jar BEDWARS_GAME
screen -dmS ServiceAPI java -jar ServiceAPI.jar
screen -dmS ServiceAuctionHouse java -jar ServiceAuctionHouse.jar
screen -dmS ServiceBazaar java -jar ServiceBazaar.jar
screen -dmS ServiceItemTracker java -jar ServiceItemTracker.jar
screen -dmS ServiceDataMutex java -jar ServiceDataMutex.jar

echo "Started all services with a total of: $(screen -ls | grep -c 'Detached') screens"

# Keep the container running
tail -f /dev/null