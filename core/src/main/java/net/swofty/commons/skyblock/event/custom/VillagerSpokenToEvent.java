package net.swofty.commons.skyblock.event.custom;

import lombok.Getter;
import net.minestom.server.event.trait.CancellableEvent;
import net.minestom.server.event.trait.PlayerInstanceEvent;
import net.swofty.commons.skyblock.user.SkyBlockPlayer;
import net.swofty.commons.skyblock.entity.villager.SkyBlockVillagerNPC;
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
