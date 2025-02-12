package net.swofty.types.generic.block.placement.states.state;

import org.jetbrains.annotations.NotNull;

import java.util.stream.Stream;

public enum StairsShape implements State<StairsShape> {
    STRAIGHT,
    INNER_LEFT,
    INNER_RIGHT,
    OUTER_LEFT,
    OUTER_RIGHT;

    @Override
    public @NotNull String getKey() {
        return "shape";
    }

    @Override
    public @NotNull String getValue() {
        return name().toLowerCase().trim();
    }

    @NotNull
    @Override
    public StairsShape parse(String rawValue) {
        return Stream.of(values()).filter(stairsShape -> stairsShape.name().equalsIgnoreCase(rawValue.trim())).findFirst().orElse(STRAIGHT);
    }
}
