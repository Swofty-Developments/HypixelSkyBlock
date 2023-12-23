package net.swofty.item.items.enchantment;

import com.mongodb.lang.Nullable;
import net.swofty.enchantment.SkyBlockEnchantment;
import net.swofty.item.SkyBlockItem;
import net.swofty.item.impl.CustomSkyBlockItem;
import net.swofty.user.SkyBlockPlayer;
import net.swofty.user.statistics.ItemStatistics;
import net.swofty.utility.StringUtility;

import java.util.ArrayList;
import java.util.stream.Stream;

public class EnchantedBook implements CustomSkyBlockItem {
    @Override
    public ItemStatistics getStatistics() {
        return ItemStatistics.empty();
    }

    @Override
    public ArrayList<String> getLore(@Nullable SkyBlockPlayer player, SkyBlockItem item) {
        Stream<SkyBlockEnchantment> enchantments = item.getAttributeHandler().getEnchantments();
        ArrayList<String> lore = new ArrayList<>();

        enchantments.forEach(enchantment -> {
            lore.add("§9" + StringUtility.toNormalCase(enchantment.getType().name()) + " " +
                    StringUtility.getAsRomanNumeral(enchantment.getLevel()));
            StringUtility.splitByWordAndLength(enchantment.getType().getDescription(), 20, " ")
                    .forEach(line -> lore.add("§7" + line));
        });

        lore.add(" ");
        lore.add("§7Use this on an item in an Anvil to");
        lore.add("§7apply it!");

        return lore;
    }
}
