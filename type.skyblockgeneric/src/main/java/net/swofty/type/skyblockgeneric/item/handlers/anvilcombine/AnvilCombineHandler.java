package net.swofty.type.skyblockgeneric.item.handlers.anvilcombine;

import net.swofty.type.skyblockgeneric.item.SkyBlockItem;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;
import org.jetbrains.annotations.Nullable;

public record AnvilCombineHandler(
        CombineFunction combineFunction,
        ValidateFunction validateFunction,
        CostFunction costFunction,
        @Nullable OnCraftFunction onCraftFunction
) {
    // Constructor without onCraftFunction for backwards compatibility
    public AnvilCombineHandler(
            CombineFunction combineFunction,
            ValidateFunction validateFunction,
            CostFunction costFunction
    ) {
        this(combineFunction, validateFunction, costFunction, null);
    }

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

    /**
     * Called when the player actually confirms the craft (not during preview).
     * Use this for deducting non-XP costs like Bits.
     * Returns true if craft should proceed, false to cancel.
     */
    @FunctionalInterface
    public interface OnCraftFunction {
        boolean onCraft(SkyBlockPlayer player, SkyBlockItem upgradeItem, SkyBlockItem sacrificeItem);
    }
}