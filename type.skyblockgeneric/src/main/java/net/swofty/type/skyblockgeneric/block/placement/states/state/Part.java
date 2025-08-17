package net.swofty.type.skyblockgeneric.block.placement.states.state;

import org.jetbrains.annotations.NotNull;

import java.util.stream.Stream;

public enum Part implements State<Part> {
    FOOT, HEAD;

    @NotNull
    @Override
    public String getKey() {
        return "part";
    }

    @NotNull
    @Override
    public String getValue() {
        return name().trim().toLowerCase();
    }

    @NotNull
    @Override
    public Part parse(String rawValue) {
        return Stream.of(values()).filter(shape -> shape.name().equalsIgnoreCase(rawValue.trim())).findFirst().orElse(FOOT);
    }
}
