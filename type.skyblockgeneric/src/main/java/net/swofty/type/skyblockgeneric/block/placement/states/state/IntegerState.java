package net.swofty.type.skyblockgeneric.block.placement.states.state;

import org.jetbrains.annotations.NotNull;

public class IntegerState implements State<Integer> {

    private final String key;
    private final int max;
    private final int min;
    private int value;

    protected IntegerState(String key, int max, int min, int value) {
        this.key = key;
        this.max = max;
        this.min = min;
        this.value = value;
    }

    @NotNull
    @Override
    public String getKey() {
        return key;
    }

    @NotNull
    @Override
    public String getValue() {
        return String.valueOf(value);
    }

    public IntegerState setValue(int value) {
        if (value > max)
            value = max;
        if (value < min)
            value = min;
        this.value = value;
        return this;
    }

    @NotNull
    @Override
    public Integer parse(String rawValue) {
        return Integer.parseInt(rawValue);
    }

    public int getMin() {
        return min;
    }

    public int getMax() {
        return max;
    }

    public static IntegerState Of(String key) {
        return new IntegerState(key, 0, 0, 0);
    }

    public static IntegerState Of(String key, int value) {
        return new IntegerState(key, value, value, value);
    }

    public static IntegerState Of(String key, int value, int min, int max) {
        return new IntegerState(key, max, min, value);
    }
}
