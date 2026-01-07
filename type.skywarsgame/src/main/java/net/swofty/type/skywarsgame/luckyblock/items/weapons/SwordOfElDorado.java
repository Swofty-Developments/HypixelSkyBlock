package net.swofty.type.skywarsgame.luckyblock.items.weapons;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.minestom.server.entity.Entity;
import net.minestom.server.entity.LivingEntity;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.type.skywarsgame.luckyblock.items.LuckyBlockItemRegistry;
import net.swofty.type.skywarsgame.luckyblock.items.LuckyBlockWeapon;
import net.swofty.type.skywarsgame.user.SkywarsPlayer;

import java.util.List;

public class SwordOfElDorado implements LuckyBlockWeapon {

    public static final String ID = "sword_of_el_dorado";

    @Override
    public String getId() {
        return ID;
    }

    @Override
    public String getDisplayName() {
        return "Sword of El Dorado";
    }

    @Override
    public Material getBaseMaterial() {
        return Material.GOLDEN_SWORD;
    }

    @Override
    public ItemStack createItemStack() {
        return ItemStack.builder(Material.GOLDEN_SWORD)
                .customName(Component.text("Sword of El Dorado", NamedTextColor.GOLD)
                        .decoration(TextDecoration.ITALIC, false))
                .lore(List.of(
                        Component.empty(),
                        Component.text("Sharpness IV", NamedTextColor.GRAY)
                                .decoration(TextDecoration.ITALIC, false),
                        Component.text("Fire Aspect I", NamedTextColor.GRAY)
                                .decoration(TextDecoration.ITALIC, false),
                        Component.empty(),
                        Component.text("Sets enemies ablaze.", NamedTextColor.RED)
                                .decoration(TextDecoration.ITALIC, false),
                        Component.empty(),
                        Component.text("\"The lost city's treasure.\"", NamedTextColor.DARK_GRAY)
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
            living.setFireTicks(80);
        }

        return damage;
    }

    @Override
    public double getAttackDamage() {
        return 4.0 + 5.0;
    }

    @Override
    public boolean hasOnHitEffect() {
        return true;
    }
}
