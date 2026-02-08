package net.swofty.commons.zombies;

import lombok.Getter;
import org.jetbrains.annotations.Nullable;

@Getter
public enum ZombiesMap {
    DEAD_END(0, "Dead End", 30, "The original Zombies map. Stranded in the middle of a city, make your way through the high rises to battle the undead!"),
    BAD_BLOOD(1, "Bad Blood", 30, "After crashing into the courtyard of a beautiful mansion, fend off zombies as you uncover the secrets hidden within!"),
    ALIEN_ARCADIUM(2, "Alien Arcadium", 105, "Extraterrestrial Zombies have attacked! Battle alien creatures and save the theme park from destruction."),
    PRISON(3, "Prison", 30, "The cells of the undead have been opened! Save yourselves and escape from the Prison!");

    private final int id;
    private final String displayName;
    private final int maxRounds;
    private final String description;

    ZombiesMap(int id, String displayName, int maxRounds, String description) {
        this.id = id;
        this.displayName = displayName;
        this.maxRounds = maxRounds;
        this.description = description;
    }

    public boolean isExtendedMap() {
        return this == ALIEN_ARCADIUM;
    }

    @Nullable
    public static ZombiesMap from(String field) {
        for (ZombiesMap map : values()) {
            if (map.name().equalsIgnoreCase(field)) {
                return map;
            }
        }
        return null;
    }

    @Nullable
    public static ZombiesMap fromDisplayName(String displayName) {
        for (ZombiesMap map : values()) {
            if (map.displayName.equalsIgnoreCase(displayName)) {
                return map;
            }
        }
        return null;
    }

    @Nullable
    public static ZombiesMap fromId(int id) {
        for (ZombiesMap map : values()) {
            if (map.id == id) {
                return map;
            }
        }
        return null;
    }
}
