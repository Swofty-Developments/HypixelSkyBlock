# Requirements

This project requires substantial resources to run properly. Make sure your system meets these requirements before proceeding.

## System Requirements

| Resource | Minimum | Recommended |
|----------|---------|-------------|
| RAM | 16 GB | 24+ GB |
| CPU Cores | 6 | 8+ |
| Storage | 15 GB | 25+ GB |
| Java | 25 | 25 |

### RAM Distribution

- **MongoDB**: ~4 GB
- **Redis**: ~512 MB
- **Velocity Proxy**: ~512 MB
- **Game Servers**: ~1-2 GB each
- **Services**: ~256-512 MB each

## Software Requirements

### Required

| Software | Purpose | Download |
|----------|---------|----------|
| Java 25 | Runtime | [Eclipse Adoptium](https://adoptium.net/) |
| MongoDB | Database | [MongoDB Community](https://www.mongodb.com/try/download/community) |
| Redis | Caching & Messaging | [Redis](https://redis.io/download/) or [Memurai](https://www.memurai.com/) (Windows) |

### Optional

| Software | Purpose | Download |
|----------|---------|----------|
| Docker | Containerized deployment | [Docker Desktop](https://www.docker.com/products/docker-desktop) |
| MongoDB Compass | Database GUI | [MongoDB Compass](https://www.mongodb.com/products/compass) |

If you use the Docker deployment, Java, MongoDB and Redis all run in containers, so Docker with Compose v2 is the only thing you need on the host. The [installer](/docs/docker/setup) checks for it and offers to install it for you if it is missing.

## Network Requirements

The following ports are used by default:

| Port  | Service                             |
|-------|-------------------------------------|
| 25565 | Velocity Proxy (player connections) |
| 27017 | MongoDB                             |
| 6379  | Redis                               |
| 7270  | Resource Pack Server                |
| 8080  | API Service                         |

:::alert warning
Ensure these ports are available and not blocked by your firewall when running locally.
:::
