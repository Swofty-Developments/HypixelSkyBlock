package net.swofty.types.generic.entity.villager;

import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.metadata.villager.VillagerMeta;

public abstract class NPCVillagerParameters {
    public abstract String[] holograms();

    public abstract Pos position();

    public abstract boolean looking();

    public abstract VillagerMeta.Profession profession();
}