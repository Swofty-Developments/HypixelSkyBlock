package net.swofty.commons.skyblock.entity;

import lombok.Getter;
import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.Entity;
import net.minestom.server.entity.EntityType;
import net.minestom.server.entity.metadata.display.ItemDisplayMeta;
import net.minestom.server.timer.ExecutionType;
import net.minestom.server.timer.TaskSchedule;
import net.swofty.commons.skyblock.item.SkyBlockItem;
import net.swofty.commons.skyblock.user.SkyBlockPlayer;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
public class DroppedItemEntityImpl extends Entity {
    @Getter
    private static Map<SkyBlockPlayer, List<DroppedItemEntityImpl>> droppedItems = new HashMap<>();
    private final SkyBlockPlayer player;
    private final long endPickupDelay;

    public DroppedItemEntityImpl(SkyBlockItem item, SkyBlockPlayer player) {
        super(EntityType.ITEM_DISPLAY);

        this.player = player;
        this.endPickupDelay = System.currentTimeMillis() + 500;

        ItemDisplayMeta meta = (ItemDisplayMeta) this.entityMeta;
        meta.setItemStack(item.getItemStack());
        meta.setDisplayContext(ItemDisplayMeta.DisplayContext.GROUND);
        meta.setShadowRadius(0.2f);
        meta.setShadowStrength(2);

        setAutoViewable(false);

        this.scheduleRemove(Duration.ofSeconds(60));
        this.addViewer(player);

        droppedItems.computeIfPresent(player, (key, value) -> {
            if (value.size() > 50) {
                value.get(0).remove();
            }
            value.add(this);
            return value;
        });
        droppedItems.putIfAbsent(player, new ArrayList<>(List.of(this)));
    }

    public SkyBlockItem getItem() {
        return new SkyBlockItem(((ItemDisplayMeta) this.entityMeta).getItemStack());
    }

    public static void spinLoop() {
        MinecraftServer.getSchedulerManager().submitTask(() -> {
            droppedItems.forEach((player, items) -> {
                List<DroppedItemEntityImpl> toRemove = new ArrayList<>();
                items.forEach(item -> {
                    if (item.instance == null) {
                        // Only runs max once
                        return;
                    }

                    if (item.isRemoved()) {
                        toRemove.add(item);
                        return;
                    }

                    if (item.isOnGround())
                        item.setInstance(item.getInstance(), item.getPosition().withYaw(item.getPosition().yaw() + 6));
                });

                toRemove.forEach(item -> {
                    droppedItems.get(player).remove(item);
                });
            });
            return TaskSchedule.tick(1);
        }, ExecutionType.ASYNC);
    }
}
