package net.swofty.type.generic.user.categories;

import lombok.Getter;
import net.kyori.adventure.text.format.NamedTextColor;
import net.swofty.commons.StringUtility;

@Getter
public enum Rank {
    STAFF("§c[§6ዞ§c] ", true, NamedTextColor.RED),
    YOUTUBE("§c[§fYOUTUBE§c] ", false, NamedTextColor.RED),
    MVP_PLUS("§b[MVP§c+§b] ", false, NamedTextColor.AQUA),
    MVP("§b[MVP] ", false, NamedTextColor.AQUA),
    VIP_PLUS("§a[VIP§6+§a] ", false, NamedTextColor.GREEN),
    VIP("§a[VIP] ", false, NamedTextColor.GREEN),
    DEFAULT("§7", false, NamedTextColor.GRAY),
    ;

    private final String prefix;
    private final boolean isStaff;
    private final NamedTextColor textColor;

    Rank(String prefix, boolean isStaff, NamedTextColor textColor) {
        this.prefix = prefix;
        this.isStaff = isStaff;
        this.textColor = textColor;

    }

    public boolean isEqualOrHigherThan(Rank rank) {
        return this.ordinal() <= rank.ordinal();
    }

    public char getPriorityCharacter() {
        return StringUtility.ALPHABET[ordinal()];
    }
}
