package net.swofty.types.generic.entity.npc;

import net.minestom.server.coordinate.Pos;
import net.swofty.types.generic.user.SkyBlockPlayer;

import java.util.function.Function;

public abstract class NPCParameters {
    public abstract String[] holograms();

    public abstract String signature();

    public abstract String texture();

    public abstract Pos position();

    public Function<SkyBlockPlayer, Pos> positionPerPlayer() {
        return p -> position();
    }

    public abstract boolean looking();
}