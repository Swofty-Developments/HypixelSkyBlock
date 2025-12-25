package net.swofty.type.skyblockgeneric.enchantment;

import lombok.Builder;
import net.swofty.commons.skyblock.item.UnderstandableSkyBlockEnchantment;

import java.util.ArrayList;
import java.util.List;

@Builder
public record SkyBlockEnchantment(EnchantmentType type, int level) {
    public SkyBlockEnchantment(UnderstandableSkyBlockEnchantment enchantment) {
        this(EnchantmentType.valueOf(enchantment.getEnchantmentType()), enchantment.getLevel());
    }

    public record ItemEnchantments(List<SkyBlockEnchantment> enchantments) {
        public ItemEnchantments(UnderstandableSkyBlockEnchantment.ItemEnchantments enchantments) {
            this(enchantments.enchantments().stream().map(SkyBlockEnchantment::new).toList());
        }

        public void addEnchantment(SkyBlockEnchantment enchantment) {
            enchantments.add(enchantment);
        }

        public UnderstandableSkyBlockEnchantment.ItemEnchantments toUnderstandable() {
            UnderstandableSkyBlockEnchantment.ItemEnchantments itemEnchantments = new UnderstandableSkyBlockEnchantment.ItemEnchantments(new ArrayList<>());
            enchantments.forEach((enchantment) -> itemEnchantments.addEnchantment(enchantment.toUnderstandable()));
            return itemEnchantments;
        }
    }

    public UnderstandableSkyBlockEnchantment toUnderstandable() {
        return new UnderstandableSkyBlockEnchantment(type.name(), level);
    }
}
