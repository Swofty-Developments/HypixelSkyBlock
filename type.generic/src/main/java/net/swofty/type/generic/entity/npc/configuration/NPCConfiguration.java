package net.swofty.type.generic.entity.npc.configuration;

import net.minestom.server.coordinate.Pos;
import net.swofty.type.generic.user.HypixelPlayer;

public interface NPCConfiguration {

    String[] holograms(HypixelPlayer player);

    Pos position(HypixelPlayer player);

    default boolean looking() {
        return false;
    }

    default boolean visible(HypixelPlayer player) {
        return true;
    }
}
