package net.swofty.types.generic.item.items.accessories.spider;

import net.minestom.server.entity.EntityType;
import net.swofty.types.generic.event.value.SkyBlockValueEvent;
import net.swofty.types.generic.event.value.ValueUpdateEvent;
import net.swofty.types.generic.event.value.events.PlayerDamagedByMobValueUpdateEvent;
import net.swofty.types.generic.item.ItemTypeLinker;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.SkullHead;
import net.swofty.types.generic.item.impl.TieredTalisman;
import net.swofty.types.generic.user.SkyBlockPlayer;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class SpiderRing extends SkyBlockValueEvent implements TieredTalisman, SkullHead {
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
            event.setValue((float) (((float) event.getValue()) * 0.90));
        }
    }

    @Override
    public List<String> getTalismanDisplay() {
        return List.of("§7Reduces the damage taken from",
                "§7§7Cave Spiders§7 and §7Spiders",
                "§7by §a10%§7.");
    }

    @Override
    public String getSkullTexture(@Nullable SkyBlockPlayer player, SkyBlockItem item) {
        return "7652bd7617e56c756514f83c28dd1d96a7e5e167bf7fb593693fc65046f799";
    }

    @Override
    public ItemTypeLinker getBaseTalismanTier() {
        return ItemTypeLinker.SPIDER_TALISMAN;
    }

    @Override
    public Integer getTier() {
        return 2;
    }
}
