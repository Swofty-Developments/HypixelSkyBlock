package net.swofty.types.generic.user.fairysouls;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public enum FairySoulExchangeLevels {
    EXCHANGE_1(10, 1),
    EXCHANGE_2(10, 1),
    EXCHANGE_3(10, 0),
    EXCHANGE_4(10, 1),
    EXCHANGE_5(10, 0),
    EXCHANGE_6(10, 1),
    EXCHANGE_7(10, 0),
    EXCHANGE_8(10, 1),
    EXCHANGE_9(10, 0),
    EXCHANGE_10(10, 1),
    EXCHANGE_11(10, 0),
    EXCHANGE_12(10, 0),
    EXCHANGE_13(10, 1),
    ;

    private final double skyBlockXP;
    private final int backpackSlots;

    FairySoulExchangeLevels(double skyBlockXP, int backpackSlots) {
        this.skyBlockXP = skyBlockXP;
        this.backpackSlots = backpackSlots;
    }

    public List<String> getDisplay() {
        List<String> toReturn = new ArrayList<>();

        if (backpackSlots > 0) {
            toReturn.add("§6" + backpackSlots + " Backpack Slots");
        }
        if (skyBlockXP > 0) {
            toReturn.add("§8+ §b" + skyBlockXP + " SkyBlock XP");
        }

        return toReturn;
    }

    public static FairySoulExchangeLevels getLevel(int level) {
        for (FairySoulExchangeLevels levels : values()) {
            if ((levels.ordinal() + 1) == level) {
                return levels;
            }
        }
        return null;
    }
}
