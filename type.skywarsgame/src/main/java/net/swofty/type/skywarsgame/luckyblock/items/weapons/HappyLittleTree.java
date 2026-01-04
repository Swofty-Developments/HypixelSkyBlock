package net.swofty.type.skywarsgame.luckyblock.items.weapons;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.type.skywarsgame.luckyblock.items.LuckyBlockItemRegistry;
import net.swofty.type.skywarsgame.luckyblock.items.LuckyBlockWeapon;

import java.util.List;

public class HappyLittleTree implements LuckyBlockWeapon {

    public static final String ID = "happy_little_tree";

    @Override
    public String getId() {
        return ID;
    }

    @Override
    public String getDisplayName() {
        return "Happy Little Tree";
    }

    @Override
    public Material getBaseMaterial() {
        return Material.OAK_SAPLING;
    }

    @Override
    public ItemStack createItemStack() {
        return ItemStack.builder(Material.OAK_SAPLING)
                .customName(Component.text("Happy Little Tree", NamedTextColor.GREEN)
                        .decoration(TextDecoration.ITALIC, false))
                .lore(List.of(
                        Component.empty(),
                        Component.text("Sharpness III", NamedTextColor.GRAY)
                                .decoration(TextDecoration.ITALIC, false),
                        Component.empty(),
                        Component.text("\"There are no mistakes,", NamedTextColor.DARK_GRAY)
                                .decoration(TextDecoration.ITALIC, true),
                        Component.text("only happy accidents.\"", NamedTextColor.DARK_GRAY)
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
        return 1.0 + 3.75;
    }

    @Override
    public boolean hasOnHitEffect() {
        return false;
    }
}
