package net.swofty.type.generic.entity.npc.impl;

import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.minestom.server.component.DataComponents;
import net.minestom.server.entity.EntityType;
import net.minestom.server.entity.LivingEntity;
import net.swofty.type.generic.user.HypixelPlayer;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

@Getter
public class NPCAnimalEntityImpl extends LivingEntity {
    private final ArrayList<HypixelPlayer> inRangeOf = new ArrayList<>();

    public NPCAnimalEntityImpl(@NotNull String bottomDisplay, @NotNull EntityType entityType) {
        super(entityType);

        this.setCustomNameVisible(true);
        this.set(DataComponents.CUSTOM_NAME, Component.text(bottomDisplay));
        setNoGravity(true);
    }

    /**
     * Clears the cache for a player, is only run on quit, {@see QuitAction.java}
     * @param player The player to clear the cache for
     */
    public void clearCache(HypixelPlayer player) {
        inRangeOf.remove(player);
    }
}
