package net.swofty.type.skyblockgeneric.collection;

import lombok.Getter;
import net.swofty.commons.skyblock.item.ItemType;

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
    LOOTING_DISCOUNT("§9Looting §7Exp Discount §a(-25%)"),
    SHARPNESS_DISCOUNT("§9Sharpness §7Exp Discount §a(-25%)"),
    SMITE_DISCOUNT("§9Smite §7Exp Discount §a(-25%)"),
    ENDER_SLAYER_DISCOUNT("§9Ender Slayer §7Exp Discount §a(-25%)"),
    GIANT_KILLER_DISCOUNT("§9Giant Killer §7Exp Discount §a(-25%)"),
    EXECUTE_DISCOUNT("§9Execute §7Exp Discount §a(-25%)"),
    IMPALING_DISCOUNT("§9Impaling §7Exp Discount §a(-25%)"),
    BANE_OF_ARTHROPODS_DISCOUNT("§9Bane of Arthropods §7Exp Discount §a(-25%)"),
    CUBISM_DISCOUNT("§9Cubism §7Exp Discount §a(-25%)"),
    FORTUNE_DISCOUNT("§9Fortune §7Exp Discount §a(-25%)"),
    CLEAVE_DISCOUNT("§9Cleave §7Exp Discount §a(-25%)"),
    LIFE_STEAL_DISCOUNT("§9Life Steal §7Exp Discount §a(-25%)"),
    PROSECUTE_DISCOUNT("§9Prosecute §7Exp Discount §a(-25%)"),
    THUNDERBOLT_DISCOUNT("§9Thunderbolt §7Exp Discount §a(-25%)"),
    EXPERIENCE_DISCOUNT("§9Experience §7Exp Discount §a(-25%)"),
    FIRE_ASPECT_DISCOUNT("§9Fire Aspect §7Exp Discount §a(-25%)"),
    KNOCKBACK_DISCOUNT("§9Knockback §7Exp Discount §a(-25%)"),
    LETHALITY_DISCOUNT("§9Lethality §7Exp Discount §a(-25%)"),
    THUNDERLORD_DISCOUNT("§9Thunderlord §7Exp Discount §a(-25%)"),
    VAMPIRISM_DISCOUNT("§9Vampirism §7Exp Discount §a(-25%)"),
    VENOMOUS_DISCOUNT("§9Venomous §7Exp Discount §a(-25%)"),

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
