package net.swofty.types.generic.item.items.accessories.dungeon;

import net.minestom.server.entity.EntityType;
import net.swofty.types.generic.event.value.SkyBlockValueEvent;
import net.swofty.types.generic.event.value.ValueUpdateEvent;
import net.swofty.types.generic.event.value.events.PlayerDamagedByMobValueUpdateEvent;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.Talisman;
import net.swofty.types.generic.user.SkyBlockPlayer;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class WitherRelic extends SkyBlockValueEvent implements Talisman {
    @Override
    public List<String> getTalismanDisplay() {
        return List.of("§7Reduces the damage taken from",
                "§7Wither by §a25%§7.");
    }

    @Override
    public String getSkullTexture(@Nullable SkyBlockPlayer player, SkyBlockItem item) {
        return "964e1c3e315c8d8fffc37985b6681c5bd16a6f97ffd07199e8a05efbef103793";
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

        if (event.getMob().getEntityType() == EntityType.WITHER) {
            event.setValue((float) (((float) event.getValue()) * 0.75));
        }
    }
}
