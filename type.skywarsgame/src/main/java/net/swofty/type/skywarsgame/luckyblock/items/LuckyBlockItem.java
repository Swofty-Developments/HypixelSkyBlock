package net.swofty.type.skywarsgame.luckyblock.items;

import net.minestom.server.entity.Entity;
import net.minestom.server.item.ItemStack;
import net.swofty.type.skywarsgame.user.SkywarsPlayer;

public interface LuckyBlockItem {

    String getId();

    String getDisplayName();

    ItemStack createItemStack();

    default void onHit(SkywarsPlayer holder, Entity target) {
    }

    default void onHitReceived(SkywarsPlayer holder, Entity attacker, float damage) {
    }

    default void onTick(SkywarsPlayer holder, boolean isHeld) {
    }

    default boolean onUse(SkywarsPlayer holder) {
        return false;
    }

    default boolean hasTickEffect() {
        return false;
    }

    default boolean hasHitEffect() {
        return false;
    }

    default boolean hasUseEffect() {
        return false;
    }
}
