package net.swofty.types.generic.block.placement.states.state;

import org.jetbrains.annotations.NotNull;

public final class BooleanState implements State<Boolean> {

    private final String key;
    private boolean value;

    private BooleanState(String key, boolean default_value) {
        this.key = key;
        this.value = default_value;
    }

    private BooleanState(Boolean value) {
        this(null, value);
    }

    private BooleanState(String key) {
        this(key, true);
    }

    public static BooleanState Of(State<?> stateKey, boolean value) {
        return new BooleanState(stateKey.getKey(), value);
    }

    @NotNull
    @Override
    public Boolean parse(String rawValue) {
        return Boolean.parseBoolean(rawValue);
    }

    @Override
    public @NotNull String getKey() {
        return key;
    }

    public BooleanState setValue(boolean value) {
        this.value = value;
        return this;
    }

    @Override
    public @NotNull String getValue() {
        return String.valueOf(value).toLowerCase().trim();
    }

    public static BooleanState Of(boolean value) {
        return new BooleanState(value);
    }

    public static BooleanState Of(String key) {
        return new BooleanState(key);
    }

    public static BooleanState Of(String key, boolean value) {
        return new BooleanState(key, value);
    }

    public static BooleanState Waterlogged(boolean value) {
        return new BooleanState("waterlogged", value);
    }
}
