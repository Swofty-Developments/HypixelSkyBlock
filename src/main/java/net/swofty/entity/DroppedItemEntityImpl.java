package net.swofty.entity;

import lombok.Getter;
import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.Entity;
import net.minestom.server.entity.EntityType;
import net.minestom.server.entity.metadata.display.ItemDisplayMeta;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.minestom.server.timer.TaskSchedule;
import net.swofty.item.SkyBlockItem;
import net.swofty.user.SkyBlockPlayer;
import org.jetbrains.annotations.NotNull;

import java.time.Duration;
import java.util.*;

public class DroppedItemEntityImpl extends Entity {
    @Getter
    private static Map<SkyBlockPlayer, List<DroppedItemEntityImpl>> droppedItems = new HashMap<>();
    @Getter
    private final SkyBlockPlayer player;

    public DroppedItemEntityImpl(SkyBlockItem item, SkyBlockPlayer player) {
        super(EntityType.ITEM_DISPLAY);

        this.player = player;

        ItemDisplayMeta meta = (ItemDisplayMeta) this.entityMeta;
        meta.setItemStack(item.getItemStack());
        meta.setDisplayContext(ItemDisplayMeta.DisplayContext.GROUND);
        meta.setInterpolationDuration(5);
        meta.setInterpolationStartDelta(5);

        meta.setShadowRadius(0.2f);
        meta.setShadowStrength(5);

        this.scheduleRemove(Duration.ofSeconds(60));

        MinecraftServer.getSchedulerManager().submitTask(() -> {
            if (instance == null) {
                return TaskSchedule.tick(1);
            }

            if (this.isRemoved()) {
                droppedItems.computeIfPresent(player, (key, value) -> {
                    value.remove(this);
                    return value;
                });
                return TaskSchedule.stop();
            }

            setInstance(getInstance(), getPosition().withYaw(getPosition().yaw() + 3));

            return TaskSchedule.tick(1);
        });

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
}
