# Docker Setup

## Quick Install (Linux / macOS)

The fastest way to get running. A single command downloads the native installer and launches an interactive TUI that handles everything:

```bash
curl -fsSL skyblock-installer.swofty.net | bash
```

This fetches the prebuilt `skyblock-installer` binary for your platform from the [latest release](https://github.com/Swofty-Developments/HypixelSkyBlock/releases/latest), caches it under `~/.cache/skyblock-installer`, and runs it. The installer will:

1. Verify its dependencies are present (`docker`, `git`, `curl`)
2. Run a system requirements check
3. Let you pick which server types and services to run
4. Generate all configuration and Docker Compose files
5. Build and start everything in the correct order
6. Drop you into a management dashboard

:::alert note
Requires **Docker** with **Compose v2** and a running Docker daemon your user can access. The installer itself is a self-contained Go binary — there are no other runtime dependencies to install.
:::

### What You'll See

The installer walks you through:

| Step              | What It Does                                                              |
|-------------------|---------------------------------------------------------------------------|
| System Check      | Validates RAM, CPU, disk space, Docker version                            |
| Configuration     | Pick install directory, bind IP, online mode                              |
| Server Selection  | Choose from 14 SkyBlock servers and 10 minigame servers                   |
| Service Selection | Pick which microservices to run (DataMutex and Party are required)        |
| Build & Launch    | Builds Docker images, starts containers in order, waits for health checks |

### Management Dashboard

To manage an existing install, run the installer again — it detects your installation and offers to open the dashboard:

```bash
curl -fsSL skyblock-installer.swofty.net | bash
```

By default it installs to `~/.hypixel-skyblock`; pass `-dir <path>` to use a different location (`curl -fsSL skyblock-installer.swofty.net | bash -s -- -dir /opt/skyblock`). From the home menu choose **Manage existing installation** to reach the dashboard, which provides:

- **Refresh status** - Live view of every container's health
- **Start all / Stop all** - Control all containers at once
- **Restart container** - Restart an individual container
- **Rebuild and update** - Pull the latest JARs and rebuild images
- **Configure servers/services** - Change which servers and services run
- **Make player STAFF** - Promote a player to staff rank via the database
- **View logs** - Tail logs from any container
- **Fresh reinstall / Uninstall** - Recreate or remove the deployment

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

In the top of `docker-compose.yml` change the `change-me` to other.

```yml
x-forwarding-env: &forwarding_env
  FORWARDING_SECRET: ${FORWARDING_SECRET:-change-me}
```

For reference:

```yml
x-forwarding-env: &forwarding_env
  FORWARDING_SECRET: ${FORWARDING_SECRET:-i7sC4xqh}
```

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

| Container      | Purpose                                     |
|----------------|---------------------------------------------|
| MongoDB        | Database                                    |
| Redis          | Caching & messaging                         |
| Velocity Proxy | Player connections                          |
| PicoLimbo      | Connection queue                            |
| Game Servers   | Gameplay instances                          |
| Services       | Microservices (API, Auctions, Bazaar, etc.) |

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
