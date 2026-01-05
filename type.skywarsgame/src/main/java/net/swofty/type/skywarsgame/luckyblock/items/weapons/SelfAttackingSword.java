package net.swofty.type.skywarsgame.luckyblock.items.weapons;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.minestom.server.entity.Entity;
import net.minestom.server.entity.damage.Damage;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.type.skywarsgame.luckyblock.items.LuckyBlockItemRegistry;
import net.swofty.type.skywarsgame.luckyblock.items.LuckyBlockWeapon;
import net.swofty.type.skywarsgame.user.SkywarsPlayer;

import java.util.List;

public class SelfAttackingSword implements LuckyBlockWeapon {

    public static final String ID = "self_attacking_sword";

    @Override
    public String getId() {
        return ID;
    }

    @Override
    public String getDisplayName() {
        return "Self-Attacking Sword";
    }

    @Override
    public Material getBaseMaterial() {
        return Material.IRON_SWORD;
    }

    @Override
    public ItemStack createItemStack() {
        return ItemStack.builder(Material.IRON_SWORD)
                .customName(Component.text("Self-Attacking Sword", NamedTextColor.RED)
                        .decoration(TextDecoration.ITALIC, false))
                .lore(List.of(
                        Component.empty(),
                        Component.text("This cursed blade attacks", NamedTextColor.GRAY)
                                .decoration(TextDecoration.ITALIC, false),
                        Component.text("its wielder instead!", NamedTextColor.GRAY)
                                .decoration(TextDecoration.ITALIC, false),
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
        holder.damage(Damage.fromEntity(null, damage));
        return 0;
    }

    @Override
    public double getAttackDamage() {
        return 6.0;
    }

    @Override
    public boolean hasOnHitEffect() {
        return true;
    }
}
