package net.swofty.type.skyblockgeneric.block.placement.states.state;

import org.jetbrains.annotations.NotNull;

import java.util.stream.Stream;

public enum WallHeight implements State<WallHeight> {
    NONE, LOW, TALL;

    @Override
    public @NotNull String getKey() {
        return "NoKey";
    }

    @Override
    public @NotNull String getValue() {
        return name().trim().toLowerCase();
    }

    @Override
    public @NotNull WallHeight parse(String input) {
        return Stream.of(values()).filter(shape -> shape.name().equalsIgnoreCase(input.trim())).findFirst().orElse(NONE);
    }
}
