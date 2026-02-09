# Docker Setup

## Quick Install (Linux)

The fastest way to get running. A single command launches an interactive installer that handles everything:

```bash
curl -fsSL skyblock-installer.swofty.net | bash
```

The installer will:
1. Check and install dependencies (Docker, `gum`, `figlet`)
2. Run a system requirements check
3. Let you pick which server types and services to run
4. Generate all configuration and Docker Compose files
5. Build and start everything in the correct order
6. Drop you into a management dashboard

:::alert note
Requires **Linux** with **Docker** installed. The installer will guide you through Docker setup if it's missing.
:::

### What You'll See

The installer walks you through:

| Step | What It Does |
|------|-------------|
| System Check | Validates RAM, CPU, disk space, Docker version |
| Configuration | Pick install directory, bind IP, online mode |
| Server Selection | Choose from 14 SkyBlock servers and 10 minigame servers |
| Service Selection | Pick which microservices to run (DataMutex and Party are required) |
| Build & Launch | Builds Docker images, starts containers in order, waits for health checks |

### Management Dashboard

After installation, manage your server anytime:

```bash
~/.hypixel-skyblock/install.sh --manage
```

The dashboard provides:

- **Start/Stop All** - Control all containers at once
- **Restart Container** - Restart individual containers
- **View Logs** - Tail logs from any container
- **Make Admin** - Promote a player to staff rank via the database
- **Check for Updates** - Pull latest JARs and rebuild
- **Watch Mode** - Live health monitoring with auto-refresh

You can also run the health monitor directly:

```bash
~/.hypixel-skyblock/install.sh --watch
```

## Manual Setup

If you prefer to set things up manually or aren't on Linux, you can use Docker Compose directly.

### Prerequisites

- [Docker](https://docs.docker.com/engine/install/) with Docker Compose v2
- Git

### 1. Clone the Repository

```bash
git clone https://github.com/Swofty-Developments/HypixelSkyBlock.git
cd HypixelSkyBlock
```

### 2. Configure

In your `configuration` folder:

1. Remove the default `config.yml`
2. Rename `config.docker.yml` to `config.yml`

### 3. Build and Run

```bash
docker compose up --build
```

For detached mode:

```bash
docker compose up --build -d
```

### 4. Stop Containers

```bash
docker compose down
```

## What Gets Started

The Docker Compose setup starts:

| Container | Purpose |
|-----------|---------|
| MongoDB | Database |
| Redis | Caching & messaging |
| Velocity Proxy | Player connections |
| NanoLimbo | Connection queue |
| Game Servers | Gameplay instances |
| Services | Microservices (API, Auctions, Bazaar, etc.) |

## Connecting

Once everything is running, connect with your Minecraft client to:

```
localhost:25565
```

## Logs and Debugging

View logs for all containers:
```bash
docker compose logs -f
```

View logs for a specific container:
```bash
docker compose logs -f hypixel_proxy
docker compose logs -f hypixelcore_skyblock_hub
```

## Data Persistence

Docker volumes persist data between restarts:

- MongoDB data (player profiles, auctions, etc.)
- Configuration files
- World saves
