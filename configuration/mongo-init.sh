#!/bin/bash

echo "Importing regions..."
mongoimport --db Minestom --collection regions --type csv --ignoreBlanks --headerline --file /csv/skyblock/Minestom.regions.csv || echo "Failed to import regions"

echo "Importing crystals..."
mongoimport --db Minestom --collection crystals --type csv --ignoreBlanks --headerline --file /csv/skyblock/Minestom.crystals.csv || echo "Failed to import crystals"
