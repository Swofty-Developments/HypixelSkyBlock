# Adding Servers with Docker

You can add additional game servers by modifying `docker-compose.yml`.

## Server Template

Add this template to your `docker-compose.yml`:

```yaml
<server_name>:
  image: game_server_prepared
  container_name: <server_name>
  restart: "unless-stopped"
  environment:
    SERVICE_CMD: java -jar HypixelCore.jar <ServerType>
  depends_on:
    proxy:
      condition: service_healthy
    game_server_builder:
      condition: service_started
  volumes:
    - ./configuration:/app/configuration_files
  networks:
    - hypixel_network
```

## Configuration

Replace the placeholders:

| Placeholder     | Description           | Example            |
|-----------------|-----------------------|--------------------|
| `<server_name>` | Unique container name | `hypixelcore_hub2` |
| `<ServerType>`  | Valid ServerType enum | `SKYBLOCK_HUB`     |

## Example: Adding a Second Hub

```yaml
hypixelcore_hub2:
  image: game_server_prepared
  container_name: hypixelcore_hub2
  restart: "unless-stopped"
  environment:
    SERVICE_CMD: java -jar HypixelCore.jar SKYBLOCK_HUB
  depends_on:
    proxy:
      condition: service_healthy
    game_server_builder:
      condition: service_started
  volumes:
    - ./configuration:/app/configuration_files
  networks:
    - hypixel_network
```

## Example: Adding Spider's Den

```yaml
hypixelcore_spiders_den:
  image: game_server_prepared
  container_name: hypixelcore_spiders_den
  restart: "unless-stopped"
  environment:
    SERVICE_CMD: java -jar HypixelCore.jar SKYBLOCK_SPIDERS_DEN
  depends_on:
    proxy:
      condition: service_healthy
    game_server_builder:
      condition: service_started
  volumes:
    - ./configuration:/app/configuration_files
  networks:
    - hypixel_network
```

## Available Server Types

See the [Server Types Reference](/docs/reference/server-types) for all available types.

## Applying Changes

After modifying `docker-compose.yml`:

```bash
docker-compose up -d
```

This will start the new containers without affecting running ones.

## Load Balancing

The Velocity proxy automatically load balances between multiple servers of the same type. Simply add more containers with the same ServerType to distribute players.
