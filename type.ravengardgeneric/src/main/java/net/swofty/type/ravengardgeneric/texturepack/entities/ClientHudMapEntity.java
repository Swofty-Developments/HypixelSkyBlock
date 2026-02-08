package net.swofty.type.ravengardgeneric.texturepack.entities;

import net.minestom.server.component.DataComponents;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.coordinate.Vec;
import net.minestom.server.entity.Entity;
import net.minestom.server.entity.EntityType;
import net.minestom.server.entity.metadata.ObjectDataProvider;
import net.minestom.server.entity.metadata.other.ItemFrameMeta;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.minestom.server.network.packet.server.play.SpawnEntityPacket;
import net.minestom.server.utils.Direction;
import net.minestom.server.utils.Rotation;

public class ClientHudMapEntity extends Entity {
    private int mapId;

    public ClientHudMapEntity(int mapId) {
        super(EntityType.ITEM_FRAME);
        this.mapId = mapId;
        this.setNoGravity(true);
        this.setInvisible(true);

        editEntityMeta(ItemFrameMeta.class, meta -> {
            meta.setDirection(Direction.NORTH);
            meta.setRotation(Rotation.NONE);
            meta.setItem(mapItem(mapId));
        });
    }

    public int mapId() {
        return mapId;
    }

    public SpawnEntityPacket createSpawnPacket(Pos position) {
        int data = 0;
        if (getEntityMeta() instanceof ObjectDataProvider provider) {
            data = provider.getObjectData();
        }
        return new SpawnEntityPacket(
                getEntityId(),
                getUuid(),
                getEntityType(),
                position,
                position.yaw(),
                data,
                Vec.ZERO
        );
    }

    public void setMapId(int mapId) {
        if (this.mapId == mapId) {
            return;
        }
        this.mapId = mapId;
        editEntityMeta(ItemFrameMeta.class, meta -> meta.setItem(mapItem(mapId)));
    }

    private static ItemStack mapItem(int mapId) {
        return ItemStack.builder(Material.FILLED_MAP)
                .set(DataComponents.MAP_ID, mapId)
                .build();
    }
}
