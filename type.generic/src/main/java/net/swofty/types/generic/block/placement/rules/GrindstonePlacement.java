package net.swofty.types.generic.block.placement.rules;

import net.minestom.server.instance.block.Block;
import net.minestom.server.instance.block.BlockFace;
import net.minestom.server.instance.block.rule.BlockPlacementRule;
import net.swofty.types.generic.block.placement.states.state.Face;
import net.swofty.types.generic.block.placement.PlacementRule;
import net.swofty.types.generic.block.placement.states.BlockState;

public class GrindstonePlacement extends PlacementRule {

    public GrindstonePlacement() {
        super(Block.GRINDSTONE);
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
        BlockFace blockFace = placementState.blockFace();

        if (blockFace == BlockFace.TOP)
            blockState.set(Face.FLOOR);
        else if (blockFace == BlockFace.BOTTOM)
            blockState.set(Face.CEILING);
        else blockState.set(Face.WALL);
    }
}
