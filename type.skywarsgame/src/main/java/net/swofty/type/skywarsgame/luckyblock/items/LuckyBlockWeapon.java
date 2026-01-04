package net.swofty.type.skywarsgame.luckyblock.items;

import net.minestom.server.entity.Entity;
import net.minestom.server.item.Material;
import net.swofty.type.skywarsgame.user.SkywarsPlayer;

public interface LuckyBlockWeapon extends LuckyBlockItem {

    Material getBaseMaterial();

    default float onWeaponHit(SkywarsPlayer holder, Entity target, float damage) {
        return -1;
    }

    default void onHeldTick(SkywarsPlayer holder) {
    }

    default double getAttackDamage() {
        return -1;
    }

    default double getAttackSpeed() {
        return -1;
    }

    default boolean hasOnHitEffect() {
        return true;
    }

    default boolean hasPassiveBuff() {
        return false;
    }

    default int getMaxUses() {
        return -1;
    }

    default boolean dealsTrueDamage() {
        return false;
    }

    default double getKnockbackMultiplier() {
        return 1.0;
    }

    @Override
    default boolean hasHitEffect() {
        return hasOnHitEffect();
    }

    @Override
    default boolean hasTickEffect() {
        return hasPassiveBuff();
    }
}
