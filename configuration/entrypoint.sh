#!/bin/bash
cp configuration_files/forwarding.secret ./forwarding.secret
secret=$(cat ./forwarding.secret)
jq --arg secret "$secret" '.["velocity-secret"] = $secret' ./configuration/resources.json > ./configuration/resources.json.tmp
mv ./configuration/resources.json.tmp ./configuration/resources.json
sed -i "s/secret: '.*'/secret: '$secret'/" ./settings.yml
screen -wipe

# Start services in separate screen sessions
screen -dmS HypixelCore_ISLAND java --enable-preview -jar HypixelCore.jar SKYBLOCK_ISLAND
echo "Started HypixelCore_ISLAND"
sleep 20
screen -dmS HypixelCore_HUB java --enable-preview -jar HypixelCore.jar SKYBLOCK_HUB
echo "Started HypixelCore_HUB"
sleep 5
screen -dmS HypixelCore_PROTOTYPE java --enable-preview -jar HypixelCore.jar PROTOTYPE_LOBBY
echo "Started HypixelCore_PROTOTYPE"
sleep 5
screen -dmS HypixelCore_FARMING java --enable-preview -jar HypixelCore.jar SKYBLOCK_THE_FARMING_ISLANDS
echo "Started HypixelCore_FARMING"
sleep 5
screen -dmS NanoLimbo java -jar NanoLimbo-1.9.1-all.jar
echo "Started NanoLimbo"
sleep 5
screen -dmS ServiceAPI java -jar ServiceAPI.jar
echo "Started ServiceAPI"
sleep 5
screen -dmS ServiceAuctionHouse java -jar ServiceAuctionHouse.jar
echo "Started ServiceAuctionHouse"
sleep 5
screen -dmS ServiceBazaar java -jar ServiceBazaar.jar
echo "Started ServiceBazaar"
sleep 5
screen -dmS ServiceItemTracker java -jar ServiceItemTracker.jar
echo "Started ServiceItemTracker"
sleep 5
screen -dmS ServiceDataMutex java -jar ServiceDataMutex.jar
echo "Started ServiceDataMutex"
sleep 5
screen -dmS ServiceExperiments java -jar ServiceExperiments.jar
echo "Started ServiceExperiments"
echo "Started all services with a total of: $(screen -ls | grep -c 'Detached') screens"
tail -f /dev/null
