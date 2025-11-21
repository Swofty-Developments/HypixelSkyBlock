package net.swofty.anticheat.event.packet;

import lombok.Getter;
import net.swofty.anticheat.engine.SwoftyPlayer;

import java.util.UUID;

@Getter
public abstract class SwoftyPacket {
    private final SwoftyPlayer player;

    public SwoftyPacket(UUID uuid) {
        this.player = SwoftyPlayer.players.get(uuid);
    }

    public SwoftyPacket(SwoftyPlayer player) {
        this.player = player;
    }
}
