package net.swofty.types.generic.block.placement.rules;

import net.minestom.server.coordinate.Point;
import net.minestom.server.instance.Instance;
import net.minestom.server.instance.block.Block;
import net.minestom.server.instance.block.rule.BlockPlacementRule;
import net.swofty.types.generic.block.placement.BlockUtils;
import net.swofty.types.generic.block.placement.PlacementRule;
import net.swofty.types.generic.block.placement.states.BlockState;

public class TwistingVines extends PlacementRule {

    public TwistingVines() {
        super(Block.TWISTING_VINES);
    }

    @Override
    public boolean canPlace(BlockState blockState, BlockPlacementRule.PlacementState placementState) {
        return true;
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
        Instance instance = (Instance) placementState.instance();
        Point blockPosition = placementState.placePosition();

        BlockUtils selfBlock = new BlockUtils(instance, blockPosition);
        BlockUtils downBlock = selfBlock.below();

        if (downBlock.getBlock() == Block.TWISTING_VINES) {
            instance.setBlock(downBlock.position(), Block.TWISTING_VINES_PLANT);
        }
    }
}
