package net.swofty.types.generic.block.placement.states.state;

import org.jetbrains.annotations.NotNull;

import java.util.stream.Stream;

public enum DoorHalf implements State<DoorHalf> {
    LOWER, UPPER;

    @NotNull
    @Override
    public String getKey() {
        return "half";
    }

    @NotNull
    @Override
    public String getValue() {
        return name().trim().toLowerCase();
    }

    @NotNull
    @Override
    public DoorHalf parse(String rawValue) {
        return Stream.of(values()).filter(shape -> shape.name().equalsIgnoreCase(rawValue.trim())).findFirst().orElse(LOWER);
    }
}
