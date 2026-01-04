package net.swofty.type.skywarsgame.luckyblock.items.weapons;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.type.skywarsgame.luckyblock.items.LuckyBlockItemRegistry;
import net.swofty.type.skywarsgame.luckyblock.items.LuckyBlockWeapon;

import java.util.List;

public class TheSpoon implements LuckyBlockWeapon {

    public static final String ID = "the_spoon";

    @Override
    public String getId() {
        return ID;
    }

    @Override
    public String getDisplayName() {
        return "The Spoon";
    }

    @Override
    public Material getBaseMaterial() {
        return Material.WOODEN_SHOVEL;
    }

    @Override
    public ItemStack createItemStack() {
        return ItemStack.builder(Material.WOODEN_SHOVEL)
                .customName(Component.text("The Spoon", NamedTextColor.YELLOW)
                        .decoration(TextDecoration.ITALIC, false))
                .lore(List.of(
                        Component.empty(),
                        Component.text("Sharpness V", NamedTextColor.GRAY)
                                .decoration(TextDecoration.ITALIC, false),
                        Component.empty(),
                        Component.text("\"There is no spoon.\"", NamedTextColor.DARK_GRAY)
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
        return 2.5 + 6.25;
    }

    @Override
    public boolean hasOnHitEffect() {
        return false;
    }
}
