# Resource Pack Setup

The current packer flow is server-hosted. It builds the pack in memory and serves it over HTTP.

## Current Pack Source Paths

The active testing pack definition uses:

- `configuration/resourcepacks/testingpack` (base pack files)
- `configuration/resourcepacks/testingpack_textures` (optional custom texture PNGs)

Example shader override path:

```text
configuration/resourcepacks/testingpack/assets/minecraft/shaders/core/rendertype_text.vsh
```

## Build the Packer

From the repository root:

```bash
./gradlew :packer:shadowJar
```

This produces:

```text
packer/build/libs/net.swofty.packer.HypixelPackServer.jar
```

## Run the Pack Server

From the repository root:

```bash
java -jar packer/build/libs/net.swofty.packer.HypixelPackServer.jar \
  --host 127.0.0.1 \
  --port 7270
```

Supported args:

- `--host` (or `-h`)
- `--port` (or `-p`)

On startup, the server logs:

- `Resource pack built. Hash: ...`
- `Pack URL: http://<host>:<port>/<hash>.zip`

## Configure the Game Server

For locally built packs, set the pack server URL in `configuration/config.yml`:

```yaml
resource-packs:
  testingpack:
    server-url: "http://127.0.0.1:7270"
```

SkyBlock can use Hypixel's official pack metadata API instead. The selected pack URL is sent directly to each player, using the pack format matching their client version:

```yaml
resource-packs:
  skyblockpack:
    use-hypixel-api: true
    hypixel-api-url: "https://api.hypixel.net/v2/resources/packs"
```

Notes:

- Local pack hashes are generated automatically at runtime by the pack builder; official-pack hashes come from the API.
- You do not manually set a `resource-pack-hash` field.
- `use-hypixel-api` uses the original client protocol reported by ViaVersion when it is enabled.
- The client does not download `skyblockpack` from the local pack server when `use-hypixel-api` is enabled.

## Verify In-Game

1. Join a Ravengard server (for example `RAVENGARD_LOBBY`).
2. Wait for the resource pack prompt/application.
3. If testing visually, use `/minimap` to toggle the minimap item.
4. Reload packs with `F3 + T` after pack changes.

If Minecraft keeps an old pack cached, remove the cached server-resource-pack directory and reconnect.
