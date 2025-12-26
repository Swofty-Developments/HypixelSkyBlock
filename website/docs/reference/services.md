# Services Reference

Services are microservices that handle specific features independently of game servers.

## Architecture Overview

```
┌─────────────────┐     ┌─────────────────┐
│   Game Server   │────▶│      Redis      │◀────│   Game Server   │
└─────────────────┘     └────────┬────────┘     └─────────────────┘
                                 │
         ┌───────────────────────┼───────────────────────┐
         │                       │                       │
         ▼                       ▼                       ▼
┌─────────────────┐     ┌─────────────────┐     ┌─────────────────┐
│  Auction House  │     │     Bazaar      │     │      Party      │
└─────────────────┘     └─────────────────┘     └─────────────────┘
         │                       │                       │
         ▼                       ▼                       ▼
┌─────────────────────────────────────────────────────────────────┐
│                           MongoDB                                │
└─────────────────────────────────────────────────────────────────┘
```

## Service List

### ServiceAPI

**JAR**: `ServiceAPI.jar`
**Port**: 8080 (configurable)
**Type**: `AUCTION_HOUSE`

REST API service for external integrations.

```bash
java -jar ServiceAPI.jar
java -jar ServiceAPI.jar --port=8081  # Custom port
```

**Features**:
- HTTP endpoints using Spark Framework
- Session-based authentication
- Admin panel at `/panel/authenticated`
- User and profile database access

**Dependencies**: MongoDB, Spark Framework

---

### ServiceAuctionHouse

**JAR**: `ServiceAuctionHouse.jar`
**Type**: `AUCTION_HOUSE`

Manages auction house listings and transactions.

```bash
java -jar ServiceAuctionHouse.jar
```

**Features**:
- Create, bid, and complete auctions
- Active/inactive auction tracking
- Auction caching for performance

**MongoDB Collections**:
- `auction_active` - Current listings
- `auction_inactive` - Completed/expired auctions

---

### ServiceBazaar

**JAR**: `ServiceBazaar.jar`
**Type**: `BAZAAR`

Handles the bazaar marketplace system.

```bash
java -jar ServiceBazaar.jar
```

**Features**:
- Buy and sell order management
- Market state tracking
- Order matching and execution

**MongoDB Collections**:
- `orders` - Buy/sell orders

---

### ServiceParty

**JAR**: `ServiceParty.jar`
**Type**: `PARTY`

Manages player parties across servers.

```bash
java -jar ServiceParty.jar
```

**Features**:
- Party creation and disbanding
- Invitation system
- Cross-server party synchronization
- Party chat routing

**Endpoints**:
- `GetPartyEndpoint` - Retrieve party info
- `IsPlayerInPartyEndpoint` - Check membership
- `PartyEventToServiceEndpoint` - Handle party events

---

### ServiceItemTracker

**JAR**: `ServiceItemTracker.jar`
**Type**: `ITEM_TRACKER`

Tracks valuable items across the server network.

```bash
java -jar ServiceItemTracker.jar
```

**Features**:
- Item location tracking
- Item history logging
- Cross-server item queries

**MongoDB Collections**:
- `tracked_items` - Item tracking data

---

### ServiceDataMutex

**JAR**: `ServiceDataMutex.jar`
**Type**: `DATA_MUTEX`

Provides distributed locking for data synchronization.

```bash
java -jar ServiceDataMutex.jar
```

**Features**:
- Prevents data corruption during server transfers
- Distributed lock management
- Atomic data operations

**Endpoints**:
- `SynchronizeDataEndpoint` - Acquire locks
- `UnlockDataEndpoint` - Release locks
- `UpdateSynchronizedDataEndpoint` - Update locked data

:::alert warning
This service is essential for data integrity. Always run it in production.
:::

---

### ServiceDarkAuction

**JAR**: `ServiceDarkAuction.jar`
**Type**: `DARK_AUCTION`

Manages periodic dark auction events.

```bash
java -jar ServiceDarkAuction.jar
```

**Features**:
- Scheduled events based on SkyBlock time
- Auction state management
- Prize pool handling

**Components**:
- `DarkAuctionScheduler` - Event timing
- `DarkAuctionState` - State management

---

### ServiceOrchestrator

**JAR**: `ServiceOrchestrator.jar`
**Type**: `ORCHESTRATOR`

Orchestrates game server management, primarily for BedWars.

```bash
java -jar ServiceOrchestrator.jar
```

**Features**:
- Server health monitoring via heartbeats
- Game map management
- Player game assignment
- Rejoin handling

**Endpoints**:
- `GameHeartbeatEndpoint` - Server status
- `GetMapsEndpoint` - Available maps
- `GetServerForMapEndpoint` - Server assignment
- `RejoinGameEndpoint` - Rejoin requests
- `GameChooseEndpoint` - Game selection

## Communication Protocol

Services communicate via Redis pub/sub with a specific protocol:

### Request Format
```
{request_id};{serialized_message}
```

### Response Format
```
{request_id}}=-=---={serialized_response}
```

### Protocol Objects

Located in `net.swofty.commons.protocol.objects`, these handle serialization:

```java
// Example endpoint registration
ServiceInitializer.register(
    ServiceType.AUCTION_HOUSE,
    GetAuctionEndpoint.class,
    GetAuctionRequest.class
);
```

## Memory Requirements

| Service       | Minimum RAM | Recommended RAM |
|---------------|-------------|-----------------|
| API           | 256 MB      | 512 MB          |
| Auction House | 256 MB      | 512 MB          |
| Bazaar        | 256 MB      | 512 MB          |
| Party         | 128 MB      | 256 MB          |
| Item Tracker  | 128 MB      | 256 MB          |
| Data Mutex    | 128 MB      | 256 MB          |
| Dark Auction  | 128 MB      | 256 MB          |
| Orchestrator  | 128 MB      | 256 MB          |

## Docker Reference

```yaml
service_api:
  image: service_prepared
  environment:
    SERVICE_CMD: java -jar ServiceAPI.jar

service_auction:
  image: service_prepared
  environment:
    SERVICE_CMD: java -jar ServiceAuctionHouse.jar

service_bazaar:
  image: service_prepared
  environment:
    SERVICE_CMD: java -jar ServiceBazaar.jar
```

## Health Checks

Services don't expose HTTP health endpoints by default. Monitor them via:

1. **Redis connectivity** - Services publish heartbeats
2. **Log output** - Check for error messages
3. **MongoDB connectivity** - Verify database operations
