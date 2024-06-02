package net.swofty.types.generic.item.attribute.attributes;

import net.swofty.types.generic.enchantment.SkyBlockEnchantment;
import net.swofty.types.generic.item.attribute.ItemAttribute;
import net.swofty.types.generic.item.impl.CustomSkyBlockItem;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class ItemAttributeEnchantments extends ItemAttribute<SkyBlockEnchantment.ItemEnchantments> {

    @Override
    public String getKey() {
        return "enchantments";
    }

    @Override
    public SkyBlockEnchantment.ItemEnchantments getDefaultValue(@Nullable Class<? extends CustomSkyBlockItem> itemClass) {
        return new SkyBlockEnchantment.ItemEnchantments(new ArrayList<>());
    }

    @Override
    public SkyBlockEnchantment.ItemEnchantments loadFromString(String string) {
        List<SkyBlockEnchantment> enchantments = new ArrayList<>();

        if (string.isEmpty()) {
            return new SkyBlockEnchantment.ItemEnchantments(enchantments);
        }

        String[] split = string.split(",");
        for (String enchantment : split) {
            enchantments.add(SkyBlockEnchantment.deserialize(enchantment));
        }

        return new SkyBlockEnchantment.ItemEnchantments(enchantments);
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
