# Docker Setup

Docker provides an automated way to deploy the entire stack with minimal configuration.

## Prerequisites

- [Docker Desktop](https://www.docker.com/products/docker-desktop) installed
- Git installed

## Quick Start

### 1. Clone the Repository

```bash
git clone https://github.com/Swofty-Developments/HypixelSkyBlock.git
cd HypixelSkyBlock
```

### 2. Add World Files

Download the [world files](https://files.catbox.moe/of7snu.zip) and extract them directly into the `configuration/` folder. The zip already contains the correct folder structure.

### 3. Configure resources.json

In your `configuration` folder:

1. Remove the default `resources.json`
2. Rename `resources.json.docker` to `resources.json`

### 4. Build and Run

```bash
docker-compose up --build
```

For detached mode (background):

```bash
docker-compose up --build -d
```

:::alert note
After the first build, you can skip `--build` for faster startup:
```bash
docker-compose up
```
:::

### 5. Stop Containers

```bash
docker-compose down
```

Or use the stop button in Docker Desktop.

## What Gets Started

The Docker Compose setup starts:

| Container      | Purpose             |
|----------------|---------------------|
| MongoDB        | Database            |
| Redis          | Caching & messaging |
| Velocity Proxy | Player connections  |
| NanoLimbo      | Connection queue    |
| Game Servers   | Gameplay instances  |
| Services       | Microservices       |

## Logs and Debugging

View logs for all containers:
```bash
docker-compose logs -f
```

View logs for a specific container:
```bash
docker-compose logs -f proxy
docker-compose logs -f hypixelcore_island
```

## Data Persistence

Docker volumes persist data between restarts:

- MongoDB data
- Configuration files
- World saves

## Rebuilding

If you make changes to the codebase:

```bash
docker-compose down
docker-compose up --build
```
