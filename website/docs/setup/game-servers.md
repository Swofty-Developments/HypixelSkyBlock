# Game Servers Setup

Game servers run the actual gameplay using Minestom. Each server runs as a specific ServerType.

## Download Required Files

1. Download `HypixelCore.jar` from the [releases page](https://github.com/Swofty-Developments/HypixelSkyBlock/releases/tag/latest)
2. Download [`config.yml`](https://github.com/Swofty-Developments/HypixelSkyBlock/tree/master/configuration)
3. Download [world files](https://files.catbox.moe/of7snu.zip)
4. Download [`PicoLimbo.jar`](https://github.com/Swofty-Developments/HypixelSkyBlock/tree/master/configuration) and its config

## Directory Structure

```
gameserver/
├── HypixelCore.jar
├── configuration/
│   ├── config.yml
│   ├── skyblock/
│   │   ├── islands/
│   │   │   ├── hypixel_skyblock_hub/
│   │   │   ├── hypixel_skyblock_island_template/
│   │   │   └── ... (other islands)
│   │   ├── collections/
│   │   ├── items/
│   │   ├── levels/
│   │   ├── reforges/
│   │   ├── skills/
│   │   ├── pack_textures/    # Optional
│   │   └── songs/            # Optional
│   ├── bedwars/              # BedWars maps (.polar)
│   ├── murdermystery/        # Murder Mystery maps (.polar)
│   ├── hypixel_prototype_lobby/
│   ├── hypixel_bedwars_lobby/
│   └── hypixel_murder_mystery_lobby/
```

## Setup Steps

### 1. Create Directory Structure

```bash
mkdir -p gameserver/configuration/worldbac
```

### 2. Configure config.yml

Copy the `forwarding.secret` from your Velocity proxy directory and add it to `config.yml`:

```yaml
host-name: 0.0.0.0
transfer-timeout: 800
mongodb: mongodb://localhost
redis-url: redis://localhost:6379
velocity-secret: your-forwarding-secret-here
require-auth: false
sandbox: false
spark: false
anticheat: false
redis-uri: redis://localhost:6379
limbo:
  host-name: 127.0.0.1
  port: 65535
```

### 3. Install World Files

Extract the world files zip directly into your `configuration/` folder. The zip already contains the correct folder structure.

### 4. Install Data Files

Download from [configuration/skyblock](https://github.com/Swofty-Developments/HypixelSkyBlock/tree/master/configuration/skyblock):

- `skills/` folder → `configuration/skyblock/skills/`
- `levels/` folder → `configuration/skyblock/levels/`
- `reforges/` folder → `configuration/skyblock/reforges/`
- `items/` folder → `configuration/skyblock/items/`
- `collections/` folder → `configuration/skyblock/collections/`
- `songs/` folder → `configuration/skyblock/songs/` (optional)

### 5. Setup PicoLimbo

PicoLimbo handles the limbo state:

1. Place `PicoLimbo.jar` in a separate directory
2. Edit the `server.toml` which you can find in the repository configuration folder:
   - Set `method="MODERN"`
   - Set `secret="YOUR_SECRET"` (or FORWARDING_SECRET environment variable by default)
3. Move the `limbo.polar` file to the same directory, or set polar_file to empty: `polar_file=""`
4. Keep it running in the background

### 6. Start a Game Server

```bash
java -jar HypixelCore.jar SKYBLOCK_ISLAND
```

Replace `SKYBLOCK_ISLAND` with any valid [ServerType](/docs/reference/server-types).

## Starting Multiple Servers

You can run multiple game servers simultaneously. Each ServerType can have multiple instances:

```bash
# Terminal 1 - Island server
java -jar HypixelCore.jar SKYBLOCK_ISLAND

# Terminal 2 - Hub server
java -jar HypixelCore.jar SKYBLOCK_HUB

# Terminal 3 - Another hub for load balancing
java -jar HypixelCore.jar SKYBLOCK_HUB
```

## Database Setup

After starting your first server, import the required data into MongoDB:

### Regions (Required)

1. Download [`Minestom.regions.csv`](https://github.com/Swofty-Developments/HypixelSkyBlock/tree/master/configuration/skyblock)
2. Import to the `regions` collection in MongoDB
3. Restart the server

### Fairy Souls (Optional)

1. Download [`Minestom.fairysouls.csv`](https://github.com/Swofty-Developments/HypixelSkyBlock/tree/master/configuration/skyblock)
2. Import to the `fairysouls` collection

### Hub Crystals (Optional)

1. Download [`Minestom.crystals.csv`](https://github.com/Swofty-Developments/HypixelSkyBlock/tree/master/configuration/skyblock)
2. Import to the `crystals` collection

Or use the `/addcrystal` command in-game.

## Admin Setup

To give yourself admin permissions:

1. Log in and out of the server
2. Open MongoDB Compass
3. Navigate to `Minestom` → `profiles`
4. Find your profile and set `rank: "ADMIN"`
5. Log back in

## Memory Allocation

For production, allocate appropriate memory:

```bash
java -Xms2G -Xmx2G -jar HypixelCore.jar SKYBLOCK_HUB
```

Recommended per server type:
- Hub: 2-4 GB
- Island: 1-2 GB
- Other locations: 1-2 GB
