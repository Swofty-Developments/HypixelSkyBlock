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

/**
 * +4 Hearts consumable.
 * Grants extra hearts via Absorption effect (simulating permanent health increase).
 */
public class PlusHealthItem implements LuckyBlockConsumable {

    @Override
    public String getId() {
        return "plus_four_hearts";
    }

    @Override
    public String getDisplayName() {
        return "+4 Hearts";
    }

    @Override
    public ItemStack createItemStack() {
        return ItemStack.builder(Material.RED_DYE)
                .customName(Component.text(getDisplayName(), NamedTextColor.RED)
                        .decoration(TextDecoration.ITALIC, false)
                        .decoration(TextDecoration.BOLD, true))
                .lore(List.of(
                        Component.text("Gives you 4 extra hearts", NamedTextColor.GRAY)
                                .decoration(TextDecoration.ITALIC, false),
                        Component.text("for the rest of the game!", NamedTextColor.GRAY)
                                .decoration(TextDecoration.ITALIC, false),
                        Component.empty(),
                        Component.text("Right-click to use!", NamedTextColor.YELLOW)
                                .decoration(TextDecoration.ITALIC, false)
                ))
                .set(LuckyBlockItemRegistry.LUCKY_BLOCK_ITEM_TAG, getId())
                .build();
    }

    @Override
    public void onConsume(SkywarsPlayer player) {
        // Grant Absorption IV which gives 8 extra hearts (4 absorption hearts)
        // Uses very long duration to simulate permanent effect
        player.addEffect(new Potion(PotionEffect.ABSORPTION, (byte) 3, Integer.MAX_VALUE));

        // Also heal to full
        player.setHealth(20.0f);

        player.sendMessage(Component.text("You gained 4 extra golden hearts!", NamedTextColor.GOLD));
    }
}
