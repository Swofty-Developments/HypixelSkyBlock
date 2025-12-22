package net.swofty.type.generic.event.custom;

import lombok.Getter;
import net.minestom.server.event.trait.CancellableEvent;
import net.minestom.server.event.trait.PlayerInstanceEvent;

import net.swofty.type.generic.entity.npc.HypixelNPC;
import net.swofty.type.generic.user.HypixelPlayer;
import org.jetbrains.annotations.NotNull;

public class VillagerSpokenToEvent implements PlayerInstanceEvent, CancellableEvent {
    private final HypixelPlayer player;
    @Getter
    private final HypixelNPC villager;
    private Boolean cancelled = false;

    public VillagerSpokenToEvent(HypixelPlayer player, HypixelNPC villager) {
        this.player = player;
        this.villager = villager;
    }

    @Override
    public @NotNull HypixelPlayer getPlayer() {
        return player;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean b) {
        cancelled = b;
    }
}
