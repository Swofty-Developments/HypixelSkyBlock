# TestFlow System Usage Guide

## Overview

The TestFlow system allows you to coordinate multi-server testing scenarios where multiple servers need to work together with a specific set of test players.

## Setup

1. **Create a TestFlow YAML file** in the `testflows/` directory
2. **Create a TestFlow handler** that extends `TestFlowHandler`
3. **Run the test flow** using the Gradle task

## YAML Configuration

Create a YAML file in `testflows/` directory (e.g., `testflows/mytestflow.yml`):

```yaml
start_servers:
  - ISLAND:2    # Start 2 ISLAND servers
  - HUB:3       # Start 3 HUB servers
  - GENERIC:1   # Start 1 GENERIC server

handler: MyTestFlowHandler.java
```

## Running a TestFlow

Use the Gradle task to start your test flow:

```bash
./gradlew runWithTestFlow -PtestFlow=mytestflow -Pplayers=player1,player2,player3
```

### Parameters:
- `testFlow`: Name of the YAML file (without .yml extension)
- `players`: Comma-separated list of player names that are part of this test

## Creating a TestFlow Handler

Create a Java class that extends `TestFlowHandler`:

```java
public class MyTestFlowHandler extends TestFlowHandler {
    
    @Override
    public void onTestFlowStart(TestFlow.TestFlowInstance instance) {
        // Called when test flow starts
        System.out.println("Test flow started with " + instance.getPlayers().size() + " players");
    }

    @Override
    public void onAllServersReady(TestFlow.TestFlowInstance instance) {
        // Called when all servers are online and ready
        System.out.println("All " + instance.getTotalServers() + " servers are ready!");
    }

    @Override
    public void onPlayerJoin(String playerName, TestFlow.TestFlowInstance instance) {
        if (instance.hasPlayer(playerName)) {
            // Handle test player joining
            System.out.println("Test player " + playerName + " joined");
        }
    }

    @Override
    public void onPlayerLeave(String playerName, TestFlow.TestFlowInstance instance) {
        if (instance.hasPlayer(playerName)) {
            // Handle test player leaving
            System.out.println("Test player " + playerName + " left");
        }
    }

    @Override
    public void onTestFlowEnd(TestFlow.TestFlowInstance instance) {
        // Called when test flow ends
        System.out.println("Test flow ended after " + instance.getUptime() + "ms");
    }
}
```

## Using TestFlow in Your Code

Check if a test flow is active:

```java
if (TestFlow.isTestFlowActive()) {
    TestFlow.TestFlowInstance instance = TestFlow.getTestFlowInstance();
    
    // Check if a specific player is part of the test flow
    if (instance.hasPlayer("somePlayer")) {
        // Handle test flow logic
    }
    
    // Get test flow information
    String testName = instance.getName();
    List<String> players = instance.getPlayers();
    boolean allReady = instance.areAllServersRegistered();
}
```

## How It Works

1. **Gradle Task**: Parses the YAML file and starts multiple JVM processes
2. **Server Registration**: Each server registers with the proxy, indicating it's part of a test flow
3. **Proxy Coordination**: The proxy tracks which servers belong to which test flow
4. **Handler Activation**: Once all servers are ready, the test flow handler is activated
5. **Player Tracking**: The system tracks which players are part of the test flow across all servers

## Example Test Flow Scenarios

### Load Testing
```yaml
start_servers:
  - ISLAND:5
  - HUB:3

handler: LoadTestHandler.java
``