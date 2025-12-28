# Introduction

HypixelSkyBlock is a Minestom-based recreation of Hypixel's SkyBlock gamemode. This project aims to provide a properly abstracted, scalable codebase for running your own SkyBlock server.

:::alert note
This implementation is under active development and is not yet production-ready. Some portions of the codebase are still being refined.
:::

## Features

- **Multi-Server Architecture** - Game logic distributed across multiple Minestom servers by ServerType
- **Microservices Pattern** - Each major feature runs as an independent service
- **Redis Communication** - Real-time inter-service communication via Redis pub/sub
- **MongoDB Storage** - Persistent data storage for profiles, auctions, and more
- **Velocity Proxy** - Single entry point with load balancing support
- **Java 25** - Uses modern Java features including virtual threads

## Project Structure

The project is organized into several module types:

| Module Type          | Purpose                                                   |
|----------------------|-----------------------------------------------------------|
| `commons`            | Shared enums, configs, and protocols                      |
| `service.*`          | Independent microservices (API, Auctions, Bazaar, etc.)   |
| `type.*`             | Server type implementations (Hub, Island, Dungeons, etc.) |
| `loader`             | Main entry point (HypixelCore.jar)                        |
| `velocity.extension` | Velocity proxy plugin                                     |
| `packer`             | Resource pack builder                                     |

## Related Projects

This project is designed to work with [HypixelForums](https://github.com/Swofty-Developments/HypixelForums) for a complete forum and website experience.

## Getting Help

- **Discord**: [discord.gg/ZaGW5wzUJ3](https://discord.gg/ZaGW5wzUJ3)
- **Javadocs**: [swofty-developments.github.io/HypixelSkyBlock](https://swofty-developments.github.io/HypixelSkyBlock/)
- **Video Guide**: [YouTube Setup Tutorial](https://www.youtube.com/watch?v=pxzJbjjQL-M)
