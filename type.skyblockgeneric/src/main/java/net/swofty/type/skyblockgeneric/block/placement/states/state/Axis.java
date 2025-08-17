package net.swofty.type.skyblockgeneric.block.placement.states.state;

import org.jetbrains.annotations.NotNull;

public enum Axis implements State<Axis> {
    X, Y, Z;

    public static Axis of(Facing blockFace) {
        return switch (blockFace) {
            case EAST, WEST -> X;
            case NORTH, SOUTH -> Z;
            default -> Y;
        };
    }

    public boolean isHorizontal() {
        return this == X || this == Z;
    }

    @Override
    public @NotNull String getKey() {
        return "axis";
    }

    @Override
    public @NotNull String getValue() {
        return name().trim().toLowerCase();
    }

    @Override
    public @NotNull Axis parse(String rawValue) {
        if (rawValue.equalsIgnoreCase("x"))
            return X;
        else if (rawValue.equalsIgnoreCase("y"))
            return Y;
        else return Z;
    }
}
