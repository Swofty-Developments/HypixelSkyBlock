package net.swofty.type.skyblockgeneric.item.handlers.anvilcombine;

import net.swofty.type.generic.item.SkyBlockItem;
import net.swofty.type.generic.user.HypixelPlayer;

public record AnvilCombineHandler(
        CombineFunction combineFunction,
        ValidateFunction validateFunction,
        CostFunction costFunction
) {
    @FunctionalInterface
    public interface CombineFunction {
        void apply(SkyBlockItem upgradeItem, SkyBlockItem sacrificeItem);
    }

    @FunctionalInterface
    public interface ValidateFunction {
        boolean canApply(HypixelPlayer player, SkyBlockItem upgradeItem, SkyBlockItem sacrificeItem);
    }

    @FunctionalInterface
    public interface CostFunction {
        int getCost(SkyBlockItem upgradeItem, SkyBlockItem sacrificeItem, HypixelPlayer player);
    }
}