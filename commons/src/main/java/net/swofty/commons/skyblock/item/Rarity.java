package net.swofty.commons.skyblock.item;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Rarity {
    COMMON("§f", "hypixel_skyblock:common"),
    UNCOMMON("§a", "hypixel_skyblock:uncommon"),
    RARE("§9", "hypixel_skyblock:rare"),
    EPIC("§5", "hypixel_skyblock:epic"),
    LEGENDARY("§6", "hypixel_skyblock:legendary"),
    MYTHIC("§d", "hypixel_skyblock:mythic"),
    DIVINE("§b", "hypixel_skyblock:divine"),
    SPECIAL("§c", "hypixel_skyblock:special", false),
    VERY_SPECIAL("§c", "hypixel_skyblock:very_special", false),
    ADMIN("§4", "hypixel_skyblock:admin", false),
    ;

    private final String color;
    private final String tooltipStyle;
    private final boolean reforgable;

    Rarity(String color, String tooltipStyle) {
        this(color, tooltipStyle, true);
    }

    public Rarity upgrade() {
        return values()[Math.min(this.ordinal() + 1, values().length - 1)];
    }

    public Rarity downgrade() {
        if (this.ordinal() - 1 < 0)
            return this;
        return values()[this.ordinal() - 1];
    }

    public boolean isAtLeast(Rarity rarity) {
        return ordinal() >= rarity.ordinal();
    }

    public String getDisplay() {
        return color + "§l" + name().replaceAll("_", " ");
    }

    public String getDisplayCapitalized() {
        return name().charAt(0) + name().toLowerCase().replaceAll("_", " ").substring(1);
    }

    public String getBoldedColor() {
        return color + "§l";
    }

    public static Rarity getRarity(String string) {
        try {
            return Rarity.valueOf(string.toUpperCase());
        } catch (IllegalArgumentException ex) {
            return null;
        }
    }

}