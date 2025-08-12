package net.swofty.type.skyblockgeneric.item.handlers.anvilcombine;

import net.swofty.type.skyblockgeneric.item.SkyBlockItem;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;

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
        boolean canApply(SkyBlockPlayer player, SkyBlockItem upgradeItem, SkyBlockItem sacrificeItem);
    }

    @FunctionalInterface
    public interface CostFunction {
        int getCost(SkyBlockItem upgradeItem, SkyBlockItem sacrificeItem, SkyBlockPlayer player);
    }
}