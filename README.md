> This is a Minestom-implementation of a Hypixel-SkyBlock recreation hosted on my server at discord.gg/atlasmc. This implementation is nowhere near complete and is not ready for production. There are still portions of the codebase which are messy and aren't following proper Minestom standard practice.
# Hypixel SkyBlock

[<img src="https://discordapp.com/assets/e4923594e694a21542a489471ecffa50.svg" alt="" height="55" />](https://discord.gg/atlasmc)

A 1.20.1 recreation of HypixelSkyBlock utilizing Minestom, with the intention of actually having a properly abstracted, scalable codebase.

#### Releases

Releases are auto deployed on push onto the GitHub releases page which can be found [here](https://github.com/Swofty-Developments/HypixelSkyBlock/releases). Updates are also periodically sent within my discord server located at [discord.gg/atlasmc](discord.gg/atlasmc).

#### Javadocs

Javadocs can be found [here](https://swofty-developments.github.io/HypixelSkyBlock/).

## Setup Guide

1. Start a MongoDB service either locally or remotely, a guide for installation can be found [here](https://www.mongodb.com/docs/manual/installation/).
2. Ensure you have the `Java 17` SDK installed.
3. Start a Redis server, if you're on Windows you can run an installer [here](https://www.memurai.com/)
4. Follow the 'Proxy Setup Guide' below.
5. Follow the 'Game Server Setup Guide' below.

### Proxy Setup Guide
1. Download 'SkyBlockProxy.jar' from the releases page [here](https://github.com/Swofty-Developments/HypixelSkyBlock/releases/tag/latest)
2. Download the Velocity proxy from [here](https://api.papermc.io/v2/projects/velocity/versions/3.3.0-SNAPSHOT/builds/316/downloads/velocity-3.3.0-SNAPSHOT-316.jar)
3. Download `velocity.toml` from [here](https://github.com/Swofty-Developments/HypixelSkyBlock/tree/master/configuration) and move it to where you want your Proxy server to run.
4. Move your Velocity proxy JAR into that folder aswell, and run the proxy using `java -jar velocity-3.3.0-SNAPSHOT-316.jar` as a command in that directory.
5. Close this proxy once it has generated the `plugins` folder, just by pressing `CTRL + C` or closing the CMD Prompt.
6. Move the `SkyBlockProxy.jar` from earlier into the plugins folder.
7. Make a new folder where your `velocity.toml` is and call it `configuration`
8. Download `resources.json` from [here](https://github.com/Swofty-Developments/HypixelSkyBlock/tree/master/configuration)
9. Move this file into the `configuration` folder you just made.
10. Start the proxy again using `java -jar velocity-3.3.0-SNAPSHOT-316.jar`. This will need to be on for your game servers to work.

### Game Server Setup Guide
1. Download 'SkyBlockCore.jar' from the releases page [here](https://github.com/Swofty-Developments/HypixelSkyBlock/releases/tag/latest)
2. Make a folder called `configuration` in the same directory as the JAR file. (Note this should be placed differently to where your Proxy is)
3. Download `resources.json` from [here](https://github.com/Swofty-Developments/HypixelSkyBlock/tree/master/configuration)
4. Move this file into the `configuration` folder you just made.
5. Download the [world files for the Hub and Island worlds.](https://www.mediafire.com/file/ugdq2fycq0weval/HypixelSkyBlockMinestom.zip/file)
6. Get the Hypixel SkyBlock hub from the above download and put it in the same directory as your jar under the name `hypixel_hub`.
7. Get the Hypixel Island default template from the above download and put it in the same directory as your jar under the name `hypixel_island_template`.
8. There should be a `forwarding.secret` file where your Velocity JAR is, take this and put it into your `resources.json` under `velocity-secret`.
9. Run the jar using `java -jar {Insert the JAR file} ISLAND`, this will create an Island server that will latch onto your running proxy.
10. To make other game servers for the other islands merely run the command above again but with different island types, you can see all the possible types [here](https://github.com/Swofty-Developments/HypixelSkyBlock/blob/7df2db59ef0f14281f332d2cf43fdbf8ab09e574/commons/src/main/java/net/swofty/commons/ServerType.java#L4).
11. (OPTIONAL) If you wish to have regions, download the CSV of the regions from this Git repo and upload them to the `regions` collection made in your Mongo after starting the server. Once you have done this restart your server.

## Credits

Thanks to:
* All the lovely people in the Minestom discord for single-handedly carrying all of my knowledge about this API.
* Myself and any other contributors, which can be viewed on this Git page.
