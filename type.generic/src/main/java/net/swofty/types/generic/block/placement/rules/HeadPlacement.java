package net.swofty.types.generic.block.placement.rules;

import net.minestom.server.instance.block.Block;
import net.minestom.server.instance.block.BlockFace;
import net.minestom.server.instance.block.rule.BlockPlacementRule;
import net.swofty.types.generic.block.placement.states.state.Facing;
import net.swofty.types.generic.block.placement.PlacementRule;
import net.swofty.types.generic.block.placement.states.BlockState;

public class HeadPlacement extends PlacementRule {
    private final Block wall_head;

    public HeadPlacement(Block block, Block wall_head) {
        super(block);

        this.wall_head = wall_head;
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

        if (blockFace != BlockFace.TOP && blockFace != BlockFace.BOTTOM) {
            blockState.withBlock(wall_head);
            blockState.set(Facing.parse(blockFace));
        }
    }
}
