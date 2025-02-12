package net.swofty.types.generic.block.placement.states.state;

import org.jetbrains.annotations.NotNull;

import java.util.stream.Stream;

public enum RedstoneWireHeight implements State<RedstoneWireHeight> {
    NONE, SIDE, UP;

    @Override
    public @NotNull String getKey() {
        return "No Key";
    }

    @Override
    public @NotNull String getValue() {
        return name().toLowerCase().trim();
    }

    @Override
    public @NotNull RedstoneWireHeight parse(String rawValue) {
        return Stream.of(values()).filter(shape -> shape.name().equalsIgnoreCase(rawValue.trim())).findFirst().orElse(SIDE);
    }
}
