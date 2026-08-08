package net.swofty.type.skyblockgeneric.experimentation;

public enum ExperimentType {
    SUPERPAIRS("Superpairs"),
    CHRONOMATRON("Chronomatron"),
    ULTRASEQUENCER("Ultrasequencer");

    private final String displayName;

    ExperimentType(String displayName) {
        this.displayName = displayName;
    }

    public String displayName() {
        return displayName;
    }
}
