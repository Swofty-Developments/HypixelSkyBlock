package net.swofty.anticheat.loader.managers;

import net.minestom.server.coordinate.Vec;
import net.swofty.anticheat.math.Pos;

import java.util.UUID;

public abstract class SwoftyPlayerManager {
    private UUID uuid;

    public SwoftyPlayerManager(UUID uuid) {
        this.uuid = uuid;
    }

    public abstract void setPositionForPlayer(Pos pos);
    public abstract void setVelocityForPlayer(Vec vel);
    public abstract void sendMessage(String message);
}
