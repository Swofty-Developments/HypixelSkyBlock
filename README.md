> This is a Minestom-implementation of a Hypixel-SkyBlock recreation hosted on my server at discord.gg/atlasmc. This implementation is nowhere near complete and is not ready for production. There are still portions of the codebase which are messy and aren't following proper Minestom standard practice.
# Hypixel SkyBlock

[<img src="https://discordapp.com/assets/e4923594e694a21542a489471ecffa50.svg" alt="" height="55" />](https://discord.gg/atlasmc)

A 1.20.1 recreation of HypixelSkyBlock utilizing Minestom, with the intention of actually having a properly abstracted, scalable codebase.

#### Releases

Releases are auto deployed on push onto the GitHub releases page which can be found [here](https://github.com/Swofty-Developments/HypixelSkyBlock/releases). Updates are also periodically sent within my discord server located at [discord.gg/atlasmc](discord.gg/atlasmc).

#### Javadocs

Javadocs can be found [here](https://swofty-developments.github.io/HypixelSkyBlock/apidocs/).

## Execution Guide

1. Start a MongoDB service either locally or remotely, a guide for installation can be found [here](https://www.mongodb.com/docs/manual/installation/).
2. Ensure you have the `Java 17` SDK installed.
3. Download the JAR file from the latest release on the GitHub page, it should be named `skyblock-1.0-SNAPSHOT.jar`.
4. Scroll down to Setup Guide to continue.

## Compilation Guide

1. Start a MongoDB service either locally or remotely, a guide for installation can be found [here](https://www.mongodb.com/docs/manual/installation/).
2. Load the project into your IntelliJ or in another Maven-enabled environment.
3. Ensure you have the `Java 17` JDK installed.
4. Run the `package` lifecycle either through `mvn package` or your Maven-enabled environment.
5. Scroll down to Setup Guide to continue.

## Setup Guide
1. With the resulting JAR file insert a `resources.json` in the same directory with the following setup ensuring you change the values in accordance to your setup;
   {"mongodb": "mongodb://localhost", "cracked_domain": "0.0.0.0"}
2. Download a world copy of the Hypixel SkyBlock hub and put it in the same directory as your jar under the name `hypixel_hub`.
2. Download a world copy of the Hypixel Island default template and put it in the same directory as your jar under the name `hypixel_island_template`.
3. Run the jar using `java -jar {Insert the JAR file}` and connect through port 25530.
4. (OPTIONAL) If you wish to have regions, download the CSV of the regions from this Git repo and upload them to the `regions` collection made in your Mongo after starting the server. Once you have done this restart your server.

## Credits

Thanks to:
* All of the lovely people in the Minestom discord for single handedly carrying all of my knowledge about this API.
* Myself and any other contributors which can be viewed on this Git page.
