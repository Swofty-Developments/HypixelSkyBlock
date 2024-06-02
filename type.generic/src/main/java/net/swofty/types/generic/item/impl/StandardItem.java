package net.swofty.types.generic.item.impl;

import lombok.Getter;
import net.swofty.types.generic.item.ReforgeType;
import net.swofty.types.generic.utility.groups.EnchantItemGroups;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface StandardItem extends ExtraRarityDisplay, Reforgable, Enchantable, Runeable, HotPotatoable {
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

    @Override
    default RuneItem.RuneApplicableTo getRuneApplicableTo() {
        return getStandardItemType().getRuneApplicableTo();
    }

    @Override
    default @Nullable PotatoType getHotPotatoType() {
        return switch (getStandardItemType()) {
            case SWORD -> PotatoType.WEAPONS;
            case HELMET, CHESTPLATE, LEGGINGS, BOOTS -> PotatoType.ARMOR;
            case HOE, PICKAXE -> null;
        };
    }

    StandardItemType getStandardItemType();

    @Getter
    enum StandardItemType {
        SWORD("SWORD", ReforgeType.SWORDS, List.of(EnchantItemGroups.SWORD), RuneItem.RuneApplicableTo.WEAPONS),
        PICKAXE("PICKAXE", ReforgeType.PICKAXES, List.of(EnchantItemGroups.TOOLS), RuneItem.RuneApplicableTo.WEAPONS),
        HOE("HOE", ReforgeType.HOES, List.of(EnchantItemGroups.TOOLS), RuneItem.RuneApplicableTo.HOES),
        HELMET("HELMET", ReforgeType.ARMOR, List.of(EnchantItemGroups.ARMOR), RuneItem.RuneApplicableTo.HELMETS),
        CHESTPLATE("CHESTPLATE", ReforgeType.ARMOR, List.of(EnchantItemGroups.ARMOR), RuneItem.RuneApplicableTo.CHESTPLATES),
        LEGGINGS("LEGGINGS", ReforgeType.ARMOR, List.of(EnchantItemGroups.ARMOR), RuneItem.RuneApplicableTo.LEGGINGS),
        BOOTS("BOOTS", ReforgeType.ARMOR, List.of(EnchantItemGroups.ARMOR), RuneItem.RuneApplicableTo.BOOTS),
        ;

        private final String rarityDisplay;
        private final ReforgeType reforgeType;
        private final List<EnchantItemGroups> enchantItemGroups;
        private final RuneItem.RuneApplicableTo runeApplicableTo;

        StandardItemType(String rarityDisplay, ReforgeType reforgeType, List<EnchantItemGroups> enchantItemGroups, RuneItem.RuneApplicableTo runeApplicableTo) {
            this.rarityDisplay = rarityDisplay;
            this.reforgeType = reforgeType;
            this.enchantItemGroups = enchantItemGroups;
            this.runeApplicableTo = runeApplicableTo;
        }

        public boolean isArmor() {
            return this == HELMET || this == CHESTPLATE || this == LEGGINGS || this == BOOTS;
        }
    }
}
