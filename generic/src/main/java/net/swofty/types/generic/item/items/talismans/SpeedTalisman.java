package net.swofty.types.generic.item.items.talismans;

import net.minestom.server.entity.EntityType;
import net.swofty.types.generic.event.value.SkyBlockValueEvent;
import net.swofty.types.generic.event.value.ValueUpdateEvent;
import net.swofty.types.generic.event.value.events.PlayerDamagedByMobValueUpdateEvent;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.Talisman;
import net.swofty.types.generic.user.SkyBlockPlayer;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class SpeedTalisman extends SkyBlockValueEvent implements Talisman {
    @Override
    public String getSkullTexture(@Nullable SkyBlockPlayer player, SkyBlockItem item) {
        return "8624bacb5f1986e6477abce4ae7dca1820a5260b6233b55ba1d9ba936c84b";
    }
    @Override
    public ItemStatistics getStatistics() {
        return ItemStatistics.builder()
                .with(ItemStatistic.SPEED, 1D)
                .build();
    }
    
}