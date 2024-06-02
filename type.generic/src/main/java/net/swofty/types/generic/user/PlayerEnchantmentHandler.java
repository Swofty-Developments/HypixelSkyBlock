package net.swofty.types.generic.user;

import net.minestom.server.entity.Player;
import net.swofty.types.generic.enchantment.EnchantmentType;
import net.swofty.types.generic.enchantment.SkyBlockEnchantment;
import net.swofty.types.generic.item.SkyBlockItem;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

public record PlayerEnchantmentHandler(SkyBlockPlayer player) {
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
            return List.of(new SkyBlockItem(player.getItemInHand(Player.Hand.MAIN)));
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

        private Function<SkyBlockPlayer, List<SkyBlockItem>> items;

        EnchantedItemSource(Function<SkyBlockPlayer, List<SkyBlockItem>> items) {
            this.items = items;
        }
    }
}
