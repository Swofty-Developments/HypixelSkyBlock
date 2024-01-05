package net.swofty.item.items.enchantment;

import com.mongodb.lang.Nullable;
import net.swofty.enchantment.SkyBlockEnchantment;
import net.swofty.enchantment.EnchantmentSource;
import net.swofty.item.SkyBlockItem;
import net.swofty.item.impl.CustomSkyBlockItem;
import net.swofty.user.SkyBlockPlayer;
import net.swofty.user.statistics.ItemStatistics;
import net.swofty.utility.ItemGroups;
import net.swofty.utility.StringUtility;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class EnchantedBook implements CustomSkyBlockItem {
    @Override
    public ItemStatistics getStatistics() {
        return ItemStatistics.EMPTY;
    }

    @Override
    public ArrayList<String> getLore(@Nullable SkyBlockPlayer player, SkyBlockItem item) {
        List<SkyBlockEnchantment> enchantments = item.getAttributeHandler().getEnchantments().toList();
        ArrayList<String> lore = new ArrayList<>();

        enchantments.forEach(enchantment -> {
            lore.add("§9" + StringUtility.toNormalCase(enchantment.type().name()) + " " +
                    StringUtility.getAsRomanNumeral(enchantment.level()));
            StringUtility.splitByWordAndLength(enchantment.type().getDescription(enchantment.level()), 30, " ")
                    .forEach(line -> lore.add("§7" + line));
        });

        lore.add(" ");
        lore.add("§7Apply Cost: §3" + enchantments.stream()
                .mapToInt(enchant -> enchant.type().getApplyCost(enchant.level()))
                .sum() + " Exp Levels");
        lore.add(" ");

        Set<String> sourceTypes = enchantments.stream()
                .flatMap(enchantment -> enchantment.type().getEnch().getGroups().stream())
                .map(ItemGroups::toString)
                .collect(Collectors.toSet());

        if (sourceTypes.size() == 1) {
            lore.add("§7Applicable on: §9" + sourceTypes.iterator().next());
        } else {
            lore.addAll(StringUtility.splitByWordAndLength(
                    "§7Applicable on: §9" + String.join("§7, §9", sourceTypes), 36, ",")
            );
        }

        lore.add("§7Use this on an item in an Anvil to");
        lore.add("§7apply it!");

        return lore;
    }
}
