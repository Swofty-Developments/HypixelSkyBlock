package net.swofty.types.generic.collection;

import lombok.Getter;
import net.swofty.types.generic.item.ItemType;

import java.util.HashMap;
import java.util.Map;

@Getter
public enum CustomCollectionAward {
    QUIVER("§aQuiver"),
    QUIVER_UPGRADE_1("§aLarge Quiver Update §7(+9 slots)"),
    QUIVER_UPGRADE_2("§aGiant Quiver Update §7(+9 slots)"),
    ACCESSORY_BAG("§aSmall Accessory Bag"),
    ACCESSORY_BAG_UPGRADE_1("§aMedium Accessory Bag Upgrade §7(+6 slots)"),
    ACCESSORY_BAG_UPGRADE_2("§aLarge Accessory Bag Upgrade §7(+6 slots)"),
    ACCESSORY_BAG_UPGRADE_3("§aGreater Accessory Bag Upgrade §7(+6 slots)"),
    ACCESSORY_BAG_UPGRADE_4("§aGiant Accessory Bag Upgrade §7(+6 slots)"),
    ACCESSORY_BAG_UPGRADE_5("§aMassive Accessory Bag Upgrade §7(+6 slots)"),
    ACCESSORY_BAG_UPGRADE_6("§aHumongous Accessory Bag Upgrade §7(+6 slots)"),
    ACCESSORY_BAG_UPGRADE_7("§aColossal Accessory Bag Upgrade §7(+6 slots)"),
    ACCESSORY_BAG_UPGRADE_8("§aTitanic Accessory Bag Upgrade §7(+6 slots)"),
    ACCESSORY_BAG_UPGRADE_9("§aPreposterous Accessory Bag Upgrade §7(+6 slots)"),
    ;

    public static final Map<CustomCollectionAward, Map.Entry<ItemType, Integer>> AWARD_CACHE = new HashMap<>();

    private final String display;

    CustomCollectionAward(String display) {
        this.display = display;
    }
}
