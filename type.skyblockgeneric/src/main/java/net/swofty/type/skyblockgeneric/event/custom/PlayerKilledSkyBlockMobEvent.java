package net.swofty.type.skyblockgeneric.event.custom;

import lombok.Getter;
import net.minestom.server.event.trait.PlayerInstanceEvent;
import net.swofty.type.generic.data.datapoints.DatapointBestiary;
import net.swofty.type.generic.entity.mob.BestiaryMob;
import net.swofty.type.generic.entity.mob.SkyBlockMob;
import net.swofty.type.generic.user.HypixelPlayer;
import org.jetbrains.annotations.NotNull;

public class PlayerKilledSkyBlockMobEvent implements PlayerInstanceEvent {
    private final HypixelPlayer player;
    @Getter
    private final SkyBlockMob killedMob;

    public PlayerKilledSkyBlockMobEvent(HypixelPlayer player, SkyBlockMob killedMob) {
        this.player = player;
        this.killedMob = killedMob;

        if (killedMob instanceof BestiaryMob bestiaryMob) {
            DatapointBestiary.PlayerBestiary playerBestiary = player.getSkyBlockData().get(net.swofty.type.skyblockgeneric.data.SkyBlockDataHandler.Data.BESTIARY, DatapointBestiary.class).getValue();
            playerBestiary.increase(bestiaryMob, 1);
        }
    }

    @Override
    public @NotNull HypixelPlayer getPlayer() {
        return player;
    }
}
