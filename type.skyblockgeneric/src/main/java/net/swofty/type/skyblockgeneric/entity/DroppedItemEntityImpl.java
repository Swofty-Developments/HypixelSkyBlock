package net.swofty.type.skyblockgeneric.entity;

import lombok.Getter;
import net.minestom.server.entity.Entity;
import net.minestom.server.entity.EntityType;
import net.minestom.server.entity.metadata.item.ItemEntityMeta;
import net.swofty.type.generic.item.SkyBlockItem;
import net.swofty.type.generic.item.updater.NonPlayerItemUpdater;
import net.swofty.type.generic.user.HypixelPlayer;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
public class DroppedItemEntityImpl extends Entity {
    @Getter
    private static final Map<HypixelPlayer, List<DroppedItemEntityImpl>> droppedItems = new HashMap<>();
    private final HypixelPlayer player;
    private final long endPickupDelay;

    public DroppedItemEntityImpl(SkyBlockItem item, HypixelPlayer player) {
        super(EntityType.ITEM);

        this.player = player;
        this.endPickupDelay = System.currentTimeMillis() + 500;

        ItemEntityMeta meta = (ItemEntityMeta) this.entityMeta;
        meta.setItem(new NonPlayerItemUpdater(item.getItemStack()).getUpdatedItem().build());

        setAutoViewable(false);
        this.scheduleRemove(Duration.ofSeconds(60));

        droppedItems.computeIfPresent(player, (key, value) -> {
            if (value.size() > 50) {
                value.getFirst().remove();
            }
            value.add(this);
            return value;
        });
        droppedItems.putIfAbsent(player, new ArrayList<>(List.of(this)));
    }

    @Override
    public void spawn() {
        super.spawn();
        addViewer(player);
    }

    public SkyBlockItem getItem() {
        return new SkyBlockItem(((ItemEntityMeta) this.entityMeta).getItem());
    }
}
