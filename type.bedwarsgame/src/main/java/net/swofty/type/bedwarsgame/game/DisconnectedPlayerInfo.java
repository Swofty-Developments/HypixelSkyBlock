package net.swofty.type.bedwarsgame.game;

import lombok.Getter;
import net.swofty.commons.bedwars.map.BedWarsMapsConfig.TeamKey;

import java.util.UUID;

@Getter
public class DisconnectedPlayerInfo {
    private final UUID playerUuid;
    private final String username;
    private final TeamKey teamKey;
    private final long disconnectTime;
    private final boolean bedWasAliveOnDisconnect;

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

}
