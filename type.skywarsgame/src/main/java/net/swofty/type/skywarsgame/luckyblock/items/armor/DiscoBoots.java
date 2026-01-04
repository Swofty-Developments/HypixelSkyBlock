package net.swofty.type.skywarsgame.luckyblock.items.armor;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.minestom.server.color.Color;
import net.minestom.server.component.DataComponents;
import net.minestom.server.entity.EquipmentSlot;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.type.skywarsgame.luckyblock.items.LuckyBlockArmor;
import net.swofty.type.skywarsgame.luckyblock.items.LuckyBlockItemRegistry;
import net.swofty.type.skywarsgame.user.SkywarsPlayer;

import java.util.List;

public class DiscoBoots implements LuckyBlockArmor {

    public static final String ID = "disco_boots";
    private static final int COLOR_CHANGE_TICKS = 5;

    private static final Color[] RAINBOW_COLORS = {
            new Color(255, 0, 0),
            new Color(255, 127, 0),
            new Color(255, 255, 0),
            new Color(0, 255, 0),
            new Color(0, 0, 255),
            new Color(75, 0, 130),
            new Color(148, 0, 211)
    };

    @Override
    public String getId() {
        return ID;
    }

    @Override
    public String getDisplayName() {
        return "Disco Boots";
    }

    @Override
    public EquipmentSlot getSlot() {
        return EquipmentSlot.BOOTS;
    }

    @Override
    public Material getBaseMaterial() {
        return Material.LEATHER_BOOTS;
    }

    @Override
    public ItemStack createItemStack() {
        return ItemStack.builder(Material.LEATHER_BOOTS)
                .customName(Component.text("Disco Boots", NamedTextColor.LIGHT_PURPLE)
                        .decoration(TextDecoration.ITALIC, false))
                .lore(List.of(
                        Component.empty(),
                        Component.text("Protection III", NamedTextColor.GRAY)
                                .decoration(TextDecoration.ITALIC, false),
                        Component.empty(),
                        Component.text("Changes colors while worn!", NamedTextColor.LIGHT_PURPLE)
                                .decoration(TextDecoration.ITALIC, false),
                        Component.empty(),
                        Component.text("LUCKY BLOCK ITEM", NamedTextColor.GOLD)
                                .decoration(TextDecoration.ITALIC, false)
                                .decoration(TextDecoration.BOLD, true)
                ))
                .set(DataComponents.DYED_COLOR, RAINBOW_COLORS[0])
                .set(LuckyBlockItemRegistry.LUCKY_BLOCK_ITEM_TAG, ID)
                .build();
    }

    @Override
    public void onWornTick(SkywarsPlayer player) {
        if (player.getAliveTicks() % COLOR_CHANGE_TICKS != 0) {
            return;
        }

        int colorIndex = (int) ((player.getAliveTicks() / COLOR_CHANGE_TICKS) % RAINBOW_COLORS.length);
        Color newColor = RAINBOW_COLORS[colorIndex];

        ItemStack currentBoots = player.getBoots();
        if (currentBoots.isAir()) {
            return;
        }

        ItemStack updated = currentBoots.with(DataComponents.DYED_COLOR, newColor);
        player.setBoots(updated);
    }

    @Override
    public boolean hasVisualEffect() {
        return true;
    }
}
