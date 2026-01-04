package net.swofty.type.skywarsgame.luckyblock.items.armor;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.minestom.server.entity.EquipmentSlot;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.type.skywarsgame.luckyblock.items.LuckyBlockArmor;
import net.swofty.type.skywarsgame.luckyblock.items.LuckyBlockItemRegistry;

import java.util.List;

public class ElDoradoLeggings implements LuckyBlockArmor {

    public static final String ID = "el_dorado_leggings";

    @Override
    public String getId() {
        return ID;
    }

    @Override
    public String getDisplayName() {
        return "El Dorado Leggings";
    }

    @Override
    public EquipmentSlot getSlot() {
        return EquipmentSlot.LEGGINGS;
    }

    @Override
    public Material getBaseMaterial() {
        return Material.GOLDEN_LEGGINGS;
    }

    @Override
    public ItemStack createItemStack() {
        return ItemStack.builder(Material.GOLDEN_LEGGINGS)
                .customName(Component.text("El Dorado Leggings", NamedTextColor.GOLD)
                        .decoration(TextDecoration.ITALIC, false))
                .lore(List.of(
                        Component.empty(),
                        Component.text("Protection III", NamedTextColor.GRAY)
                                .decoration(TextDecoration.ITALIC, false),
                        Component.empty(),
                        Component.text("The legendary city of gold", NamedTextColor.DARK_GRAY)
                                .decoration(TextDecoration.ITALIC, true),
                        Component.empty(),
                        Component.text("LUCKY BLOCK ITEM", NamedTextColor.GOLD)
                                .decoration(TextDecoration.ITALIC, false)
                                .decoration(TextDecoration.BOLD, true)
                ))
                .set(LuckyBlockItemRegistry.LUCKY_BLOCK_ITEM_TAG, ID)
                .build();
    }
}
