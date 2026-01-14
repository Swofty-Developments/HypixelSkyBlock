# Resource Pack Setup

The resource pack system provides custom textures and models for items.

## Download Required Files

1. Download `SkyBlockPacker.jar` from the [releases page](https://github.com/Swofty-Developments/HypixelSkyBlock/releases/tag/latest)
2. Download [`pack_textures`](https://github.com/Swofty-Developments/HypixelSkyBlock/tree/master/configuration/skyblock) folder
3. Download [`SkyBlockPack`](https://github.com/Swofty-Developments/HypixelSkyBlock/tree/master/configuration/skyblock) folder

## Directory Structure

```
packer/
├── SkyBlockPacker.jar
├── SkyBlockPack/
│   └── ... (pack source files)
├── pack_textures/
│   └── ... (texture files)
└── output/
    └── ... (generated pack)
```

## Building the Pack

Run the packer with the following command:

```bash
java -jar SkyBlockPacker.jar \
  -v /path/to/SkyBlockPack \
  -o /path/to/output \
  -t /path/to/pack_textures
```

**Arguments**:
- `-v` - Path to SkyBlockPack folder (pack source)
- `-o` - Output directory for the generated pack
- `-t` - Path to pack_textures folder

## Using the Pack

After generation:

1. The resource pack will be in your output directory
2. Apply it in Minecraft under Options → Resource Packs
3. For server-side distribution, host the pack and configure in `config.yml`

## Server-Side Distribution

To automatically send the pack to players, add to your `config.yml`:

```yaml
resource-pack-url: "https://your-host.com/pack.zip"
resource-pack-hash: "SHA1_HASH_HERE"
```

Generate the SHA1 hash:
```bash
sha1sum output/resourcepack.zip
```
