package net.swofty.types.generic.item.set;

import lombok.Getter;
import net.swofty.types.generic.item.ItemType;
import net.swofty.types.generic.item.set.impl.ArmorSet;
import net.swofty.types.generic.item.set.sets.*;
import net.swofty.types.generic.utility.StringUtility;

@Getter
public enum ArmorSetRegistry {
    LEAFLET(LeafletSet.class, ItemType.LEAFLET_BOOTS, ItemType.LEAFLET_LEGGINGS, ItemType.LEAFLET_CHESTPLATE, ItemType.LEAFLET_HELMET),
    MINER_OUTFIT(MinerOutfitSet.class, ItemType.MINERS_OUTFIT_BOOTS, ItemType.MINERS_OUTFIT_LEGGINGS, ItemType.MINERS_OUTFIT_CHESTPLATE, ItemType.MINERS_OUTFIT_HELMET),
    CHEAP_TUXEDO(CheapTuxedoSet.class, ItemType.CHEAP_TUXEDO_BOOTS, ItemType.CHEAP_TUXEDO_LEGGINGS, ItemType.CHEAP_TUXEDO_CHESTPLATE, null),
    FANCY_TUXEDO(FancyTuxedoSet.class, ItemType.FANCY_TUXEDO_BOOTS, ItemType.FANCY_TUXEDO_LEGGINGS, ItemType.FANCY_TUXEDO_CHESTPLATE, null),
    ELEGANT_TUXEDO(ElegantTuxedoSet.class, ItemType.ELEGANT_TUXEDO_BOOTS, ItemType.ELEGANT_TUXEDO_LEGGINGS, ItemType.ELEGANT_TUXEDO_CHESTPLATE, null),
    MUSHROOM(MushroomSet.class, ItemType.MUSHROOM_BOOTS, ItemType.MUSHROOM_LEGGINGS, ItemType.MUSHROOM_CHESTPLATE, ItemType.MUSHROOM_HELMET),
    PUMPKIN(PumpkinSet.class, ItemType.PUMPKIN_BOOTS, ItemType.PUMPKIN_LEGGINGS, ItemType.PUMPKIN_CHESTPLATE, ItemType.PUMPKIN_HELMET);

    private final Class<? extends ArmorSet> clazz;
    private final ItemType boots;
    private final ItemType leggings;
    private final ItemType chestplate;
    private final ItemType helmet;

    ArmorSetRegistry(Class<? extends ArmorSet> clazz, ItemType boots, ItemType legging,
                     ItemType chestplate, ItemType helmet) {
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

    public static ArmorSetRegistry getArmorSet(ItemType item) {
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

    public static ArmorSetRegistry getArmorSet(ItemType boots, ItemType leggings, ItemType chestplate, ItemType helmet) {
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
