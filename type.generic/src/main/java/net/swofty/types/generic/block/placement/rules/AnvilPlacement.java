package net.swofty.types.generic.block.placement.rules;

import net.minestom.server.instance.block.Block;
import net.minestom.server.instance.block.rule.BlockPlacementRule;
import net.swofty.types.generic.block.placement.states.state.Facing;
import net.swofty.types.generic.block.placement.PlacementRule;
import net.swofty.types.generic.block.placement.states.BlockState;

public class AnvilPlacement extends PlacementRule {

    public AnvilPlacement(Block block) {
        super(block);
    }

    @Override
    public boolean canPlace(BlockState blockState, BlockPlacementRule.PlacementState placementState) {
        return false;
    }

    @Override
    public boolean canUpdate(BlockState blockState, BlockPlacementRule.UpdateState updateState) {
        return false;
    }

    @Override
    public void update(BlockState blockState, BlockPlacementRule.UpdateState updateState) {

    }

    @Override
    public void place(BlockState blockState, BlockPlacementRule.PlacementState placementState) {
        blockState.set(blockState.get(Facing.class).rotateY());
    }
}
