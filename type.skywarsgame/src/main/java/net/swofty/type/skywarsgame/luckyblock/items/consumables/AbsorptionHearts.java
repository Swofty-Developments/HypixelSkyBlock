package net.swofty.type.skywarsgame.luckyblock.items.consumables;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.minestom.server.potion.Potion;
import net.minestom.server.potion.PotionEffect;
import net.swofty.type.skywarsgame.luckyblock.items.LuckyBlockConsumable;
import net.swofty.type.skywarsgame.luckyblock.items.LuckyBlockItemRegistry;
import net.swofty.type.skywarsgame.user.SkywarsPlayer;

import java.util.List;

public class AbsorptionHearts implements LuckyBlockConsumable {

    public static final String ID = "absorption_hearts";
    private static final int DURATION_TICKS = 60 * 20;

    @Override
    public String getId() {
        return ID;
    }

    @Override
    public String getDisplayName() {
        return "Absorption Hearts";
    }

    @Override
    public ItemStack createItemStack() {
        return ItemStack.builder(Material.GOLDEN_APPLE)
                .customName(Component.text(getDisplayName(), NamedTextColor.GOLD)
                        .decoration(TextDecoration.ITALIC, false))
                .lore(List.of(
                        Component.empty(),
                        Component.text("Grants ", NamedTextColor.GRAY)
                                .append(Component.text("10 Absorption Hearts", NamedTextColor.YELLOW))
                                .decoration(TextDecoration.ITALIC, false),
                        Component.text("for 1 minute!", NamedTextColor.GRAY)
                                .decoration(TextDecoration.ITALIC, false),
                        Component.empty(),
                        Component.text("Right-click to use!", NamedTextColor.YELLOW)
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
    public void onConsume(SkywarsPlayer player) {
        player.addEffect(new Potion(PotionEffect.ABSORPTION, (byte) 4, DURATION_TICKS));
        player.sendMessage(Component.text("You gained 10 Absorption Hearts!", NamedTextColor.GOLD));
    }
}
