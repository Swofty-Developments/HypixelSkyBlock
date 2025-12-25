package net.swofty.commons.skyblock.item.attribute.attributes;

import net.swofty.commons.skyblock.item.UnderstandableSkyBlockEnchantment;
import net.swofty.commons.skyblock.item.attribute.ItemAttribute;
import net.swofty.commons.skyblock.statistics.ItemStatistics;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class ItemAttributeEnchantments extends ItemAttribute<UnderstandableSkyBlockEnchantment.ItemEnchantments> {

    @Override
    public String getKey() {
        return "enchantments";
    }

    @Override
    public UnderstandableSkyBlockEnchantment.ItemEnchantments getDefaultValue(@Nullable ItemStatistics defaultStatistics) {
        return new UnderstandableSkyBlockEnchantment.ItemEnchantments(new ArrayList<>());
    }

    @Override
    public UnderstandableSkyBlockEnchantment.ItemEnchantments loadFromString(String string) {
        List<UnderstandableSkyBlockEnchantment> enchantments = new ArrayList<>();

        if (string.isEmpty()) {
            return new UnderstandableSkyBlockEnchantment.ItemEnchantments(enchantments);
        }

        String[] split = string.split(",");
        for (String enchantment : split) {
            enchantments.add(UnderstandableSkyBlockEnchantment.deserialize(enchantment));
        }

        return new UnderstandableSkyBlockEnchantment.ItemEnchantments(enchantments);
    }

    @Override
    public String saveIntoString() {
        List<String> serializedEnchantments = new ArrayList<>();

        this.value.enchantments().forEach(enchantment -> {
            serializedEnchantments.add(enchantment.serialize());
        });

        return String.join(",", serializedEnchantments);
    }
}
