# Swofty AntiCheat

[<img src="https://discordapp.com/assets/e4923594e694a21542a489471ecffa50.svg" alt="" height="55" />](https://discord.gg/atlasmc)

An advanced prediction-based anticheat for both Minestom and Spigot/Bukkit servers. Features comprehensive packet tracking, movement prediction with physics simulation, and lag compensation.

## Table of contents

* [Features](#features)
* [Getting Started](#getting-started)
* [Minestom Loader](#minestom-loader)
* [Spigot Loader](#spigot-loader)
* [License](#license)
* [Credits](#credits)

## Features

### Prediction-Based Movement System
- Physics-accurate movement simulation with gravity, friction, and velocity modifiers
- 14 velocity modifiers (sprint, speed effects, soul speed, depth strider, soul sand, cobweb, honey, bubble columns, levitation, slow falling, sneak, using item, dolphins grace)
- 3 friction modifiers (ice, blue ice, slime blocks)
- Modular modifier architecture with priority-based application

### Advanced Packet Tracking (14 packet types)
- **Movement packets**: Position, rotation, position+rotation
- **Player state**: Entity actions (sprint/sneak/elytra), abilities (flying state)
- **Combat**: Use entity (attack/interact), animation (arm swing)
- **Block interaction**: Block dig (mining), block place
- **Inventory**: Window click, held item change
- **Vehicle**: Steer vehicle (boats/horses)
- **Network**: Ping/pong for latency tracking

### Lag Compensation
- Transaction-based snapshot system
- Ping uncertainty calculations
- Lag spike detection and check skipping
- Per-player latency tracking

### Detection Flags (15 checks)
- **Movement**: Prediction, Speed, Flight, Strafe, Phase, Jesus
- **Combat**: Reach, Velocity, KillAura, Aim
- **Misc**: Timer, AutoClicker, BadPackets, OnGroundSpoof, TimeoutPingPackets

### Cross-Platform Support
- Full Minestom support with native packet API
- Full Spigot/Bukkit support via ProtocolLib
- Unified packet abstraction layer
- Platform-specific scheduler managers

## Getting Started
SwoftyAnticheat requires Java 17+ and either Minestom or Spigot/Bukkit with ProtocolLib. 

## Minestom Loader
To use SwoftyAnticheat with Minestom, create a new instance of the MinestomLoader and initialize the anticheat:

```java
MinestomLoader minestomLoader = new MinestomLoader();

SwoftyAnticheat.loader(minestomLoader);
SwoftyAnticheat.values(new SwoftyValues());
SwoftyAnticheat.start(); // Do this after MinecraftServer#init()
```

The MinestomLoader automatically registers all packet listeners using Minestom's native packet API.

## Spigot Loader
To use SwoftyAnticheat with Spigot/Bukkit, you need ProtocolLib installed. Then create a SpigotLoader instance:

```java
public class YourPlugin extends JavaPlugin {
    @Override
    public void onEnable() {
        SpigotLoader spigotLoader = new SpigotLoader(this);

        SwoftyAnticheat.loader(spigotLoader);
        SwoftyAnticheat.values(new SwoftyValues());
        SwoftyAnticheat.start();
    }
}
```

The SpigotLoader uses ProtocolLib to intercept and process all relevant packets.

## License
SwoftyAnticheat's directory is licensed under the permissive MIT license. Please see [`LICENSE.txt`](https://github.com/Swofty-Developments/HypixelSkyBlock/blob/master/anticheat/LICENSE.md) for more information.

## Credits
Thanks to:
* Myself and any other contributors, which can be viewed on this Git page.
