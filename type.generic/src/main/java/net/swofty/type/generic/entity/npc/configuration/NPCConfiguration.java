package net.swofty.type.generic.entity.npc.configuration;

import net.minestom.server.coordinate.Pos;
import net.swofty.type.generic.user.HypixelPlayer;
import net.swofty.type.generic.entity.npc.HypixelNPC;
import net.swofty.type.generic.entity.npc.configuration.HumanConfiguration;

public abstract class NPCConfiguration {

    public abstract String[] holograms(HypixelPlayer player);

    public abstract Pos position(HypixelPlayer player);

    public boolean looking() {
        return false;
    }

    public boolean visible(HypixelPlayer player) {
        return true;
    }
}
