package net.swofty.types.generic.block.placement.states.state;

import org.jetbrains.annotations.NotNull;

import java.util.stream.Stream;

public enum Face implements State<Face> {
    FLOOR, WALL, CEILING;

    @NotNull
    @Override
    public String getKey() {
        return "face";
    }

    @NotNull
    @Override
    public String getValue() {
        return name().toLowerCase().trim();
    }

    @NotNull
    @Override
    public Face parse(String rawValue) {
        return Stream.of(values()).filter(shape -> shape.name().equalsIgnoreCase(rawValue.trim())).findFirst().orElse(FLOOR);
    }
}
