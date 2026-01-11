# Services Setup

Services are independent microservices that handle specific features. They communicate with game servers via Redis.

## Overview

| Service       | JAR Name                  | Purpose                      |
|---------------|---------------------------|------------------------------|
| API           | `ServiceAPI.jar`          | REST API for external access |
| Auction House | `ServiceAuctionHouse.jar` | Manages auction listings     |
| Bazaar        | `ServiceBazaar.jar`       | Market/trading operations    |
| Party         | `ServiceParty.jar`        | Player party management      |
| Item Tracker  | `ServiceItemTracker.jar`  | Tracks items across servers  |
| Data Mutex    | `ServiceDataMutex.jar`    | Distributed data locking     |
| Dark Auction  | `ServiceDarkAuction.jar`  | Dark auction events          |
| Orchestrator  | `ServiceOrchestrator.jar` | Game server orchestration    |

## Download

Download all service JARs from the [releases page](https://github.com/Swofty-Developments/HypixelSkyBlock/releases/tag/latest).

## Directory Structure

Services share configuration with game servers:

```
services/
├── ServiceAPI.jar
├── ServiceAuctionHouse.jar
├── ServiceBazaar.jar
├── ServiceParty.jar
├── ServiceItemTracker.jar
├── ServiceDataMutex.jar
├── ServiceDarkAuction.jar
├── ServiceOrchestrator.jar
└── configuration/
    └── config.yml    # Same as game servers
```

## Starting Services

Each service runs as a separate process:

```bash
java -jar ServiceAPI.jar
java -jar ServiceAuctionHouse.jar
java -jar ServiceBazaar.jar
java -jar ServiceParty.jar
java -jar ServiceItemTracker.jar
java -jar ServiceDataMutex.jar
java -jar ServiceDarkAuction.jar
java -jar ServiceOrchestrator.jar
```

:::alert note
Services should be started after MongoDB and Redis are running, but before or alongside game servers.
:::

## Service Details

### API Service

Provides REST endpoints for external applications.

```bash
java -jar ServiceAPI.jar
# Or with custom port:
java -jar ServiceAPI.jar --port=8081
```

**Default Port**: 8080

**Key Endpoints**:
- Authentication via session cookies
- Admin panel at `/panel/authenticated`
- User and profile data access

### Auction House Service

Manages all auction functionality.

```bash
java -jar ServiceAuctionHouse.jar
```

**MongoDB Collections**:
- `auction_active` - Current listings
- `auction_inactive` - Completed/expired auctions

**Features**:
- Active/inactive auction tracking
- Auction caching for performance

### Bazaar Service

Handles the bazaar marketplace.

```bash
java -jar ServiceBazaar.jar
```

**MongoDB Collections**:
- `orders` - Buy/sell orders

**Features**:
- Order management
- Market state tracking

### Party Service

Manages player parties and groups.

```bash
java -jar ServiceParty.jar
```

**Features**:
- Party creation and management
- Invitation handling
- Cross-server party sync

### Item Tracker Service

Tracks valuable items across servers.

```bash
java -jar ServiceItemTracker.jar
```

**MongoDB Collections**:
- `tracked_items` - Item tracking data

### Data Mutex Service

Provides distributed locking for data synchronization.

```bash
java -jar ServiceDataMutex.jar
```

**Features**:
- Prevents data corruption during server transfers
- Ensures atomic operations across services

### Dark Auction Service

Manages periodic dark auction events.

```bash
java -jar ServiceDarkAuction.jar
```

**Features**:
- Scheduled based on SkyBlock time
- Auction state management

### Orchestrator Service

Coordinates game servers, primarily for BedWars.

```bash
java -jar ServiceOrchestrator.jar
```

**Features**:
- Server heartbeat monitoring
- Game map assignment
- Player rejoining

## Required vs Optional Services

### Required for Core Gameplay
- **Data Mutex** - Essential for player data integrity
- **Party** - Needed for party features

### Required for Economy
- **Auction House** - For auction functionality
- **Bazaar** - For bazaar functionality

### Optional
- **API** - Only needed for external integrations
- **Item Tracker** - For item tracking features
- **Dark Auction** - For dark auction events
- **Orchestrator** - Mainly for BedWars

## Memory Allocation

Services are lightweight and can share a single machine:

```bash
java -Xms256M -Xmx512M -jar ServiceAPI.jar
```

Typical usage: 256-512 MB per service.
