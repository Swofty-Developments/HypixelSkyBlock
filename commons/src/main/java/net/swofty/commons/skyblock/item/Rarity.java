package net.swofty.commons.skyblock.item;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Rarity {
    COMMON("§f"),
    UNCOMMON("§a"),
    RARE("§9"),
    EPIC("§5"),
    LEGENDARY("§6"),
    MYTHIC("§d"),
    DIVINE("§b"),
    SPECIAL("§c", false),
    VERY_SPECIAL("§c", false),
    ADMIN("§4", false),
    ;

    private final String color;
    private final boolean reforgable;

    Rarity(String color) {
        this(color, true);
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