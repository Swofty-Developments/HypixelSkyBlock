package net.swofty.commons.murdermystery;

import lombok.Getter;

@Getter
public enum MurderMysteryStatType {
    // General stats
    WINS("wins", "Wins"),
    KILLS("kills", "Kills"),
    GAMES_PLAYED("games_played", "Games Played"),

    // Kill types
    BOW_KILLS("bow_kills", "Bow Kills"),
    KNIFE_KILLS("knife_kills", "Knife Kills"),
    THROWN_KNIFE_KILLS("thrown_knife_kills", "Thrown Knife Kills"),
    TRAP_KILLS("trap_kills", "Trap Kills"),

    // Role-specific stats
    KILLS_AS_MURDERER("kills_as_murderer", "Kills as Murderer"),
    DETECTIVE_WINS("detective_wins", "Detective Wins"),
    MURDERER_WINS("murderer_wins", "Murderer Wins"),
    KILLS_AS_HERO("kills_as_hero", "Kills as Hero"),

    // Infection mode stats
    SURVIVOR_WINS("survivor_wins", "Survivor Wins"),
    ALPHA_WINS("alpha_wins", "Alpha Wins"),
    KILLS_AS_INFECTED("kills_as_infected", "Kills as Infected"),
    KILLS_AS_SURVIVOR("kills_as_survivor", "Kills as Survivor"),
    TIME_SURVIVED("time_survived", "Time Survived"),

    // Time records (stored as millis)
    QUICKEST_DETECTIVE_WIN("quickest_detective_win", "Quickest Detective Win"),
    QUICKEST_MURDERER_WIN("quickest_murderer_win", "Quickest Murderer Win");

    private final String key;
    private final String displayName;

    MurderMysteryStatType(String key, String displayName) {
        this.key = key;
        this.displayName = displayName;
    }

    public static MurderMysteryStatType fromKey(String key) {
        for (MurderMysteryStatType type : values()) {
            if (type.key.equalsIgnoreCase(key)) {
                return type;
            }
        }
        return null;
    }
}
