package net.swofty.types.generic.event.custom;

import lombok.Getter;
import net.minestom.server.event.trait.PlayerInstanceEvent;
import net.swofty.types.generic.entity.mob.SkyBlockMob;
import net.swofty.types.generic.user.SkyBlockPlayer;
import org.jetbrains.annotations.NotNull;

public class PlayerKilledSkyBlockMobEvent implements PlayerInstanceEvent {
    private final SkyBlockPlayer player;
    @Getter
    private final SkyBlockMob killedMob;

    public PlayerKilledSkyBlockMobEvent(SkyBlockPlayer player, SkyBlockMob killedMob) {
        this.player = player;
        this.killedMob = killedMob;
    }

    @Override
    public @NotNull SkyBlockPlayer getPlayer() {
        return player;
    }
}
