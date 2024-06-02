package net.swofty.types.generic.entity.animalnpc;

import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.EntityType;

public abstract class NPCAnimalParameters {
    public abstract String[] holograms();
    public abstract int hologramYOffset();
    public abstract EntityType entityType();
    public abstract Pos position();
    public abstract boolean looking();
}