package net.swofty.types.generic.entity.npc;

import net.minestom.server.coordinate.Pos;
import net.swofty.types.generic.user.SkyBlockPlayer;

public abstract class NPCParameters {
    public abstract String[] holograms(SkyBlockPlayer player);

    public abstract String signature(SkyBlockPlayer player);

    public abstract String texture(SkyBlockPlayer player);

    public abstract Pos position(SkyBlockPlayer player);

    public abstract boolean looking();
}