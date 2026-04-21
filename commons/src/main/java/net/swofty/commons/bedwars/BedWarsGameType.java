package net.swofty.commons.bedwars;

import lombok.Getter;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;

@Getter
public enum BedWarsGameType {
    ONE_EIGHT(0, "Solo", 1, 8, List.of(
        "Fight against 7 other players!",
        "Destroy enemy beds to stop them",
        "from respawning!",
        "Protect your bed from destruction!"
    )),
    TWO_EIGHT(1, "Doubles", 2, 8, List.of(
        "Team up with 1 other player to",
        "defeat 7 enemy teams!",
        "Destroy enemy beds to stop them",
        "from respawning!",
        "Protect your bed from destruction!"
    )),
    FOUR_THREE(2, "3v3v3v3", 3, 4, List.of(
        "Team up with 2 other players to",
        "defeat 3 other groups of players!",
        "Destroy enemy beds to stop them",
        "from respawning!",
        "Protect your bed from destruction!"
    )),
    FOUR_FOUR(3, "4v4v4v4", 4, 4, List.of(
        "Team up with 3 other players to",
        "defeat 3 other groups of players!",
        "Destroy enemy beds to stop them",
        "from respawning!",
        "Protect your bed from destruction!"
    )),
    TWO_FOUR(4, "4v4", 4, 2, List.of(
        "4v4 is the classic Bed Wars",
        "everyone knows and loves, but with",
        "only 1 enemy team!"
    )),

    // dream
    ULTIMATE_DOUBLES(5, "Ultimate V2 Doubles", 2, 8, List.of(), true),
    ULTIMATE_FOURS(6, "Ultimate V2 4v4v4v4", 4, 4, List.of(), true),
    SWAPPAGE_DOUBLES(7, "Swappage Doubles", 2, 8, List.of("Use the Swap to change ownership", "between beds! Chaos ensues."), true),
    SWAPPAGE_FOURS(8, "Swappage 4v4v4v4", 4, 4, List.of("Use the Swap to change ownership", "between beds! Chaos ensues."), true),
    RUSH_DOUBLES(9, "Rush V2", 2, 8, List.of(), true),
    RUSH_FOURS(10, "Rush V2", 4, 4, List.of(), true),
    VOIDLESS_DOUBLES(11, "Voidless", 2, 8, List.of(), true),
    VOIDLESS_FOURS(12, "Voidless", 4, 4, List.of(), true),
    ARMED_DOUBLES(12, "Armed", 2, 8, List.of(), true),
    ARMED_FOURS(12, "Armed", 4, 4, List.of(), true),
    CASTLE(9, "Castle", 40, 2, List.of(), true),
    ONE_BLOCK(20, "One Block", 1, 8, List.of(), true);

    private final int id;
    private final String displayName;
    private final int teamSize;
    private final int teams;
    private final List<String> description;
    private final boolean dream;

    BedWarsGameType(int id, String displayName, int teamSize, int teams, List<String> description) {
        this.id = id;
        this.displayName = displayName;
        this.teamSize = teamSize;
        this.teams = teams;
        this.description = description;
        this.dream = false;
    }

    BedWarsGameType(int id, String displayName, int teamSize, int teams, List<String> description, boolean dream) {
        this.id = id;
        this.displayName = displayName;
        this.teamSize = teamSize;
        this.teams = teams;
        this.description = description;
        this.dream = dream;
    }

    public int maxPlayers() {
        return teamSize * teams;
    }

    @Nullable
    public static BedWarsGameType from(String field) {
        for (BedWarsGameType type : values()) {
            if (type.name().equalsIgnoreCase(field)) {
                return type;
            }
        }
        return null;
    }

    @Nullable
    public static BedWarsGameType fromDisplayName(String displayName) {
        for (BedWarsGameType type : values()) {
            if (type.displayName.equalsIgnoreCase(displayName)) {
                return type;
            }
        }
        return null;
    }

    @Nullable
    public static BedWarsGameType fromId(int id) {
        for (BedWarsGameType type : values()) {
            if (type.id == id) {
                return type;
            }
        }
        return null;
    }

    public boolean isDoublesSolo() {
        return this == ONE_EIGHT || this == TWO_EIGHT || this == ULTIMATE_DOUBLES;
    }

    // if you would do the enum names correctly you could just use name() basically
    public String getQueueModeDisplayName() {
        return switch (this) {
            case ONE_EIGHT -> "Eight One";
            case TWO_EIGHT -> "Eight Two";
            case FOUR_THREE -> "Four Three";
            case TWO_FOUR -> "Four Four";
            case ONE_BLOCK -> "Eight One Oneblock";
            default -> displayName;
        };
    }

    public static List<BedWarsGameType> getDreamTypes() {
        return Arrays.stream(BedWarsGameType.values())
            .filter(type -> type.dream)
            .toList();
    }
}
