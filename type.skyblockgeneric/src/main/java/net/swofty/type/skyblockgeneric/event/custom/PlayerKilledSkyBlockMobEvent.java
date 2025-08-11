package net.swofty.type.skyblockgeneric.event.custom;

import lombok.Getter;
import net.minestom.server.event.trait.PlayerInstanceEvent;
import net.swofty.type.skyblockgeneric.data.datapoints.DatapointBestiary;
import net.swofty.type.skyblockgeneric.entity.mob.BestiaryMob;
import net.swofty.type.skyblockgeneric.entity.mob.SkyBlockMob;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;
import org.jetbrains.annotations.NotNull;

public class PlayerKilledSkyBlockMobEvent implements PlayerInstanceEvent {
    private final SkyBlockPlayer player;
    @Getter
    private final SkyBlockMob killedMob;

    public PlayerKilledSkyBlockMobEvent(SkyBlockPlayer player, SkyBlockMob killedMob) {
        this.player = player;
        this.killedMob = killedMob;

        if (killedMob instanceof BestiaryMob bestiaryMob) {
            DatapointBestiary.PlayerBestiary playerBestiary = player.getDataHandler().get(DataHandler.Data.BESTIARY, DatapointBestiary.class).getValue();
            playerBestiary.increase(bestiaryMob, 1);
        }
    }

    @Override
    public @NotNull SkyBlockPlayer getPlayer() {
        return player;
    }
}
