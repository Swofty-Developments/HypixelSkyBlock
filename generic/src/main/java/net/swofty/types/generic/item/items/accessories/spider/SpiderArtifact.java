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

public class SpiderArtifact extends SkyBlockValueEvent implements Talisman {
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
            event.setValue((float) (((float) event.getValue()) * 0.85));
        }
    }

    @Override
    public List<String> getTalismanDisplay() {
        return List.of("§7Reduces the damage taken from",
                "§7§7Cave Spiders§7 and §7Spiders",
                "§7by §a15%§7.");
    }

    @Override
    public String getSkullTexture(@Nullable SkyBlockPlayer player, SkyBlockItem item) {
        return "8300986ed0a04ea79904f6ae53f49ed3a0ff5b1df62bba622ecbd3777f156df8";
    }
}
