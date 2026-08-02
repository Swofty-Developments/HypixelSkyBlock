package net.swofty.type.ravengardgeneric.item;

import lombok.Getter;
import lombok.Setter;
import net.minestom.server.item.Material;
import net.swofty.type.ravengardgeneric.classes.RavengardClass;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
public class RavengardItemType {
    private final String id;
    private Material material;
    private RavengardRarity rarity = RavengardRarity.COMMON;
    private String itemModel;
    private String displayName;
    private final Map<String, Double> statistics = new HashMap<>();
    private final List<RavengardItemComponent> components = new ArrayList<>();

    /** Set by the CLASS_RESTRICTED component; null means any class may hold the item. */
    private RavengardClass restrictedTo;

    public RavengardItemType(String id) {
        this.id = id;
    }

    public double statistic(String key) {
        return statistics.getOrDefault(key, 0.0);
    }

    public boolean usableBy(RavengardClass value) {
        return restrictedTo == null || restrictedTo == value;
    }

    public <T extends RavengardItemComponent> T component(Class<T> type) {
        for (RavengardItemComponent component : components) {
            if (type.isInstance(component)) {
                return type.cast(component);
            }
        }
        return null;
    }
}
