package net.swofty.entity.npc;

import net.minestom.server.coordinate.Pos;

public abstract class NPCParameters {
    public abstract String[] holograms();
    public abstract String signature();
    public abstract String texture();
    public abstract Pos position();
    public abstract boolean looking();
}