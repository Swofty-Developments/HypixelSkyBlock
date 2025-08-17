package net.swofty.type.generic.entity.npc;

import net.minestom.server.coordinate.Pos;
import net.swofty.type.generic.user.HypixelPlayer;
import org.jetbrains.annotations.NonBlocking;

public abstract class NPCParameters {
    public abstract @NonBlocking String[] holograms(HypixelPlayer player);

    public abstract @NonBlocking String signature(HypixelPlayer player);

    public abstract @NonBlocking String texture(HypixelPlayer player);

    public abstract @NonBlocking Pos position(HypixelPlayer player);

    public abstract boolean looking();
}