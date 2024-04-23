package net.swofty.types.generic.item.set;

import lombok.Getter;
import net.swofty.types.generic.item.ItemType;
import net.swofty.types.generic.item.set.impl.ArmorSet;
import net.swofty.types.generic.item.set.sets.*;

@Getter
public enum ArmorSetRegistry {
    LEAFLET(LeafletSet.class, ItemType.LEAFLET_BOOTS, ItemType.LEAFLET_LEGGINGS, ItemType.LEAFLET_CHESTPLATE, ItemType.LEAFLET_HELMET),
    MINOR_OUTFIT(MinerOutfitSet.class, ItemType.MINERS_OUTFIT_BOOTS, ItemType.MINERS_OUTFIT_LEGGINGS, ItemType.MINERS_OUTFIT_CHESTPLATE, ItemType.MINERS_OUTFIT_HELMET),
    CHEAP_TUXEDO(CheapTuxedoSet.class, ItemType.CHEAP_TUXEDO_BOOTS, ItemType.CHEAP_TUXEDO_LEGGINGS, ItemType.CHEAP_TUXEDO_CHESTPLATE, null),
    FANCY_TUXEDO(FancyTuxedoSet.class, ItemType.FANCY_TUXEDO_BOOTS, ItemType.FANCY_TUXEDO_LEGGINGS, ItemType.FANCY_TUXEDO_CHESTPLATE, null),
    ELEGANT_TUXEDO(ElegantTuxedoSet.class, ItemType.ELEGANT_TUXEDO_BOOTS, ItemType.ELEGANT_TUXEDO_LEGGINGS, ItemType.ELEGANT_TUXEDO_CHESTPLATE, null),
    MUSHROOM(MushroomSet.class, ItemType.MUSHROOM_BOOTS, ItemType.MUSHROOM_LEGGINGS, ItemType.MUSHROOM_CHESTPLATE, ItemType.MUSHROOM_HELMET)
    ;

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

    public static ArmorSetRegistry getArmorSet(ItemType boots, ItemType leggings, ItemType chestplate, ItemType helmet) {
        for (ArmorSetRegistry armorSetRegistry : values()) {
            if (armorSetRegistry.getBoots() == boots
                    && armorSetRegistry.getLeggings() == leggings
                    && armorSetRegistry.getChestplate() == chestplate
                    && armorSetRegistry.getHelmet() == helmet) {
                return armorSetRegistry;
            }
        }
        return null;
    }
}
