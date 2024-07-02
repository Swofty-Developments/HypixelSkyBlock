package net.swofty.anticheat.event.packet;

import net.swofty.anticheat.engine.SwoftyPlayer;

public abstract class SwoftyPacket {
    private SwoftyPlayer player;

    public SwoftyPacket(SwoftyPlayer player) {
        this.player = player;
    }

    public SwoftyPlayer getPlayer() {
        return player;
    }
}
