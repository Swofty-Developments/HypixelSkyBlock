> This is a Minestom-implementation of a Hypixel-SkyBlock recreation hosted on my server at discord.gg/paper. This implementation is nowhere near complete and is not ready for production. There are still portions of the codebase which are messy and aren't following proper Minestom standard practice.
# Hypixel SkyBlock

[<img src="https://discordapp.com/assets/e4923594e694a21542a489471ecffa50.svg" alt="" height="55" />](https://discord.gg/paper)

A 1.8 to 1.21.4 recreation of HypixelSkyBlock utilizing Minestom, with the intention of actually having a properly abstracted, scalable codebase. This project is sisters with the [HypixelForums](https://github.com/Swofty-Developments/HypixelForums) project and optionally, can be used together.

#### Releases

Releases are auto deployed on push onto the GitHub releases page which can be found [here](https://github.com/Swofty-Developments/HypixelSkyBlock/releases). Updates are also periodically sent within my discord server located at [discord.gg/paper](https://discord.gg/discord.gg/paper).

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
2. Download the Velocity proxy from [here](https://api.papermc.io/v2/projects/velocity/versions/3.4.0-SNAPSHOT/builds/463/downloads/velocity-3.4.0-SNAPSHOT-463.jar)
3. Download `velocity.toml` from [here](https://github.com/Swofty-Developments/HypixelSkyBlock/tree/master/configuration) and move it to where you want your Proxy server to run.
4. Move your Velocity proxy JAR into that folder aswell, and run the proxy using `java -jar velocity-3.4.0-SNAPSHOT-463.jar` as a command in that directory.
5. Close this proxy once it has generated the `plugins` folder, just by pressing `CTRL + C` or closing the CMD Prompt.
6. Move the `SkyBlockProxy.jar` from earlier into the plugins folder.
7. Make a new folder where your `velocity.toml` is and call it `configuration`
8. Download `resources.json` from [here](https://github.com/Swofty-Developments/HypixelSkyBlock/tree/master/configuration)
9. Move this file into the `configuration` folder you just made.
10. Start the proxy again using `java -jar velocity-3.4.0-SNAPSHOT-463.jar`. This will need to be on for your game servers to work.

### Game Server Setup Guide
1. Download 'SkyBlockCore.jar' from the releases page [here](https://github.com/Swofty-Developments/HypixelSkyBlock/releases/tag/latest)
2. Make a folder called `configuration` in the same directory as the JAR file. (Note this should be placed differently to where your Proxy is)
3. Download `resources.json` from [here](https://github.com/Swofty-Developments/HypixelSkyBlock/tree/master/configuration)
4. Move this file into the `configuration` folder you just made.
5. Download the [world files for the Hub and Island worlds.](https://www.mediafire.com/file/5oko5y8h7uj6i9z/world.zip/file)
6. Get the Hypixel SkyBlock hub from the above download and put it in the configuration folder you made under the name `hypixel_hub`.
7. Get the Hypixel Island default template from the above download and put it in the configuration folder you made under the name `hypixel_island_template`.
8. Get the item and collection folders from [here](https://github.com/Swofty-Developments/HypixelSkyBlock/tree/master/configuration) and put them into your configuration folder.
9. There should be a `forwarding.secret` file where your Velocity JAR is, take this and put it into your `resources.json` under `velocity-secret`.
10. Run the jar using `java -jar {Insert the JAR file} ISLAND`, this will create an Island server that will latch onto your running proxy.
11. To make other game servers for the other islands merely run the command above again but with different island types, you can see all the possible types [here](https://github.com/Swofty-Developments/HypixelSkyBlock/blob/7df2db59ef0f14281f332d2cf43fdbf8ab09e574/commons/src/main/java/net/swofty/commons/ServerType.java#L4).
12. Download `NanoLimbo.jar` from [here](https://github.com/Swofty-Developments/HypixelSkyBlock/tree/master/configuration), and download its configuration file from the same place. Start it in the background using `java -jar NanoLimbo-1.9.1-all.jar`.
13. Download `Minestom.regions.csv` from [here](https://github.com/Swofty-Developments/HypixelSkyBlock/tree/master/configuration) and upload them to the `regions` collection made in your Mongo after starting the server. Once you have done this restart your server.
14. (OPTIONAL) If you wish to have fairy souls, download `Minestom.fairysouls.csv` from [here](https://github.com/Swofty-Developments/HypixelSkyBlock/tree/master/configuration) and upload them to the `fairysouls` collection made in your Mongo after starting the server. Once you have done this restart your server.
15. (OPTIONAL) If you wish to have the Hub crystals (you can also just `/addcrystal`), download `Minestom.crystals.csv` from [here](https://github.com/Swofty-Developments/HypixelSkyBlock/tree/master/configuration) and upload them to the `crystals` collection made in your Mongo after starting the server. Once you have done this restart your server.
16. (OPTIONAL) If you wish to have Songs on your server, copy the `songs` folder from [here](https://github.com/Swofty-Developments/HypixelSkyBlock/tree/master/configuration), and put it inside your configuration folder.

### Service Setup Guide
1. Due to the nature of SkyBlock, there may be a variety of services that need to be ran. Go to the releases page [here](https://github.com/Swofty-Developments/HypixelSkyBlock/releases/tag/latest) and download any .JAR files that start with `Service`.
2. Move these JAR files into the same directory as your Game Servers, they will share the configuration JSON with the services.
3. Run them using `java -jar {Insert the JAR file}`.

### Forums Website Setup Guide
1. Ensure that your API service is running.
2. Follow the steps found at [the HypixelForums repository](https://github.com/Swofty-Developments/HypixelForums).

### Resource Pack Setup Guide
1. In preparation for SkyBlock version 1, we already have a resource pack system setup. To start, download the `SkyBlockPacker.jar` from the releases page [here](https://github.com/Swofty-Developments/HypixelSkyBlock/releases/tag/latest).
2. Download the [pack_textures](https://github.com/Swofty-Developments/HypixelSkyBlock/tree/master/configuration) and [SkyBlockPack](https://github.com/Swofty-Developments/HypixelSkyBlock/tree/master/configuration) folders and move them where the packer JAR is.
3. Run the packer JAR using `java -jar SkyBlockPacker.jar -v (Location of SkyBlockPack) -o (Output Directory) -t (Location of Pack Textures)`.
4. Once this has finished, you should have a resource pack in the output directory you specified. Merely apply this on Minecraft and you'll be good to go.


# 🐋 Running with Docker
- Install the git repository using `git clone https://github.com/Swofty-Developments/HypixelSkyBlock.git

Most parts about downloading and setting up files will be automated for you but you will need to add the following files to the `configuration` folder:

- Download the [world files for the Hub and Island worlds.](https://www.mediafire.com/file/5oko5y8h7uj6i9z/world.zip/file). Make sure they are named as **world.zip**

- In your configuration file Remove the default `resources.json` and rename `resources.json.docker` to `resources.json`

## Running the contaniners! 

- (Recommended) Install Docker Desktop from [here](https://www.docker.com/products/docker-desktop).
- Open Docker Desktop/Docker and ensure that it is running.
- Open a terminal and navigate to the directory where you cloned the repository.
- Run the following command to build and run the containers (Your Server):

```bash
docker-compose up --build
```

- If you want to run the server in detached mode, you can use the `-d` flag:

```bash
docker-compose up --build -d
```
> Note: If you have already runned the server before with `docker-compose up --build` you can just run `docker-compose up` to start the server without rebuilding the images making it faster.

- To stop the containers, you can use:

```bash
docker-compose down
```

Or click the stop button in Docker Desktop.

### How to add new servers using Docker?
To add new servers like the Hub, Islands or Farming Island follow these steps:
1. Open the `entrypoint.sh` file in the `configuration` folder.
2. Add the following line to the end of the file, replacing `ServerType` with the type of server you want to add (e.g., `HUB`, `ISLAND`, etc.):

```bash
screen -dmS SkyBlockCore_HUB java --enable-preview -jar SkyBlockCore.jar ServerType
```

3. Save the file and run `docker-compose up --build` again to apply the changes.


If you want to run services directly in the container you can attach to the container using:

```bash
docker-compose exec game_server /bin/bash
```

or go to the `exec` tab in Docker Desktop.

and then run the service command inside there. It will create its own screen session for you to run the service in.


### Common Issues
1. `redis.clients.jedis.exceptions.JedisConnectionException: Failed to connect to any host resolved for DNS name.`

    => Your Memurai isn't running.
2. `Preview features are not enabled for... Try running with '--enable-preview'`

    => try `java --enable-preview -jar SkyBlockCore.jar {ServerType}`

## Credits

Thanks to:
* All the lovely people in the Minestom discord for single-handedly carrying all of my knowledge about this API.
* Myself and any other contributors, which can be viewed on this Git page.
