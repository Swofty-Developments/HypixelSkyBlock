package net.swofty.types.generic.entity.npc;

import net.minestom.server.coordinate.Pos;
import net.swofty.types.generic.user.SkyBlockPlayer;
import org.jetbrains.annotations.NonBlocking;

public abstract class NPCParameters {
    public abstract @NonBlocking String[] holograms(SkyBlockPlayer player);

    public abstract @NonBlocking String signature(SkyBlockPlayer player);

    public abstract @NonBlocking String texture(SkyBlockPlayer player);

    public abstract @NonBlocking Pos position(SkyBlockPlayer player);

    public abstract boolean looking();
}