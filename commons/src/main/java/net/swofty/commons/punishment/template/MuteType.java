package net.swofty.commons.punishment.template;

import lombok.Getter;
import java.util.List;

@Getter
public enum MuteType {
    NEGATIVE_REFERENCE("Discussing important people or world events in a negative way.", "NR", 4),
    USER_DISRESPECT("Acting in a manner that is disrespectful to members within the community.", "UD", 4),
    STAFF_DISRESPECT("Disrespectful behaviour directed at staff members.", "SD", 4),
    INAPPROPRIATE_CONTENT_LVL1("Using adult concepts in public chat on the server.", "IC1", 3),
    DISCRIMINATION("Discrimination of a player or group of people.", "DI", 3),
    EXCESSIVE_SWEARING("Excessive use of swearing in chat.", "ES", 2),
    UN_INTENTIONALLY_CAUSING_DISTRESS("Unintentionally/Intentionally Causing distress.", "UI", 2),
    ENCOURAGING_CHEATING_LVL1("Discussing or actively promoting cheating or breaking of rules on the server.", "EC1", 2),
    MEDIA_ADVERTISING("Media Advertising", "MA", 1),
    PUBLIC_SHAMING("Publicly revealing information about a player.", "PS", 1),
    RUDE("Being rude or inappropriate.", "RU", 1),
    EXCESSIVE_SPAMMING("Repeatedly posting unnecessary messages or content.", "SP", 1),
    MISLEADING_INFORMATION("Misleading other players to carry out actions that disrupts their game.", "MI", 1, List.of("Trolling")),
    UNNECESSARY_SPOILERS("Giving spoilers, revealing important storylines of popular movies and tv shows.", "US", 1),
    ESCALATION("You have been muted for a chat offense and is currently under review.", "ESC", 0);

    private final String reason;
    private final String shortName;
    private final int weight;
    private final List<String> aliases;

    MuteType(String reason, String shortName, int weight) {
        this(reason, shortName, weight, null);
    }

    MuteType(String reason, String shortName, int weight, List<String> aliases) {
        this.reason = reason;
        this.shortName = shortName;
        this.weight = weight;
        this.aliases = aliases;
    }
}