> This is a Minestom-implementation of a Hypixel-SkyBlock recreation hosted on my server at discord.gg/atlasmc. This implementation is nowhere near complete and is not ready for production. There are still portions of the codebase which are messy and aren't following proper Minestom standard practice.
# Hypixel SkyBlock

[<img src="https://discordapp.com/assets/e4923594e694a21542a489471ecffa50.svg" alt="" height="55" />](https://discord.gg/atlasmc)

A 1.8 to 1.20.4 recreation of HypixelSkyBlock utilizing Minestom, with the intention of actually having a properly abstracted, scalable codebase.

#### Releases

Releases are auto deployed on push onto the GitHub releases page which can be found [here](https://github.com/Swofty-Developments/HypixelSkyBlock/releases). Updates are also periodically sent within my discord server located at [discord.gg/atlasmc](discord.gg/atlasmc).

#### Javadocs

Javadocs can be found [here](https://swofty-developments.github.io/HypixelSkyBlock/).

## Recommended Device Specifications
This project is not meant to be a small server, and as such, it requires a decent amount of resources to run. The following are the minimum specifications for running this server:
- 16GB of RAM (4GB for MongoDB, 12GB across servers and services)
- 6 Cores (For sufficient multi-threading)
- 15GB of Storage

## Setup Guide
A video of me going through the guide [can be found here](https://www.youtube.com/watch?v=pxzJbjjQL-M)
1. Ensure that you meet the recommended device specifications above.
2. Start a MongoDB service either locally or remotely, a guide for installation can be found [here](https://www.mongodb.com/try/download/community?tck=docs_server).
3. Ensure you have the `Java 21` SDK installed.
4. Start a Redis server, if you're on Windows you can run an installer [here](https://www.memurai.com/)
5. Follow the 'Proxy Setup Guide' below. (Note, if you want to run a cracked server, you must set "require-authentication" to true in your config)
6. Follow the 'Game Server Setup Guide' below. (Note, if you want to run a cracked server, you must set "require-authentication" to true in your config)
7. Follow the 'Service Setup Guide' below.
8. Follow the 'Resource Pack Setup Guide' below.
9. To give yourself ADMIN, log in and out of the server, go into your MongoDB compass, click on Minestom -> data, find your profile and set your rank to "ADMIN". Log back in and you'll have it.

### Proxy Setup Guide
1. Download 'SkyBlockProxy.jar' from the releases page [here](https://github.com/Swofty-Developments/HypixelSkyBlock/releases/tag/latest)
2. Download the Velocity proxy from [here](https://api.papermc.io/v2/projects/velocity/versions/3.3.0-SNAPSHOT/builds/388/downloads/velocity-3.3.0-SNAPSHOT-388.jar)
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
5. Download the [world files for the Hub and Island worlds.](https://www.mediafire.com/file/6zcvolfjk5bislu/HypixelSkyBlockMinestom.zip/file)
6. Get the Hypixel SkyBlock hub from the above download and put it in the configuration folder you made under the name `hypixel_hub`.
7. Get the Hypixel Island default template from the above download and put it in the configuration folder you made under the name `hypixel_island_template`.
8. There should be a `forwarding.secret` file where your Velocity JAR is, take this and put it into your `resources.json` under `velocity-secret`.
9. Run the jar using `java -jar {Insert the JAR file} ISLAND`, this will create an Island server that will latch onto your running proxy.
10. To make other game servers for the other islands merely run the command above again but with different island types, you can see all the possible types [here](https://github.com/Swofty-Developments/HypixelSkyBlock/blob/7df2db59ef0f14281f332d2cf43fdbf8ab09e574/commons/src/main/java/net/swofty/commons/ServerType.java#L4).
11. Download `NanoLimbo-1.7-all.jar` from [here](https://github.com/Swofty-Developments/HypixelSkyBlock/tree/master/configuration) and start it in the background using `java -jar NanoLimbo-1.7-all.jar`.
12. (OPTIONAL) If you wish to have regions, download the CSV of the regions from this Git repo and upload them to the `regions` collection made in your Mongo after starting the server. Once you have done this restart your server.
13. (OPTIONAL) If you wish to have fairy souls, download the CSV of the fairy souls from this Git repo and upload them to the `fairysouls` collection made in your Mongo after starting the server. Once you have done this restart your server.
14. (OPTIONAL) If you wish to have the Hub crystals (you can also just `/addcrystal`), download the CSV of the collections from this Git repo and upload them to the `crystals` collection made in your Mongo after starting the server. Once you have done this restart your server.
15. (OPTIONAL) If you wish to have Songs on your server, copy the `songs` folder from [here](https://github.com/Swofty-Developments/HypixelSkyBlock/tree/master/configuration), and put it inside of your configuration folder.

### Service Setup Guide
1. Due to the nature of SkyBlock, there may be a variety of services that need to be ran. Go to the releases page [here](https://github.com/Swofty-Developments/HypixelSkyBlock/releases/tag/latest) and download any .JAR files that start with `Service`.
2. Move these JAR files into the same directory as your Game Servers, they will share the configuration JSON with the services.
3. Run them using `java -jar {Insert the JAR file}`.

### Resource Pack Setup Guide
1. In preparation for SkyBlock version 1, we already have a resource pack system setup. To start, download the `SkyBlockPacker.jar` from the releases page [here](https://github.com/Swofty-Developments/HypixelSkyBlock/releases/tag/latest).
2. Download the [pack_textures](https://github.com/Swofty-Developments/HypixelSkyBlock/tree/master/configuration) and [SkyBlockPack](https://github.com/Swofty-Developments/HypixelSkyBlock/tree/master/configuration) folders and move them where the packer JAR is.
3. Run the packer JAR using `java -jar SkyBlockPacker.jar -v (Location of SkyBlockPack) -o (Output Directory) -t (Location of Pack Textures)`.
4. Once this has finished, you should have a resource pack in the output directory you specified. Merely apply this on Minecraft and you'll be good to go.

## Credits

Thanks to:
* All the lovely people in the Minestom discord for single-handedly carrying all of my knowledge about this API.
* Myself and any other contributors, which can be viewed on this Git page.
