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
    ;

    public static final Map<CustomCollectionAward, Map.Entry<ItemType, Integer>> AWARD_CACHE = new HashMap<>();

    private final String display;

    CustomCollectionAward(String display) {
        this.display = display;
    }
}
