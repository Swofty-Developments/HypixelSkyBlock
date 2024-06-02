package net.swofty.types.generic.collection;

import lombok.Getter;
import net.swofty.types.generic.item.ItemType;

import java.util.HashMap;
import java.util.Map;

@Getter
public enum CustomCollectionAward {
    // ENCHANTMENTS
    SCAVENGER_DISCOUNT("§9Scavenger §7Exp Discount §a(-25%)"),
    PROTECTION_DISCOUNT("§9Protection §7Exp Discount §a(-25%)"),
    HARVESTING_DISCOUNT("§9Harvesting §7Exp Discount §a(-25%)"),
    FIRST_STRIKE_DISCOUNT("§9First Strike §7Exp Discount §a(-25%)"),
    CRITICAL_DISCOUNT("§9Critical §7Exp Discount §a(-25%)"),
    EFFICIENCY_DISCOUNT("§9Efficiency §7Exp Discount §a(-25%)"),
    GROWTH_DISCOUNT("§9Growth §7Exp Discount §a(-25%)"),
    LUCK_DISCOUNT("§9Luck §7Exp Discount §a(-25%)"),
    SHARPNESS_DISCOUNT("§9Sharpness §7Exp Discount §a(-25%)"),
    SMITE_DISCOUNT("§9Smite §7Exp Discount §a(-25%)"),
    ENDER_SLAYER_DISCOUNT("§9Ender Slayer §7Exp Discount §a(-25%)"),

    // BAGS
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
    FISHING_BAG("§aSmall Fishing Bag"),
    FISHING_BAG_UPGRADE_1("§aMedium Fishing Bag §7(+9 slots)"),
    FISHING_BAG_UPGRADE_2("§aLarge Fishing Bag §7(+9 slots)"),
    FISHING_BAG_UPGRADE_3("§aGiant Fishing Bag §7(+9 slots)"),
    FISHING_BAG_UPGRADE_4("§aMassive Fishing Bag §7(+9 slots)"),
    POTION_BAG("§aSmall Potion Bag"),
    POTION_BAG_UPGRADE_1("§aMedium Potion Bag §7(+9 slots)"),
    POTION_BAG_UPGRADE_2("§aLarge Potion Bag §7(+9 slots)"),
    POTION_BAG_UPGRADE_3("§aGiant Potion Bag §7(+9 slots)"),
    POTION_BAG_UPGRADE_4("§aMassive Potion Bag §7(+9 slots)"),
    SACK_OF_SACKS("§aSmall Sack of Sacks"),
    SACK_OF_SACKS_UPGRADE_1("§aMedium Sack of Sacks Upgrade §7(+3 slots)"),
    SACK_OF_SACKS_UPGRADE_2("§aLarge Sack of Sacks Upgrade §7(+3 slots)"),
    SACK_OF_SACKS_UPGRADE_3("§aGreater Sack of Sacks Upgrade §7(+3 slots)"),
    SACK_OF_SACKS_UPGRADE_4("§aGiant Sack of Sacks Upgrade §7(+3 slots)"),
    SACK_OF_SACKS_UPGRADE_5("§aMassive Sack of Sacks Upgrade §7(+3 slots)"),
    ;

    public static final Map<CustomCollectionAward, Map.Entry<ItemType, Integer>> AWARD_CACHE = new HashMap<>();

    private final String display;

    CustomCollectionAward(String display) {
        this.display = display;
    }
}
