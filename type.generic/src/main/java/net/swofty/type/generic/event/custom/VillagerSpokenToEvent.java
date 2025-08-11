package net.swofty.type.generic.event.custom;

import lombok.Getter;
import net.minestom.server.event.trait.CancellableEvent;
import net.minestom.server.event.trait.PlayerInstanceEvent;
import net.swofty.type.generic.entity.villager.HypixelVillagerNPC;
import net.swofty.type.generic.user.HypixelPlayer;
import org.jetbrains.annotations.NotNull;

public class VillagerSpokenToEvent implements PlayerInstanceEvent, CancellableEvent {
    private final HypixelPlayer player;
    @Getter
    private final HypixelVillagerNPC villager;
    private Boolean cancelled = false;

    public VillagerSpokenToEvent(HypixelPlayer player, HypixelVillagerNPC villager) {
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
