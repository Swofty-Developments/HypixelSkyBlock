package net.swofty.type.replayviewer.entity;

import lombok.Getter;
import net.kyori.adventure.nbt.BinaryTagIO;
import net.kyori.adventure.nbt.CompoundBinaryTag;
import net.minestom.server.coordinate.Vec;
import net.minestom.server.entity.Entity;
import net.minestom.server.entity.EntityType;
import net.minestom.server.entity.metadata.item.ItemEntityMeta;
import net.minestom.server.item.ItemStack;
import org.tinylog.Logger;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.UUID;

@Getter
public class ReplayDroppedItemEntity extends Entity {
    private final int recordedEntityId;
    private final UUID recordedUuid;
    private final ItemStack itemStack;
    private final int despawnTick;

    public ReplayDroppedItemEntity(int recordedEntityId, UUID recordedUuid,
                                    byte[] itemNbt, int despawnTick) {
        super(EntityType.ITEM);
        this.recordedEntityId = recordedEntityId;
        this.recordedUuid = recordedUuid;
        this.despawnTick = despawnTick;
        this.itemStack = parseItemStack(itemNbt);

        // Configure entity
        setNoGravity(true); // We control position via replay data

        // Set the item in the entity metadata
        if (this.entityMeta instanceof ItemEntityMeta itemMeta) {
            itemMeta.setItem(this.itemStack);
        }
    }

    private ItemStack parseItemStack(byte[] nbtBytes) {
        if (nbtBytes == null || nbtBytes.length == 0) {
            return ItemStack.AIR;
        }

        try {
            CompoundBinaryTag tag = BinaryTagIO.reader()
                .readNameless(new ByteArrayInputStream(nbtBytes));
            return ItemStack.fromItemNBT(tag);
        } catch (IOException e) {
            Logger.error(e, "Failed to parse item NBT for dropped item entity");
            return ItemStack.AIR;
        }
    }

    /**
     * Sets the velocity for visual effect.
     */
    public void setItemVelocity(float vx, float vy, float vz) {
        setVelocity(new Vec(vx, vy, vz));
    }

    /**
     * Checks if this item should despawn at the given tick.
     *
     * @param currentTick the current replay tick
     * @return true if the item should despawn
     */
    public boolean shouldDespawn(int currentTick) {
        return despawnTick > 0 && currentTick >= despawnTick;
    }
}
