package net.swofty.type.generic.utility;

import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.color.TeamColor;

public final class TeamColorUtility {

    private TeamColorUtility() {
    }

    public static TeamColor fromNamedColor(NamedTextColor color) {
        return TeamColor.fromName(color.toString());
    }
}
