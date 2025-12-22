package net.swofty.type.generic.entity.npc.impl;

import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.minestom.server.component.DataComponents;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.EntityCreature;
import net.minestom.server.entity.EntityType;
import net.minestom.server.entity.VillagerProfession;
import net.minestom.server.entity.VillagerType;
import net.minestom.server.entity.metadata.villager.VillagerMeta;
import net.minestom.server.instance.Instance;
import net.swofty.type.generic.user.HypixelPlayer;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

@Getter
public class NPCVillagerEntityImpl extends EntityCreature {
    private final ArrayList<HypixelPlayer> inRangeOf = new ArrayList<>();

    public NPCVillagerEntityImpl(@NotNull String bottomDisplay, VillagerProfession profession) {
        super(EntityType.VILLAGER);

        this.setCustomNameVisible(true);
        this.set(DataComponents.CUSTOM_NAME, Component.text(bottomDisplay));

        VillagerMeta meta = (VillagerMeta) this.entityMeta;
        meta.setVillagerData(new VillagerMeta.VillagerData(
                VillagerType.PLAINS, profession, VillagerMeta.Level.EXPERT)
        );

        setNoGravity(true);
    }

    /**
     * Clears the cache for a player, is only run on quit
     * @param player The player to clear the cache for
     */
    public void clearCache(HypixelPlayer player) {
        inRangeOf.remove(player);
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
