package net.swofty.types.generic.item.impl;

import lombok.Getter;
import net.swofty.types.generic.item.ReforgeType;
import net.swofty.types.generic.utility.groups.EnchantItemGroups;

import java.util.List;

public interface StandardItem extends ExtraRarityDisplay, Reforgable, Enchantable {
    @Override
    default String getExtraRarityDisplay() {
        return " " + getStandardItemType().getRarityDisplay();
    }

    @Override
    default boolean showEnchantLores() {
        return true;
    }

    @Override
    default List<EnchantItemGroups> getEnchantItemGroups() {
        return getStandardItemType().getEnchantItemGroups();
    }

    @Override
    default ReforgeType getReforgeType() {
        return getStandardItemType().getReforgeType();
    }

    StandardItemType getStandardItemType();

    @Getter
    enum StandardItemType {
        SWORD("SWORD", ReforgeType.SWORDS, List.of(EnchantItemGroups.SWORD)),
        PICKAXE("PICKAXE", ReforgeType.PICKAXES, List.of(EnchantItemGroups.TOOLS)),
        HOE("HOE", ReforgeType.HOES, List.of(EnchantItemGroups.TOOLS)),
        HELMET("HELMET", ReforgeType.ARMOR, List.of(EnchantItemGroups.ARMOR)),
        CHESTPLATE("CHESTPLATE", ReforgeType.ARMOR, List.of(EnchantItemGroups.ARMOR)),
        LEGGINGS("LEGGINGS", ReforgeType.ARMOR, List.of(EnchantItemGroups.ARMOR)),
        BOOTS("BOOTS", ReforgeType.ARMOR, List.of(EnchantItemGroups.ARMOR)),
        ;

        private final String rarityDisplay;
        private final ReforgeType reforgeType;
        private final List<EnchantItemGroups> enchantItemGroups;

        StandardItemType(String rarityDisplay, ReforgeType reforgeType, List<EnchantItemGroups> enchantItemGroups) {
            this.rarityDisplay = rarityDisplay;
            this.reforgeType = reforgeType;
            this.enchantItemGroups = enchantItemGroups;
        }
    }
}
