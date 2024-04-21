package net.swofty.types.generic.item.set;

import lombok.Getter;
import net.swofty.types.generic.item.ItemType;
import net.swofty.types.generic.item.set.impl.ArmorSet;
import net.swofty.types.generic.item.set.sets.LeafletSet;
import net.swofty.types.generic.item.set.sets.MinerOutfitSet;
import net.swofty.types.generic.item.set.sets.StrongDragonSet;
import net.swofty.types.generic.item.set.sets.YoungDragonSet;
import net.swofty.types.generic.item.set.sets.*;

@Getter
public enum ArmorSetRegistry {
    LEAFLET(LeafletSet.class, ItemType.LEAFLET_SANDALS, ItemType.LEAFLET_PANTS, ItemType.LEAFLET_TUNIC, ItemType.LEAFLET_HAT),
    STRONG_DRAGON(StrongDragonSet.class, ItemType.STRONG_DRAGON_BOOTS, ItemType.STRONG_DRAGON_LEGGINGS, ItemType.STRONG_DRAGON_CHESTPLATE, ItemType.STRONG_DRAGON_HELMET),
    YOUNG_DRAGON(YoungDragonSet.class, ItemType.YOUNG_DRAGON_BOOTS, ItemType.YOUNG_DRAGON_LEGGINGS, ItemType.YOUNG_DRAGON_CHESTPLATE, ItemType.YOUNG_DRAGON_HELMET),
    MINOR_OUTFIT(MinerOutfitSet.class, ItemType.MINERS_OUTFIT_BOOTS, ItemType.MINERS_OUTFIT_LEGGINGS, ItemType.MINERS_OUTFIT_CHESTPLATE, ItemType.MINERS_OUTFIT_HELMET),
    CHEAP_TUXEDO(CheapTuxedoSet.class, ItemType.CHEAP_TUXEDO_OXFORDS, ItemType.CHEAP_TUXEDO_PANTS, ItemType.CHEAP_TUXEDO_JACKET, null),
    FANCY_TUXEDO(FancyTuxedoSet.class, ItemType.FANCY_TUXEDO_OXFORDS, ItemType.FANCY_TUXEDO_PANTS, ItemType.FANCY_TUXEDO_JACKET, null),
    ELEGANT_TUXEDO(ElegantTuxedoSet.class, ItemType.ELEGANT_TUXEDO_OXFORDS, ItemType.ELEGANT_TUXEDO_PANTS, ItemType.ELEGANT_TUXEDO_JACKET, null)
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
