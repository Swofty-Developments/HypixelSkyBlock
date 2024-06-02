package net.swofty.types.generic.entity.animalnpc;

import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.minestom.server.entity.EntityType;
import net.minestom.server.entity.LivingEntity;
import net.swofty.types.generic.user.SkyBlockPlayer;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

@Getter
public class NPCAnimalEntityImpl extends LivingEntity {
    private final ArrayList<SkyBlockPlayer> inRangeOf = new ArrayList<>();

    public NPCAnimalEntityImpl(@NotNull String bottomDisplay, @NotNull EntityType entityType) {
        super(entityType);

        this.setCustomNameVisible(true);
        this.setCustomName(Component.text(bottomDisplay));

        setNoGravity(true);
    }

    /**
     * Clears the cache for a player, is only run on quit, {@see QuitAction.java}
     * @param player The player to clear the cache for
     */
    public void clearCache(SkyBlockPlayer player) {
        inRangeOf.remove(player);
    }
}
