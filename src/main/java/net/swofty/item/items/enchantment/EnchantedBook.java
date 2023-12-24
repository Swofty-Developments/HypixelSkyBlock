package net.swofty.item.items.enchantment;

import com.mongodb.lang.Nullable;
import net.swofty.enchantment.SkyBlockEnchantment;
import net.swofty.item.SkyBlockItem;
import net.swofty.item.impl.CustomSkyBlockItem;
import net.swofty.user.SkyBlockPlayer;
import net.swofty.user.statistics.ItemStatistics;
import net.swofty.utility.StringUtility;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class EnchantedBook implements CustomSkyBlockItem {
    @Override
    public ItemStatistics getStatistics() {
        return ItemStatistics.empty();
    }

    @Override
    public ArrayList<String> getLore(@Nullable SkyBlockPlayer player, SkyBlockItem item) {
        List<SkyBlockEnchantment> enchantments = item.getAttributeHandler().getEnchantments().toList();
        ArrayList<String> lore = new ArrayList<>();

        enchantments.forEach(enchantment -> {
            lore.add("§9" + StringUtility.toNormalCase(enchantment.getType().name()) + " " +
                    StringUtility.getAsRomanNumeral(enchantment.getLevel()));
            StringUtility.splitByWordAndLength(enchantment.getType().getDescription(), 30, " ")
                    .forEach(line -> lore.add("§7" + line));
        });

        lore.add(" ");
        lore.add("§7Apply Cost: §3" + enchantments.stream()
                .mapToInt(enchant -> enchant.getType().getApplyCostCalculation().apply(enchant.getLevel()))
                .sum() + " Exp Levels");
        lore.add(" ");

        Set<String> sourceTypes = enchantments.stream()
                .flatMap(enchantment -> enchantment.getType().getSources().stream())
                .map(SkyBlockEnchantment.EnchantmentType.EnchantmentSource::sourceType)
                .map(SkyBlockEnchantment.EnchantmentType.SourceType::toString)
                .collect(Collectors.toSet());

        if (sourceTypes.size() == 1) {
            lore.add("§7Applicable on: §9" + sourceTypes.iterator().next());
        }

        lore.add("§7Use this on an item in an Anvil to");
        lore.add("§7apply it!");

        return lore;
    }
}
