package net.swofty.type.skyblockgeneric.block.placement.rules;

import net.minestom.server.instance.Instance;
import net.minestom.server.instance.block.Block;
import net.minestom.server.instance.block.rule.BlockPlacementRule;
import net.swofty.type.generic.block.placement.BlockUtils;
import net.swofty.type.generic.block.placement.PlacementRule;
import net.swofty.type.generic.block.placement.states.BlockState;
import net.swofty.type.generic.block.placement.states.WallState;
import net.swofty.type.generic.block.placement.states.state.BooleanState;
import net.swofty.type.generic.block.placement.states.state.Facing;

public class FenceGatePlacement extends PlacementRule {

    public FenceGatePlacement(Block block) {
        super(block);
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
        net.minestom.server.coordinate.Point blockPosition = placementState.placePosition();
        Facing facing = blockState.get(Facing.class);
        BlockUtils selfBlock = new BlockUtils((Instance) instance, blockPosition);

        switch (facing) {
            case WEST, EAST ->
                    blockState.set(BooleanState.Of("in_wall", (isWall(selfBlock.south()) || isWall(selfBlock.north()))));
            case SOUTH, NORTH ->
                    blockState.set(BooleanState.Of("in_wall", (isWall(selfBlock.east()) || isWall(selfBlock.west()))));
        }
    }

    public boolean isWall(BlockUtils alsBlock) {
        return isWall(alsBlock.getState());
    }

    public boolean isWall(BlockState state) {
        return state instanceof WallState;
    }
}
