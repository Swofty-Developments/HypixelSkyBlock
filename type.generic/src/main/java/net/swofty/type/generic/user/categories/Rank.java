package net.swofty.type.generic.user.categories;

import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.swofty.commons.StringUtility;

@Getter
public enum Rank {
    STAFF("§c[§6ዞ§c] ", "§6ዞ", true, NamedTextColor.RED),
    YOUTUBE("§c[§fYOUTUBE§c] ", "§fYOUTUBE", false, NamedTextColor.RED),
    MVP_PLUS_PLUS("§6[MVP++] ", "§6MVP++", false, NamedTextColor.GOLD),
    MVP_PLUS("§b[MVP§c+§b] ", "§bMVP§c+", false, NamedTextColor.AQUA),
    MVP("§b[MVP] ", "§bMVP", false, NamedTextColor.AQUA),
    VIP_PLUS("§a[VIP§6+§a] ", "§aVIP§6+", false, NamedTextColor.GREEN),
    VIP("§a[VIP] ", "§aVIP", false, NamedTextColor.GREEN),
    DEFAULT("§7", "§7Default", false, NamedTextColor.GRAY),
    ;

    private final String prefix;
    private final String title;
    private final boolean isStaff;
    private final NamedTextColor textColor;

    Rank(String prefix, String title, boolean isStaff, NamedTextColor textColor) {
        this.prefix = prefix;
        this.title = title;
        this.isStaff = isStaff;
        this.textColor = textColor;

    }

    public boolean isEqualOrHigherThan(Rank rank) {
        return this.ordinal() <= rank.ordinal();
    }

    public char getPriorityCharacter() {
        return StringUtility.ALPHABET[ordinal()];
    }

    public Component getTitleComponent() {
        return LegacyComponentSerializer.legacySection().deserialize(title);
    }
}
