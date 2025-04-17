package net.swofty.types.generic.block.placement.states.state;

import org.jetbrains.annotations.NotNull;

public enum ChestType implements State<ChestType> {
    SINGLE, RIGHT, LEFT;

    @NotNull
    @Override
    public String getKey() {
        return "type";
    }

    @NotNull
    @Override
    public String getValue() {
        return name().trim().toLowerCase();
    }

    @NotNull
    @Override
    public ChestType parse(String rawValue) {
        for (ChestType type : values()) {
            if (type.name().equalsIgnoreCase(rawValue.trim())) {
                return type;
            }
        }
        return SINGLE;
    }

    public ChestType opposite() {
        return switch (this) {
            case LEFT -> RIGHT;
            case RIGHT -> LEFT;
            default -> SINGLE;
        };
    }
}
