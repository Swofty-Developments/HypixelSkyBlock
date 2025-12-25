package net.swofty.type.bedwarsgame.game;

import lombok.Getter;
import net.swofty.commons.bedwars.map.BedWarsMapsConfig.TeamKey;

import java.util.UUID;

/**
 * Holds information about a player who disconnected from an active game.
 * Used to restore state when they rejoin.
 */
@Getter
public class DisconnectedPlayerInfo {
    private final UUID playerUuid;
    private final String username;
    private final TeamKey teamKey;
    private final long disconnectTime;
    private final boolean bedWasAliveOnDisconnect;

    // Saved player state for restoration
    private final int armorLevel;
    private final int pickaxeLevel;
    private final int axeLevel;

    public DisconnectedPlayerInfo(UUID playerUuid, String username, TeamKey teamKey,
                                  boolean bedWasAlive,
                                  int armorLevel, int pickaxeLevel, int axeLevel) {
        this.playerUuid = playerUuid;
        this.username = username;
        this.teamKey = teamKey;
        this.disconnectTime = System.currentTimeMillis();
        this.bedWasAliveOnDisconnect = bedWasAlive;
        this.armorLevel = armorLevel;
        this.pickaxeLevel = pickaxeLevel;
        this.axeLevel = axeLevel;
    }

    /**
     * Determines if player should rejoin as spectator.
     * True if bed was already broken when they disconnected.
     */
    public boolean shouldRejoinAsSpectator() {
        return !bedWasAliveOnDisconnect;
    }
}
