package net.swofty.type.skyblockgeneric.targetpractice;

import net.minestom.server.coordinate.Pos;

import javax.annotation.Nullable;

public enum PracticeTargets {
    TARGET_1(new Pos(7, 63, -141)),
    TARGET_2(new Pos(7, 62, -145)),
    TARGET_3(new Pos(7, 61, -149)),
    TARGET_4(new Pos(5, 63, -154)),
    TARGET_5(new Pos(0, 63, -156)),
    TARGET_6(new Pos(-1, 61, -156)),
    TARGET_7(new Pos(-5, 63, -156)),
    TARGET_8(new Pos(-9, 64, -149)),
    TARGET_9(new Pos(-10, 60, -152)),
    TARGET_10(new Pos(-10, 62, -147)),
    TARGET_11(new Pos(-12, 63, -145)),
    TARGET_12(new Pos(-12, 62, -140)),
    TARGET_13(new Pos(-10, 64, -139)),
    TARGET_14(new Pos(-4, 66, -141)),
    TARGET_15(new Pos(1, 63, -131)),
    TARGET_16(new Pos(-1, 62, -132))
    ;

    private static final PracticeTargets[] VALUES = values();
    private Pos location;

    PracticeTargets(Pos location) {
        this.location = location;
    }

    public Pos getLocation() {
        return location;
    }

    public static @Nullable PracticeTargets getFromPosition(Pos pos) {
        for (PracticeTargets target : VALUES) {
            if (target.getLocation().equals(pos)) {
                return target;
            }
        }
        return null;
    }
}
