package net.swofty.types.generic.item.items.accessories.spider;

import net.minestom.server.entity.EntityType;
import net.swofty.types.generic.event.value.SkyBlockValueEvent;
import net.swofty.types.generic.event.value.ValueUpdateEvent;
import net.swofty.types.generic.event.value.events.PlayerDamagedByMobValueUpdateEvent;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.Talisman;
import net.swofty.types.generic.user.SkyBlockPlayer;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class SpiderTalisman extends SkyBlockValueEvent implements Talisman {
    @Override
    public List<String> getTalismanDisplay() {
        return List.of("§7Reduces the damage taken from",
                "§7§7Cave Spiders§7, §7Spiders§7,",
                "§7and §7Silverfish by §a5%§7.");
    }

    @Override
    public String getSkullTexture(@Nullable SkyBlockPlayer player, SkyBlockItem item) {
        return "9d7e3b19ac4f3dee9c5677c135333b9d35a7f568b63d1ef4ada4b068b5a25";
    }

    @Override
    public Class<? extends ValueUpdateEvent> getValueEvent() {
        return PlayerDamagedByMobValueUpdateEvent.class;
    }

    @Override
    public void run(ValueUpdateEvent tempEvent) {
        PlayerDamagedByMobValueUpdateEvent event = (PlayerDamagedByMobValueUpdateEvent) tempEvent;
        SkyBlockPlayer player = event.getPlayer();

        if (!player.hasTalisman(this)) return;

        if (event.getMob().getEntityType() == EntityType.SPIDER || event.getMob().getEntityType() == EntityType.CAVE_SPIDER || event.getMob().getEntityType() == EntityType.SILVERFISH) {
            event.setValue((float) (((float) event.getValue()) * 0.95));
        }
    }
}
