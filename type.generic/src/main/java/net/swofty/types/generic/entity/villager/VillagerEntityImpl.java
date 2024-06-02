package net.swofty.types.generic.entity.villager;

import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.EntityCreature;
import net.minestom.server.entity.EntityType;
import net.minestom.server.entity.metadata.villager.VillagerMeta;
import net.minestom.server.instance.Instance;

public class VillagerEntityImpl extends EntityCreature {
    public VillagerEntityImpl(VillagerMeta.Profession profession) {
        super(EntityType.VILLAGER);

        VillagerMeta meta = (VillagerMeta) this.entityMeta;
        meta.setVillagerData(new VillagerMeta.VillagerData(
                VillagerMeta.Type.PLAINS, profession, VillagerMeta.Level.EXPERT)
        );
    }

    @Override
    public void tick(long time) {
        Instance instance = getInstance();
        Pos position = getPosition();

        if (instance == null) {
            return;
        }

        if (!instance.isChunkLoaded(position)) {
            instance.loadChunk(position).join();
        }

        try {
            super.tick(time);
        } catch (Exception e) {
            // Suppress odd warnings
        }
    }
}
