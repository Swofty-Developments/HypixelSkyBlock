package net.swofty.dungeons.ravengard;

import lombok.Getter;

@Getter
public enum Rotation {
    NONE(0),
    CLOCKWISE_90(1),
    CLOCKWISE_180(2),
    CLOCKWISE_270(3);

    private final int quarterTurns;

    Rotation(int quarterTurns) {
        this.quarterTurns = quarterTurns;
    }

    public int getDegrees() {
        return quarterTurns * 90;
    }

    public boolean swapsAxes() {
        return quarterTurns % 2 == 1;
    }
}
