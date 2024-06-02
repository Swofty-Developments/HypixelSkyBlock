package net.swofty.types.generic.item.impl;

import lombok.NonNull;
import net.swofty.types.generic.item.ItemType;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.utility.StringUtility;

import java.util.List;

public interface RuneItem extends ExtraRarityDisplay, ExtraUnderNameDisplays, SkullHead, CustomDisplayName,
        TrackedUniqueItem {
    int getRequiredRuneLevel();
    @NonNull String getColor();
    RuneApplicableTo getRuneApplicableTo();

    default String getDisplayName(SkyBlockItem item) {
        ItemType type = item.getAttributeHandler().getItemTypeAsType();
        return getColor() + "◆ " + StringUtility.toNormalCase(type.toString())
                + " " +
                StringUtility.getAsRomanNumeral(item.getAttributeHandler().getRuneLevel());
    }

    default String getDisplayName(ItemType type, int runeLevel) {
        return getColor() + "◆ " + StringUtility.toNormalCase(type.toString())
                + " " +
                StringUtility.getAsRomanNumeral(runeLevel);
    }

    default List<String> getExtraUnderNameDisplays() {
        return List.of("§8Requires level " + getRequiredRuneLevel(), StringUtility.toNormalCase(getRuneApplicableTo().name()));
    }

    default List<String> defaultRuneLores() {
        return List.of("§7Apply this rune to weapons or", "§7fuse two together at the Runic", "§7Pedestal!");
    }

    default String getExtraRarityDisplay() {
        return " COSMETIC";
    }

    enum RuneApplicableTo {
        WEAPONS,
        BOWS,
        HELMETS,
        CHESTPLATES,
        LEGGINGS,
        BOOTS,
        HOES,
    }
}
