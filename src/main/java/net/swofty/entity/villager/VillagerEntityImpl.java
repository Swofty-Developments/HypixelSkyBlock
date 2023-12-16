package net.swofty.entity.villager;

import net.minestom.server.entity.Entity;
import net.minestom.server.entity.EntityType;
import net.minestom.server.entity.metadata.villager.VillagerMeta;

public class VillagerEntityImpl extends Entity {
    public VillagerEntityImpl(VillagerMeta.Profession profession) {
        super(EntityType.VILLAGER);

        VillagerMeta meta = (VillagerMeta) this.entityMeta;
        meta.setVillagerData(new VillagerMeta.VillagerData(
                VillagerMeta.Type.PLAINS, profession, VillagerMeta.Level.EXPERT)
        );
    }
}
