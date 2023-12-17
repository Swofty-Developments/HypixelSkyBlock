package net.swofty.user.categories;

import lombok.Getter;
import net.kyori.adventure.text.format.NamedTextColor;
import net.swofty.Utility;

@Getter
public enum Rank {
    OWNER("§c[OWNER] ", true, NamedTextColor.RED),
    ADMIN("§c[ADMIN] ", true, NamedTextColor.RED),
    DEVELOPER("§d[DEV] ", true, NamedTextColor.LIGHT_PURPLE),
    MOD("§2[MOD] ", true, NamedTextColor.DARK_GREEN),
    HELPER("§9[HELPER] ", true, NamedTextColor.BLUE),
    YOUTUBE("§c[§fYOUTUBE§c] ", false, NamedTextColor.RED),
    DEFAULT("§7", false, NamedTextColor.GRAY),
    ;

    private String prefix;
    private boolean isStaff;
    private NamedTextColor textColor;

    Rank(String prefix, boolean isStaff, NamedTextColor textColor) {
        this.prefix = prefix;
        this.isStaff = isStaff;
        this.textColor = textColor;
    }

    public boolean isEqualOrHigherThan(Rank rank) {
        return this.ordinal() <= rank.ordinal();
    }

    public char ordinalToChar() {
        return Utility.ALPHABET[ordinal()];
    }
}
