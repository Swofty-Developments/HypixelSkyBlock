package net.swofty.dungeons.ravengard;

import com.google.gson.annotations.SerializedName;

public enum Direction {
    @SerializedName("north") NORTH,
    @SerializedName("east") EAST,
    @SerializedName("south") SOUTH,
    @SerializedName("west") WEST;

    public Direction getOpposite() {
        return values()[(ordinal() + 2) % 4];
    }

    public Direction rotated(Rotation rotation) {
        return values()[(ordinal() + rotation.getQuarterTurns()) % 4];
    }

    public boolean isHorizontalAxis() {
        return this == EAST || this == WEST;
    }
}
