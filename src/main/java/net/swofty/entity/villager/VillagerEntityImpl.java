package net.swofty.entity.villager;

import lombok.Getter;
import net.minestom.server.entity.EntityCreature;
import net.minestom.server.entity.EntityType;
import net.minestom.server.entity.ai.EntityAIGroup;
import net.minestom.server.entity.ai.EntityAIGroupBuilder;
import net.minestom.server.entity.metadata.villager.VillagerMeta;

public class VillagerEntityImpl extends EntityCreature {
    public VillagerEntityImpl(VillagerMeta.Profession profession) {
        super(EntityType.VILLAGER);

        VillagerMeta meta = (VillagerMeta) this.entityMeta;
        meta.setVillagerData(new VillagerMeta.VillagerData(
                VillagerMeta.Type.PLAINS, profession, VillagerMeta.Level.EXPERT)
        );
    }
}
