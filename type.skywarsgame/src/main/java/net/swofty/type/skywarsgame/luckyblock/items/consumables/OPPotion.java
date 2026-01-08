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
import java.util.Random;

public class OPPotion implements LuckyBlockConsumable {

    private static final Random RANDOM = new Random();
    private static final int DURATION_TICKS = 45 * 20;

    @Override
    public String getId() {
        return "op_potion";
    }

    @Override
    public String getDisplayName() {
        return "OP Potion";
    }

    @Override
    public ItemStack createItemStack() {
        return ItemStack.builder(Material.SPLASH_POTION)
                .customName(Component.text(getDisplayName(), NamedTextColor.LIGHT_PURPLE)
                        .decoration(TextDecoration.ITALIC, false)
                        .decoration(TextDecoration.BOLD, true))
                .lore(List.of(
                        Component.text("An incredibly powerful potion!", NamedTextColor.GRAY)
                                .decoration(TextDecoration.ITALIC, false),
                        Component.text("Grants random powerful buffs!", NamedTextColor.GRAY)
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
        int effectCount = 3 + RANDOM.nextInt(3);

        PotionEffect[][] effectPools = {
                {PotionEffect.SPEED, PotionEffect.STRENGTH, PotionEffect.JUMP_BOOST},
                {PotionEffect.RESISTANCE, PotionEffect.FIRE_RESISTANCE},
                {PotionEffect.REGENERATION, PotionEffect.ABSORPTION},
                {PotionEffect.HASTE, PotionEffect.NIGHT_VISION}
        };

        for (int i = 0; i < effectCount && i < effectPools.length; i++) {
            PotionEffect[] pool = effectPools[i];
            PotionEffect effect = pool[RANDOM.nextInt(pool.length)];
            int amplifier = 1 + RANDOM.nextInt(2);

            player.addEffect(new Potion(effect, (byte) amplifier, DURATION_TICKS));
        }

        player.sendMessage(Component.text("OP POTION! You feel incredibly powerful!", NamedTextColor.LIGHT_PURPLE));
    }
}
