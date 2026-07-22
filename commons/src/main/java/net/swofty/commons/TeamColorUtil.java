package net.swofty.commons;

import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.color.TeamColor;

public class TeamColorUtil {
    public static TeamColor fromNamedColor(NamedTextColor color) {
        return TeamColor.fromName(color.name());
    }
}
