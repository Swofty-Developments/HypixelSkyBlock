package net.swofty.types.generic.item.items.enchantment;

import com.mongodb.lang.Nullable;
import net.swofty.types.generic.enchantment.SkyBlockEnchantment;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.user.statistics.ItemStatistics;
import net.swofty.types.generic.utility.groups.EnchantItemGroups;
import net.swofty.types.generic.utility.StringUtility;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.CustomSkyBlockItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class EnchantedBook implements CustomSkyBlockItem {
    @Override
    public ItemStatistics getStatistics(SkyBlockItem instance) {
        return ItemStatistics.empty();
    }

    @Override
    public ArrayList<String> getLore(@Nullable SkyBlockPlayer player, SkyBlockItem item) {
        List<SkyBlockEnchantment> enchantments = item.getAttributeHandler().getEnchantments().toList();
        ArrayList<String> lore = new ArrayList<>();

        enchantments.forEach(enchantment -> {
            lore.add("§9" + StringUtility.toNormalCase(enchantment.type().name()) + " " +
                    StringUtility.getAsRomanNumeral(enchantment.level()));
            StringUtility.splitByWordAndLength(enchantment.type().getDescription(enchantment.level(), player), 30)
                    .forEach(line -> lore.add("§7" + line));
        });

        lore.add(" ");
        lore.add("§7Apply Cost: §3" + enchantments.stream()
                .mapToInt(enchant -> enchant.type().getApplyCost(enchant.level(), player))
                .sum() + " Exp Levels");
        lore.add(" ");

        Set<String> sourceTypes = enchantments.stream()
                .flatMap(enchantment -> enchantment.type().getEnch().getGroups().stream())
                .map(EnchantItemGroups::getDisplayName)
                .collect(Collectors.toSet());

        lore.add("§7Applicable on: §9" + String.join("§7, §9", sourceTypes));
        lore.add("§7Use this on an item in an Anvil to");
        lore.add("§7apply it!");

        return lore;
    }
}
