package net.swofty.type.game.game;

import net.minestom.server.instance.InstanceContainer;
import net.swofty.type.game.game.event.GameDisposeEvent;
import net.swofty.type.game.game.event.GameStartEvent;
import net.swofty.type.game.game.event.GameStateChangeEvent;
import net.swofty.type.game.game.event.PlayerDisconnectGameEvent;
import net.swofty.type.game.game.event.PlayerJoinGameEvent;
import net.swofty.type.game.game.event.PlayerJoinedGameEvent;
import net.swofty.type.game.game.event.PlayerLeaveGameEvent;
import net.swofty.type.game.game.event.PlayerRejoinGameEvent;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

public abstract class AbstractGame<P extends GameParticipant> implements Game<P> {
    protected final String gameId;
    protected final InstanceContainer instance;
    protected final Map<UUID, P> players = new ConcurrentHashMap<>();
    protected final Map<UUID, DisconnectedPlayerData> disconnectedPlayers = new ConcurrentHashMap<>();
    protected final Consumer<Object> eventDispatcher;

    protected GameState state = GameState.WAITING;
    protected DefaultGameCountdown countdown;

    /**
     * Stores minimal data about disconnected players for rejoin.
     */
    public record DisconnectedPlayerData(
        UUID uuid,
        String username,
        long disconnectTime,
        Map<String, Object> savedData
    ) {
    }

    protected AbstractGame(InstanceContainer instance, Consumer<Object> eventDispatcher) {
        this.gameId = UUID.randomUUID().toString();
        this.instance = instance;
        this.eventDispatcher = eventDispatcher;
        this.countdown = createCountdown();
    }

    /**
     * Creates the countdown controller for this game.
     * Override to customize countdown behavior.
     */
    protected DefaultGameCountdown createCountdown() {
        return new DefaultGameCountdown(
            gameId,
            getCountdownConfig(),
            eventDispatcher,
            this::onCountdownComplete,
            this::hasMinimumPlayers
        );
    }

    /**
     * @return The countdown configuration for this game type
     */
    protected CountdownConfig getCountdownConfig() {
        return CountdownConfig.DEFAULT;
    }

    /**
     * Called when countdown completes. Triggers game start.
     */
    protected void onCountdownComplete() {
        start();
    }

    @Override
    public String getGameId() {
        return gameId;
    }

    @Override
    public GameState getState() {
        return state;
    }

    @Override
    public InstanceContainer getInstance() {
        return instance;
    }

    @Override
    public Collection<P> getPlayers() {
        return Collections.unmodifiableCollection(players.values());
    }

    @Override
    public Optional<P> getPlayer(UUID uuid) {
        return Optional.ofNullable(players.get(uuid));
    }

    @Override
    public GameCountdownController getCountdown() {
        return countdown;
    }

    @Override
    public JoinResult join(P player) {
        PlayerJoinGameEvent event = new PlayerJoinGameEvent(gameId, player.getServerPlayer());
        eventDispatcher.accept(event);

        if (event.isCancelled()) {
            return new JoinResult.Denied(event.getCancelReason() != null ? event.getCancelReason() : "Join cancelled");
        }

        // Check basic conditions
        if (state != GameState.WAITING && state != GameState.STARTING) {
            return new JoinResult.Denied("Game already in progress");
        }

        if (getAvailableSlots() <= 0) {
            return new JoinResult.Denied("Game is full");
        }

        // Add player
        players.put(player.getUuid(), player);
        player.setGameId(gameId);

        // Fire joined event
        eventDispatcher.accept(new PlayerJoinedGameEvent(
            gameId,
            player.getUuid(),
            player.getServerPlayer().getUsername(),
            players.size(),
            getMaxPlayers()
        ));

        // Check if we should start countdown
        if (hasMinimumPlayers() && !countdown.isActive()) {
            setState(GameState.STARTING);
            countdown.start();
        }

        return new JoinResult.Success();
    }

    @Override
    public void leave(P player) {
        if (!players.containsKey(player.getUuid())) return;

        players.remove(player.getUuid());
        player.setGameId(null);

        if (this instanceof AbstractTeamGame<P, ?> teamGame) {
            teamGame.removeFromTeam(player);
        }

        eventDispatcher.accept(new PlayerLeaveGameEvent(
            gameId,
            player.getServerPlayer(),
            PlayerLeaveGameEvent.LeaveReason.VOLUNTARY
        ));

        // Check countdown conditions
        if (countdown.isActive()) {
            countdown.checkConditions();
            if (!countdown.isActive() && state == GameState.STARTING) {
                setState(GameState.WAITING);
            }
        }

        // Check win conditions if in progress
        if (state == GameState.IN_PROGRESS) {
            checkWinConditions();
        }
    }

    /**
     * Handles player disconnection during an active game.
     */
    public void handleDisconnect(P player) {
        if (state != GameState.IN_PROGRESS) {
            leave(player);
            return;
        }

        UUID uuid = player.getUuid();
        boolean canRejoin = canPlayerRejoin(player);

        // Store disconnect data
        disconnectedPlayers.put(uuid, new DisconnectedPlayerData(
            uuid,
            player.getServerPlayer().getUsername(),
            System.currentTimeMillis(),
            savePlayerData(player)
        ));

        players.remove(uuid);

        eventDispatcher.accept(new PlayerDisconnectGameEvent(
            gameId,
            player.getServerPlayer(),
            canRejoin
        ));

        checkWinConditions();
    }

    public boolean handleRejoin(P player) {
        DisconnectedPlayerData data = disconnectedPlayers.remove(player.getUuid());
        if (data == null || state != GameState.IN_PROGRESS) {
            return false;
        }

        players.put(player.getUuid(), player);
        player.setGameId(gameId);

        eventDispatcher.accept(new PlayerRejoinGameEvent(
            gameId,
            player.getServerPlayer(),
            data
        ));

        return true;
    }

    /**
     * Checks if a disconnected player can rejoin.
     */
    public boolean hasDisconnectedPlayer(UUID uuid) {
        return disconnectedPlayers.containsKey(uuid);
    }

    @Override
    public void start() {
        if (state == GameState.IN_PROGRESS) return;

        setState(GameState.IN_PROGRESS);

        eventDispatcher.accept(new GameStartEvent(gameId));
    }

    @Override
    public void end() {
        if (state == GameState.ENDING || state == GameState.TERMINATED) return;

        setState(GameState.ENDING);
    }

    @Override
    public void dispose() {
        setState(GameState.TERMINATED);
        countdown.stop();
        players.clear();
        disconnectedPlayers.clear();
        eventDispatcher.accept(new GameDisposeEvent(
            gameId
        ));
    }

    /**
     * Sets the game state and fires a state change event.
     */
    protected void setState(GameState newState) {
        if (this.state == newState) return;

        GameState oldState = this.state;
        this.state = newState;

        eventDispatcher.accept(new GameStateChangeEvent(gameId, oldState, newState));
    }

    /**
     * Check if win conditions are met and end the game if so.
     */
    protected abstract void checkWinConditions();

    /**
     * Determines if a player can rejoin after disconnecting.
     * Override for game-specific logic (e.g., bed status in BedWars).
     */
    protected boolean canPlayerRejoin(P player) {
        return true;
    }

    /**
     * Saves player data when they disconnect for potential rejoin.
     * Override to save game-specific data.
     */
    protected Map<String, Object> savePlayerData(P player) {
        return new HashMap<>();
    }

}
