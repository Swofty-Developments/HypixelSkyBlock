package net.swofty.type.generic.entity.npc.configuration;

import net.minestom.server.coordinate.Pos;
import net.minestom.server.instance.Instance;
import net.swofty.type.generic.HypixelConst;
import net.swofty.type.generic.user.HypixelPlayer;
import org.jetbrains.annotations.Nullable;

public interface NPCConfiguration {

    String[] holograms(HypixelPlayer player);

    Pos position(HypixelPlayer player);

    default boolean looking(HypixelPlayer player) {
        return false;
    }

    default boolean visible(HypixelPlayer player) {
        return true;
    }

    @Nullable
    default String chatName() {
        return null;
    }

    /**
     * Gets the chat name for this NPC specific to a player.
     * Override this for NPCs that need per-player colored names.
     * @param player The player viewing this NPC
     * @return The formatted chat name, or null to use default
     */
    @Nullable
    default String chatName(HypixelPlayer player) {
        return chatName();
    }

    default Instance instance() {
        return HypixelConst.getInstanceContainer();
    }
}
