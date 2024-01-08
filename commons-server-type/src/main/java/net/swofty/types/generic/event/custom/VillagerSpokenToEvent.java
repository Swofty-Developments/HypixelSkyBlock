package net.swofty.types.generic.event.custom;

import lombok.Getter;
import net.minestom.server.event.trait.CancellableEvent;
import net.minestom.server.event.trait.PlayerInstanceEvent;
import net.swofty.types.generic.entity.villager.SkyBlockVillagerNPC;
import net.swofty.types.generic.user.SkyBlockPlayer;
import org.jetbrains.annotations.NotNull;

public class VillagerSpokenToEvent implements PlayerInstanceEvent, CancellableEvent {
    private final SkyBlockPlayer player;
    @Getter
    private final SkyBlockVillagerNPC villager;
    private Boolean cancelled = false;

    public VillagerSpokenToEvent(SkyBlockPlayer player, SkyBlockVillagerNPC villager) {
        this.player = player;
        this.villager = villager;
    }

    @Override
    public @NotNull SkyBlockPlayer getPlayer() {
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
