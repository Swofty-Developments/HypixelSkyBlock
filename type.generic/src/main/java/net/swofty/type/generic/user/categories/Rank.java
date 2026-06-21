package net.swofty.type.generic.user.categories;

import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.swofty.commons.StringUtility;

@Getter
public enum Rank {
    STAFF("ዞ", true, NamedTextColor.RED),
    YOUTUBE("YOUTUBE", false, NamedTextColor.RED),
    MVP_PLUS_PLUS("MVP++", false, NamedTextColor.GOLD),
    MVP_PLUS("MVP+", false, NamedTextColor.AQUA),
    MVP("MVP", false, NamedTextColor.AQUA),
    VIP_PLUS("VIP+", false, NamedTextColor.GREEN),
    VIP("VIP", false, NamedTextColor.GREEN),
    DEFAULT("Default", false, NamedTextColor.GRAY),
    ;

    private final String title;
    private final boolean isStaff;
    private final NamedTextColor textColor;

    Rank(String title, boolean isStaff, NamedTextColor textColor) {
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

    public Component titleComponent(RankColor plusColor, NamedTextColor mvpPlusPlusColor) {
        return switch (this) {
            case STAFF -> Component.text("ዞ", NamedTextColor.GOLD);
            case YOUTUBE -> Component.text("YOUTUBE", NamedTextColor.WHITE);
            case MVP_PLUS_PLUS ->
                Component.text("MVP", mvpPlusPlusColor).append(Component.text("++", plusColor.getColor()));
            case MVP_PLUS ->
                Component.text("MVP", NamedTextColor.AQUA).append(Component.text("+", plusColor.getColor()));
            case MVP -> Component.text("MVP", NamedTextColor.AQUA);
            case VIP_PLUS ->
                Component.text("VIP", NamedTextColor.GREEN).append(Component.text("+", NamedTextColor.GOLD));
            case VIP -> Component.text("VIP", NamedTextColor.GREEN);
            case DEFAULT -> Component.text("Default", NamedTextColor.GRAY);
        };
    }

    public Component prefixComponent(RankColor plusColor, NamedTextColor mvpPlusPlusColor) {
        if (this == DEFAULT) return Component.empty();
        NamedTextColor bracketColor = this == MVP_PLUS || this == MVP_PLUS_PLUS ? mvpPlusPlusColor : textColor;
        return Component.text("[", bracketColor).append(titleComponent(plusColor, mvpPlusPlusColor))
            .append(Component.text("] ", bracketColor));
    }

    @Deprecated
    public String getPrefix() {
        return LegacyComponentSerializer.legacySection().serialize(prefixComponent(RankColor.RED, NamedTextColor.GOLD));
    }
}
