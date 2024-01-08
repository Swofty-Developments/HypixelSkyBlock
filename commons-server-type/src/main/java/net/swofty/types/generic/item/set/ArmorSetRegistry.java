package net.swofty.types.generic.item.set;

import lombok.Getter;
import net.swofty.types.generic.item.ItemType;
import net.swofty.types.generic.item.set.impl.ArmorSet;
import net.swofty.types.generic.item.set.sets.LeafletSet;

@Getter
public enum ArmorSetRegistry {
    LEAFLET(LeafletSet.class, ItemType.LEAFLET_SANDALS, ItemType.LEAFLET_PANTS, ItemType.LEAFLET_TUNIC, ItemType.LEAFLET_HAT),
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
