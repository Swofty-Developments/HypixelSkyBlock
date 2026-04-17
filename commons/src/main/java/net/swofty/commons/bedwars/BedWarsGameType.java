package net.swofty.commons.bedwars;

import lombok.Getter;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;

@Getter
public enum BedWarsGameType {
    SOLO(0, "Solo", 1, 8, List.of(
        "Fight against 7 other players!",
        "Destroy enemy beds to stop them",
        "from respawning!",
        "Protect your bed from destruction!"
    )),
    DOUBLES(1, "Doubles", 2, 8, List.of(
        "Team up with 1 other player to",
        "defeat 7 enemy teams!",
        "Destroy enemy beds to stop them",
        "from respawning!",
        "Protect your bed from destruction!"
    )),
    THREE_THREE_THREE_THREE(2, "3v3v3v3", 3, 4, List.of(
        "Team up with 2 other players to",
        "defeat 3 other groups of players!",
        "Destroy enemy beds to stop them",
        "from respawning!",
        "Protect your bed from destruction!"
    )),
    FOUR_FOUR_FOUR_FOUR(3, "4v4v4v4", 4, 4, List.of(
        "Team up with 3 other players to",
        "defeat 3 other groups of players!",
        "Destroy enemy beds to stop them",
        "from respawning!",
        "Protect your bed from destruction!"
    )),
    FOUR_FOUR(4, "4v4", 4, 2, List.of(
        "4v4 is the classic Bed Wars",
        "everyone knows and loves, but with",
        "only 1 enemy team!"
    )),

    // dream
    ULTIMATE_DOUBLES(5, "Ultimate Doubles", 2, 8, List.of(), true),
    ULTIMATE_FOURS(6, "Ultimate 4v4v4v4", 4, 4, List.of(), true),
    SWAPPAGE_DOUBLES(7, "Swappage Doubles", 2, 8, List.of("Use the Swap to change ownership", "between beds! Chaos ensues."), true),
    SWAPPAGE_FOURS(8, "Swappage 4v4v4v4", 4, 4, List.of("Use the Swap to change ownership", "between beds! Chaos ensues."), true),
    RUSH(9, "Rush V2", 2, 8, List.of(), true),
    ULTIMATE(10, "Ultimate V2", 2, 8, List.of(), true),
    VOIDLESS(11, "Voidless", 2, 8, List.of(), true),
    ARMED(12, "Armed", 2, 8, List.of(), true),
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
        return this == SOLO || this == DOUBLES || this == ULTIMATE_DOUBLES;
    }

    public static List<BedWarsGameType> getDreamTypes() {
        return Arrays.stream(BedWarsGameType.values())
            .filter(type -> type.dream)
            .toList();
    }
}
