package net.swofty.types.generic.block.placement;

import net.minestom.server.instance.block.Block;
import net.minestom.server.instance.block.rule.BlockPlacementRule;
import net.swofty.types.generic.block.placement.states.BlockState;
import net.swofty.types.generic.block.placement.states.BlockStateManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.function.Supplier;

public class MultiplePlacementRules extends BlockPlacementRule {
    private final ArrayList<PlacementRule> blockPlacements = new ArrayList<>();

    public MultiplePlacementRules(@NotNull Block block) {
        super(block);
    }

    @Override
    public @Nullable Block blockPlace(@NotNull PlacementState placementState) {
        //Get the current block state
        BlockState blockState = BlockStateManager.get(block);

        //Block cannot be placed
        for (PlacementRule placement : getPlacementRules()) {
            if (!placement.canPlace(blockState, placementState)) {
                return getBlock();
            }
        }

        for (PlacementRule placement : getPlacementRules())
            placement.place(blockState, placementState);

        return blockState.block();
    }

    @Override
    public @NotNull Block blockUpdate(@NotNull UpdateState updateState) {
        BlockState blockState = BlockStateManager.get(block);

        for (PlacementRule placement : getPlacementRules())
            if (placement.canUpdate(blockState, updateState))
                placement.update(blockState, updateState);

        return blockState.block();
    }

    public void addBlockPlacement(PlacementRule placementRule) {
        blockPlacements.add(placementRule);
    }

    public void addBlockPlacement(Supplier<PlacementRule> placementRule) {
        addBlockPlacement(placementRule.get());
    }

    public ArrayList<PlacementRule> getPlacementRules() {
        return blockPlacements;
    }
}
