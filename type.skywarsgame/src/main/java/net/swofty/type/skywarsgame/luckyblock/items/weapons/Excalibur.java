package net.swofty.type.skywarsgame.luckyblock.items.weapons;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.type.skywarsgame.luckyblock.items.LuckyBlockItemRegistry;
import net.swofty.type.skywarsgame.luckyblock.items.LuckyBlockWeapon;

import java.util.List;

public class Excalibur implements LuckyBlockWeapon {

    public static final String ID = "excalibur";

    @Override
    public String getId() {
        return ID;
    }

    @Override
    public String getDisplayName() {
        return "Excalibur";
    }

    @Override
    public Material getBaseMaterial() {
        return Material.DIAMOND_SWORD;
    }

    @Override
    public ItemStack createItemStack() {
        return ItemStack.builder(Material.DIAMOND_SWORD)
                .customName(Component.text("Excalibur", NamedTextColor.AQUA)
                        .decoration(TextDecoration.ITALIC, false))
                .lore(List.of(
                        Component.empty(),
                        Component.text("+7 Attack Damage", NamedTextColor.GRAY)
                                .decoration(TextDecoration.ITALIC, false),
                        Component.empty(),
                        Component.text("\"The sword of kings.\"", NamedTextColor.DARK_GRAY)
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
    public double getAttackDamage() {
        return 7.0;
    }

    @Override
    public boolean hasOnHitEffect() {
        return false;
    }
}
