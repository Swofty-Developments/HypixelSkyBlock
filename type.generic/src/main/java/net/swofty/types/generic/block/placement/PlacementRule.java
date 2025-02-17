package net.swofty.types.generic.block.placement;

import net.minestom.server.instance.block.Block;
import net.minestom.server.instance.block.rule.BlockPlacementRule;
import net.swofty.types.generic.block.placement.states.BlockState;

public abstract class PlacementRule {
    private final Block block;

    public PlacementRule(Block block) {
        this.block = block;
    }

    public Block block() {
        return block;
    }

    /**
     * @return true if the block can be placed
     */
    public abstract boolean canPlace(BlockState blockState, BlockPlacementRule.PlacementState placementState);

    /**
     * @return true if the block can be updated
     */
    public abstract boolean canUpdate(BlockState blockState, BlockPlacementRule.UpdateState updateState);

    /**
     * @param updateState update state
     */
    public abstract void update(BlockState blockState, BlockPlacementRule.UpdateState updateState);

    /**
     * @param placementState placement state
     */
    public abstract void place(BlockState blockState, BlockPlacementRule.PlacementState placementState);
}
