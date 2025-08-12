package net.swofty.type.skyblockgeneric.user;

import net.minestom.server.entity.PlayerHand;
import net.swofty.type.generic.enchantment.EnchantmentType;
import net.swofty.type.generic.enchantment.SkyBlockEnchantment;
import net.swofty.type.generic.item.SkyBlockItem;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

public record PlayerEnchantmentHandler(HypixelPlayer player) {
    public @Nullable EnchantmentHandlerResponse getItemWithHighestLevelOf(EnchantmentType type, EnchantedItemSource... sources) {
        SkyBlockItem highest = null;
        int highestLevel = 0;
        EnchantedItemSource usedSource = null;

        for (EnchantedItemSource source : sources) {
            for (SkyBlockItem item : source.items.apply(player)) {
                SkyBlockEnchantment enchantment = item.getAttributeHandler().getEnchantment(type);
                if (enchantment == null) continue;

                int level = enchantment.level();
                if (level > highestLevel) {
                    highest = item;
                    highestLevel = level;
                    usedSource = source;
                }
            }
        }

        return highest == null ? null : new EnchantmentHandlerResponse(highest, highestLevel, usedSource);
    }

    public record EnchantmentHandlerResponse(SkyBlockItem item, int level, EnchantedItemSource usedSource) { }

    public enum EnchantedItemSource {
        HAND(player -> {
            return List.of(new SkyBlockItem(player.getItemInHand(PlayerHand.MAIN)));
        }),
        ARMOR(player -> {
            return List.of(
                    new SkyBlockItem(player.getHelmet()),
                    new SkyBlockItem(player.getChestplate()),
                    new SkyBlockItem(player.getLeggings()),
                    new SkyBlockItem(player.getBoots())
            );
        }),
        ACCESSORY(player -> player.getAccessoryBag().getAllAccessories()),
        INVENTORY(player -> Arrays.stream(player.getAllInventoryItems()).toList()),
        ;

        private Function<HypixelPlayer, List<SkyBlockItem>> items;

        EnchantedItemSource(Function<HypixelPlayer, List<SkyBlockItem>> items) {
            this.items = items;
        }
    }
}
