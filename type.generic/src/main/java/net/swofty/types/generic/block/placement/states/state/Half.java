package net.swofty.types.generic.block.placement.states.state;

import org.jetbrains.annotations.NotNull;

import java.util.stream.Stream;

public enum Half implements State<Half> {
    TOP,
    BOTTOM;

    @Override
    public @NotNull Half parse(String input) {
        return Stream.of(values()).filter(shape -> shape.name().equalsIgnoreCase(input.trim())).findFirst().orElse(BOTTOM);
    }

    @Override
    public @NotNull String getKey() {
        return "half";
    }

    @Override
    public @NotNull String getValue() {
        return name().trim().toLowerCase();
    }
}
