package net.swofty.type.skyblockgeneric.block.placement.rules;

import net.minestom.server.instance.block.Block;
import net.minestom.server.instance.block.BlockFace;
import net.minestom.server.instance.block.rule.BlockPlacementRule;
import net.swofty.type.generic.block.placement.PlacementRule;
import net.swofty.type.generic.block.placement.states.BlockState;
import net.swofty.type.generic.block.placement.states.state.Face;

public class ButtonAndLeverPlacement extends PlacementRule {
    public ButtonAndLeverPlacement(Block block) {
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
        BlockFace blockFace = placementState.blockFace();
        if (blockFace == BlockFace.TOP) {
            blockState.set(Face.FLOOR);
        } else if (blockFace == BlockFace.BOTTOM) {
            blockState.set(Face.CEILING);
        } else {
            blockState.set(Face.WALL);
        }
    }
}
