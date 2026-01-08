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

public class SuperStar implements LuckyBlockConsumable {

    private static final int DURATION_TICKS = 15 * 20;

    @Override
    public String getId() {
        return "super_star";
    }

    @Override
    public String getDisplayName() {
        return "Super Star";
    }

    @Override
    public ItemStack createItemStack() {
        return ItemStack.builder(Material.NETHER_STAR)
                .customName(Component.text(getDisplayName(), NamedTextColor.GOLD)
                        .decoration(TextDecoration.ITALIC, false)
                        .decoration(TextDecoration.BOLD, true))
                .lore(List.of(
                        Component.text("Become nearly invincible", NamedTextColor.GRAY)
                                .decoration(TextDecoration.ITALIC, false),
                        Component.text("for 15 seconds!", NamedTextColor.GRAY)
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
        player.addEffect(new Potion(PotionEffect.RESISTANCE, (byte) 4, DURATION_TICKS));
        player.addEffect(new Potion(PotionEffect.REGENERATION, (byte) 3, DURATION_TICKS));
        player.addEffect(new Potion(PotionEffect.GLOWING, (byte) 0, DURATION_TICKS));

        player.sendMessage(Component.text("You are INVINCIBLE for 15 seconds!", NamedTextColor.GOLD));
    }
}
