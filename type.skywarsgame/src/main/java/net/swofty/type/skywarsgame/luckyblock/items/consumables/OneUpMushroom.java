package net.swofty.type.skywarsgame.luckyblock.items.consumables;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.type.skywarsgame.luckyblock.items.LuckyBlockConsumable;
import net.swofty.type.skywarsgame.luckyblock.items.LuckyBlockItemRegistry;
import net.swofty.type.skywarsgame.user.SkywarsPlayer;

import java.util.List;

public class OneUpMushroom implements LuckyBlockConsumable {

    private static final float HEAL_AMOUNT = 10.0f;

    @Override
    public String getId() {
        return "one_up_mushroom";
    }

    @Override
    public String getDisplayName() {
        return "1-up Mushroom";
    }

    @Override
    public ItemStack createItemStack() {
        return ItemStack.builder(Material.RED_MUSHROOM)
                .customName(Component.text(getDisplayName(), NamedTextColor.RED)
                        .decoration(TextDecoration.ITALIC, false)
                        .decoration(TextDecoration.BOLD, true))
                .lore(List.of(
                        Component.text("Instantly restores 5 hearts!", NamedTextColor.GRAY)
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
        float newHealth = Math.min(player.getHealth() + HEAL_AMOUNT, 20.0f);
        player.setHealth(newHealth);

        player.sendMessage(Component.text("1-UP! Health restored!", NamedTextColor.GREEN));
    }
}
