package net.swofty.type.ravengardgeneric.region;

import lombok.Getter;

@Getter
public enum RavengardRegionType {
    NEVERMORE("The Nevermore", "§b"),
    RAVENPORT("Ravenport", "§e");

    private final String displayName;
    private final String color;

    RavengardRegionType(String displayName) {
        this(displayName, "§f");
    }

    RavengardRegionType(String displayName, String color) {
        this.displayName = displayName;
        this.color = color;
    }

    public static RavengardRegionType fromKey(String key) {
        if (key == null) {
            return null;
        }
        for (RavengardRegionType value : values()) {
            if (value.name().equalsIgnoreCase(key)) {
                return value;
            }
        }
        return null;
    }
}
