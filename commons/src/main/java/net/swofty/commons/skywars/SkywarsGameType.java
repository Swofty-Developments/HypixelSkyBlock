package net.swofty.commons.skywars;

import lombok.Getter;
import org.jetbrains.annotations.Nullable;

@Getter
public enum SkywarsGameType {
    SOLO_NORMAL(0, "Solo Normal", 1, 12, false),
    SOLO_INSANE(1, "Solo Insane", 1, 12, true),
    DOUBLES_NORMAL(2, "Doubles", 2, 12, false),
    SOLO_LUCKY_BLOCK(3, "Lucky Block", 1, 12, false),
    ;

    private final int id;
    private final String displayName;
    private final int teamSize;
    private final int maxTeams;
    private final boolean insane;

    SkywarsGameType(int id, String displayName, int teamSize, int maxTeams, boolean insane) {
        this.id = id;
        this.displayName = displayName;
        this.teamSize = teamSize;
        this.maxTeams = maxTeams;
        this.insane = insane;
    }

    public int maxPlayers() {
        return teamSize * maxTeams;
    }

    public int getMaxPlayers() {
        return maxPlayers();
    }

    public int getMinPlayers() {
        // Minimum is 2 players for solo, or 2 teams for team modes
        return teamSize == 1 ? 2 : teamSize * 2;
    }

    public boolean isSolo() {
        return teamSize == 1;
    }

    /**
     * Returns the mode string for this game type.
     * @return "INSANE" for insane modes, "NORMAL" otherwise
     */
    public String getModeString() {
        return insane ? "INSANE" : "NORMAL";
    }

    @Nullable
    public static SkywarsGameType from(String field) {
        for (SkywarsGameType type : values()) {
            if (type.name().equalsIgnoreCase(field)) {
                return type;
            }
        }
        return null;
    }

    @Nullable
    public static SkywarsGameType fromDisplayName(String displayName) {
        for (SkywarsGameType type : values()) {
            if (type.displayName.equalsIgnoreCase(displayName)) {
                return type;
            }
        }
        return null;
    }

    @Nullable
    public static SkywarsGameType fromId(int id) {
        for (SkywarsGameType type : values()) {
            if (type.id == id) {
                return type;
            }
        }
        return null;
    }
}
