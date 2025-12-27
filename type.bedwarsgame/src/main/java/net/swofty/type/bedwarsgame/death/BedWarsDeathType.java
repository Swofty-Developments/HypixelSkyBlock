package net.swofty.type.bedwarsgame.death;

import lombok.Getter;

@Getter
public enum BedWarsDeathType {
    PLAYER_MELEE(" was slain by "),
    PLAYER_VOID_KNOCK(" was knocked into the void by "),
    PLAYER_FALL_KNOCK(" was knocked off by "),
    PLAYER_PROJECTILE(" was shot by "),
    PLAYER_EXPLOSION(" was blown up by "),
    PLAYER_FIRE(" was burned to death by "),

    VOID_ASSISTED(" was knocked into the void by "),
    FALL_ASSISTED(" was knocked off by "),

    VOID(" fell into the void."),
    FALL(" fell from a high place."),
    FIRE(" burned to death."),
    EXPLOSION(" blew up."),

    MOB_KILL(" was slain by "),

    GENERIC(" died.");

    private final String messageFormat;

    BedWarsDeathType(String messageFormat) {
        this.messageFormat = messageFormat;
    }

	public boolean involvesPlayer() {
        return switch (this) {
            case PLAYER_MELEE, PLAYER_VOID_KNOCK, PLAYER_FALL_KNOCK, PLAYER_PROJECTILE,
                 PLAYER_EXPLOSION, PLAYER_FIRE, VOID_ASSISTED, FALL_ASSISTED -> true;
            default -> false;
        };
    }

    public boolean isAssisted() {
        return this == VOID_ASSISTED || this == FALL_ASSISTED;
    }

    public boolean isDirectKill() {
        return switch (this) {
            case PLAYER_MELEE, PLAYER_VOID_KNOCK, PLAYER_FALL_KNOCK,
                 PLAYER_PROJECTILE, PLAYER_EXPLOSION, PLAYER_FIRE -> true;
            default -> false;
        };
    }

    public boolean isVoidDeath() {
        return this == VOID || this == PLAYER_VOID_KNOCK || this == VOID_ASSISTED;
    }
}

