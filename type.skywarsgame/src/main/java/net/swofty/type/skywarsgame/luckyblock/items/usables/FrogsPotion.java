package net.swofty.type.skywarsgame.luckyblock.items.usables;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.minestom.server.potion.Potion;
import net.minestom.server.potion.PotionEffect;
import net.swofty.type.skywarsgame.luckyblock.items.LuckyBlockItem;
import net.swofty.type.skywarsgame.luckyblock.items.LuckyBlockItemRegistry;
import net.swofty.type.skywarsgame.user.SkywarsPlayer;

import java.util.List;

public class FrogsPotion implements LuckyBlockItem {

    private static final int DURATION_TICKS = 60 * 20;

    @Override
    public String getId() {
        return "frogs_potion";
    }

    @Override
    public String getDisplayName() {
        return "Frog's Potion";
    }

    @Override
    public ItemStack createItemStack() {
        return ItemStack.builder(Material.POTION)
                .customName(Component.text(getDisplayName(), NamedTextColor.GREEN)
                        .decoration(TextDecoration.ITALIC, false)
                        .decoration(TextDecoration.BOLD, true))
                .lore(List.of(
                        Component.text("Leap like a frog!", NamedTextColor.GRAY)
                                .decoration(TextDecoration.ITALIC, false),
                        Component.text("Speed I and Jump Boost II", NamedTextColor.GRAY)
                                .decoration(TextDecoration.ITALIC, false),
                        Component.text("for 60 seconds.", NamedTextColor.GRAY)
                                .decoration(TextDecoration.ITALIC, false),
                        Component.empty(),
                        Component.text("Right-click to use!", NamedTextColor.YELLOW)
                                .decoration(TextDecoration.ITALIC, false)
                ))
                .set(LuckyBlockItemRegistry.LUCKY_BLOCK_ITEM_TAG, getId())
                .build();
    }

    @Override
    public boolean onUse(SkywarsPlayer holder) {
        holder.addEffect(new Potion(PotionEffect.SPEED, (byte) 0, DURATION_TICKS));
        holder.addEffect(new Potion(PotionEffect.JUMP_BOOST, (byte) 1, DURATION_TICKS));
        holder.sendMessage(Component.text("You feel like a frog!", NamedTextColor.GREEN));
        return true;
    }

    @Override
    public boolean hasUseEffect() {
        return true;
    }
}
