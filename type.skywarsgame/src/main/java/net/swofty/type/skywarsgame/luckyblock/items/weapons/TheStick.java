package net.swofty.type.skywarsgame.luckyblock.items.weapons;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.minestom.server.coordinate.Vec;
import net.minestom.server.entity.Entity;
import net.minestom.server.entity.LivingEntity;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.type.skywarsgame.luckyblock.items.LuckyBlockItemRegistry;
import net.swofty.type.skywarsgame.luckyblock.items.LuckyBlockWeapon;
import net.swofty.type.skywarsgame.user.SkywarsPlayer;

import java.util.List;

public class TheStick implements LuckyBlockWeapon {

    public static final String ID = "the_stick";
    private static final double KNOCKBACK_MULTIPLIER = 4.0;

    @Override
    public String getId() {
        return ID;
    }

    @Override
    public String getDisplayName() {
        return "The Stick";
    }

    @Override
    public Material getBaseMaterial() {
        return Material.STICK;
    }

    @Override
    public ItemStack createItemStack() {
        return ItemStack.builder(Material.STICK)
                .customName(Component.text("The Stick", NamedTextColor.YELLOW)
                        .decoration(TextDecoration.ITALIC, false))
                .lore(List.of(
                        Component.empty(),
                        Component.text("Knockback IV", NamedTextColor.GRAY)
                                .decoration(TextDecoration.ITALIC, false),
                        Component.empty(),
                        Component.text("A legendary weapon", NamedTextColor.DARK_GRAY)
                                .decoration(TextDecoration.ITALIC, true),
                        Component.text("feared by all.", NamedTextColor.DARK_GRAY)
                                .decoration(TextDecoration.ITALIC, true),
                        Component.empty(),
                        Component.text("LUCKY BLOCK ITEM", NamedTextColor.GOLD)
                                .decoration(TextDecoration.ITALIC, false)
                                .decoration(TextDecoration.BOLD, true)
                ))
                .set(LuckyBlockItemRegistry.LUCKY_BLOCK_ITEM_TAG, ID)
                .build();
    }

    @Override
    public float onWeaponHit(SkywarsPlayer holder, Entity target, float damage) {
        if (target instanceof LivingEntity living) {
            Vec direction = target.getPosition().asVec()
                    .sub(holder.getPosition().asVec())
                    .normalize();

            Vec knockback = new Vec(
                    direction.x() * 25 * KNOCKBACK_MULTIPLIER,
                    8 * KNOCKBACK_MULTIPLIER,
                    direction.z() * 25 * KNOCKBACK_MULTIPLIER
            );

            living.setVelocity(knockback);
        }

        return damage;
    }

    @Override
    public double getAttackDamage() {
        return 2.0;
    }

    @Override
    public double getKnockbackMultiplier() {
        return KNOCKBACK_MULTIPLIER;
    }

    @Override
    public boolean hasOnHitEffect() {
        return true;
    }
}
