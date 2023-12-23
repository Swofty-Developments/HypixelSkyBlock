package net.swofty.item.attribute;

import net.swofty.enchantment.SkyBlockEnchantment;
import net.swofty.item.Rarity;
import net.swofty.item.SkyBlockItem;
import net.swofty.item.attribute.attributes.*;
import net.swofty.user.statistics.ItemStatistics;

import java.util.stream.Stream;

public class AttributeHandler {
    SkyBlockItem item;

    public AttributeHandler(SkyBlockItem item) {
        this.item = item;
    }

    public String getItemType() {
        return ((ItemAttributeType) item.getAttribute("item_type")).getValue();
    }

    public Rarity getRarity() {
        return ((ItemAttributeRarity) item.getAttribute("rarity")).getValue();
    }

    public void setRarity(Rarity rarity) {
        ((ItemAttributeRarity) item.getAttribute("rarity")).setValue(rarity);
    }

    public boolean hasEnchantment(SkyBlockEnchantment.EnchantmentType type) {
        return ((ItemAttributeEnchantments) item.getAttribute("enchantments")).getValue()
                .enchantments()
                .stream()
                .anyMatch(enchantment -> enchantment.getType() == type);
    }

    public SkyBlockEnchantment getEnchantment(SkyBlockEnchantment.EnchantmentType type) {
        return ((ItemAttributeEnchantments) item.getAttribute("enchantments")).getValue()
                .enchantments()
                .stream()
                .filter(enchantment -> enchantment.getType() == type)
                .findFirst()
                .orElse(null);
    }

    public Stream<SkyBlockEnchantment> getEnchantments() {
        return ((ItemAttributeEnchantments) item.getAttribute("enchantments")).getValue().enchantments().stream();
    }

    public void addEnchantment(SkyBlockEnchantment enchantment) {
        ((ItemAttributeEnchantments) item.getAttribute("enchantments")).getValue().addEnchantment(enchantment);
    }

    public ItemStatistics getStatistics() {
        return ((ItemAttributeStatistics) item.getAttribute("statistics")).getValue();
    }

    public void setStatistics(ItemStatistics statistics) {
        ((ItemAttributeStatistics) item.getAttribute("statistics")).setValue(statistics);
    }

    public void setRecombobulated(boolean value) {
        ((ItemAttributeRecombobulated) item.getAttribute("recombobulated")).setValue(value);
    }

    public boolean isRecombobulated() {
        return ((ItemAttributeRecombobulated) item.getAttribute("recombobulated")).getValue();
    }

    public int getBreakingPower() {
        return ((ItemAttributeBreakingPower) item.getAttribute("breaking_power")).getValue();
    }

    public boolean isMiningTool() {
        return getBreakingPower() != 0;
    }

    public SkyBlockItem asSkyBlockItem() {
        return item;
    }
}
