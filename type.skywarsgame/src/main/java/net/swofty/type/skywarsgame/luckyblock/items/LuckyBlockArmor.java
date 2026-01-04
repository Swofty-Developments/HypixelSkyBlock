package net.swofty.type.skywarsgame.luckyblock.items;

import net.minestom.server.entity.EquipmentSlot;
import net.minestom.server.item.Material;
import net.swofty.type.skywarsgame.user.SkywarsPlayer;

public interface LuckyBlockArmor extends LuckyBlockItem {

    EquipmentSlot getSlot();

    Material getBaseMaterial();

    default void onEquip(SkywarsPlayer player) {
    }

    default void onUnequip(SkywarsPlayer player) {
    }

    default void onDamaged(SkywarsPlayer player, net.minestom.server.entity.Entity attacker, float damage) {
    }

    default void onWornTick(SkywarsPlayer player) {
    }

    default boolean hasTrailEffect() {
        return false;
    }

    default boolean hasDamageEffect() {
        return false;
    }

    default boolean hasPermanentBuff() {
        return false;
    }

    default boolean hasVisualEffect() {
        return false;
    }

    default boolean hasHitEffect() {
        return false;
    }

    default void onHit(SkywarsPlayer holder, net.minestom.server.entity.Entity target) {
    }

    @Override
    default boolean hasTickEffect() {
        return hasTrailEffect() || hasPermanentBuff() || hasVisualEffect();
    }
}
