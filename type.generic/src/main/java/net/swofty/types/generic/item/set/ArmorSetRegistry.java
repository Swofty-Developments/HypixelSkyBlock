package net.swofty.types.generic.item.set;

import lombok.Getter;
import net.swofty.types.generic.item.ItemTypeLinker;
import net.swofty.types.generic.item.set.impl.ArmorSet;
import net.swofty.types.generic.item.set.sets.*;
import net.swofty.commons.StringUtility;

@Getter
public enum ArmorSetRegistry {
    LEAFLET(LeafletSet.class, ItemTypeLinker.LEAFLET_BOOTS, ItemTypeLinker.LEAFLET_LEGGINGS, ItemTypeLinker.LEAFLET_CHESTPLATE, ItemTypeLinker.LEAFLET_HELMET),
    MINER_OUTFIT(MinerOutfitSet.class, ItemTypeLinker.MINERS_OUTFIT_BOOTS, ItemTypeLinker.MINERS_OUTFIT_LEGGINGS, ItemTypeLinker.MINERS_OUTFIT_CHESTPLATE, ItemTypeLinker.MINERS_OUTFIT_HELMET),
    CHEAP_TUXEDO(CheapTuxedoSet.class, ItemTypeLinker.CHEAP_TUXEDO_BOOTS, ItemTypeLinker.CHEAP_TUXEDO_LEGGINGS, ItemTypeLinker.CHEAP_TUXEDO_CHESTPLATE, null),
    FANCY_TUXEDO(FancyTuxedoSet.class, ItemTypeLinker.FANCY_TUXEDO_BOOTS, ItemTypeLinker.FANCY_TUXEDO_LEGGINGS, ItemTypeLinker.FANCY_TUXEDO_CHESTPLATE, null),
    ELEGANT_TUXEDO(ElegantTuxedoSet.class, ItemTypeLinker.ELEGANT_TUXEDO_BOOTS, ItemTypeLinker.ELEGANT_TUXEDO_LEGGINGS, ItemTypeLinker.ELEGANT_TUXEDO_CHESTPLATE, null),
    MUSHROOM(MushroomSet.class, ItemTypeLinker.MUSHROOM_BOOTS, ItemTypeLinker.MUSHROOM_LEGGINGS, ItemTypeLinker.MUSHROOM_CHESTPLATE, ItemTypeLinker.MUSHROOM_HELMET),
    PUMPKIN(PumpkinSet.class, ItemTypeLinker.PUMPKIN_BOOTS, ItemTypeLinker.PUMPKIN_LEGGINGS, ItemTypeLinker.PUMPKIN_CHESTPLATE, ItemTypeLinker.PUMPKIN_HELMET);

    private final Class<? extends ArmorSet> clazz;
    private final ItemTypeLinker boots;
    private final ItemTypeLinker leggings;
    private final ItemTypeLinker chestplate;
    private final ItemTypeLinker helmet;

    ArmorSetRegistry(Class<? extends ArmorSet> clazz, ItemTypeLinker boots, ItemTypeLinker legging,
                     ItemTypeLinker chestplate, ItemTypeLinker helmet) {
        this.clazz = clazz;
        this.boots = boots;
        this.leggings = legging;
        this.chestplate = chestplate;
        this.helmet = helmet;
    }

    public static ArmorSetRegistry getArmorSet(Class<? extends ArmorSet> clazz) {
        for (ArmorSetRegistry armorSetRegistry : values()) {
            if (armorSetRegistry.getClazz() == clazz) {
                return armorSetRegistry;
            }
        }
        return null;
    }

    public static ArmorSetRegistry getArmorSet(ItemTypeLinker item) {
        for (ArmorSetRegistry armorSetRegistry : values()) {
            if (armorSetRegistry.getBoots() == item
                    || armorSetRegistry.getLeggings() == item
                    || armorSetRegistry.getChestplate() == item
                    || armorSetRegistry.getHelmet() == item) {
                return armorSetRegistry;
            }
        }
        return null;
    }

    public static int getPieceCount(ArmorSetRegistry armorSetRegistry) {
        int count = 0;
        if (armorSetRegistry.getBoots() != null) count++;
        if (armorSetRegistry.getLeggings() != null) count++;
        if (armorSetRegistry.getChestplate() != null) count++;
        if (armorSetRegistry.getHelmet() != null) count++;
        return count;
    }

    public static ArmorSetRegistry getArmorSet(ItemTypeLinker boots, ItemTypeLinker leggings, ItemTypeLinker chestplate, ItemTypeLinker helmet) {
        for (ArmorSetRegistry armorSetRegistry : values()) {
            if ((armorSetRegistry.getBoots() == null || armorSetRegistry.getBoots() == boots)
                    && (armorSetRegistry.getLeggings() == null || armorSetRegistry.getLeggings() == leggings)
                    && (armorSetRegistry.getChestplate() == null || armorSetRegistry.getChestplate() == chestplate)
                    && (armorSetRegistry.getHelmet() == null || armorSetRegistry.getHelmet() == helmet)) {
                return armorSetRegistry;
            }
        }
        return null;
    }

    public String getDisplayName() {
        return StringUtility.toNormalCase(name().replace("_", " "));
    }
}
