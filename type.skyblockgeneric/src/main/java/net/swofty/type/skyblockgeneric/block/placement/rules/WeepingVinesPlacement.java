package net.swofty.type.skyblockgeneric.block.placement.rules;

import net.minestom.server.coordinate.Point;
import net.minestom.server.instance.Instance;
import net.minestom.server.instance.block.Block;
import net.minestom.server.instance.block.BlockFace;
import net.minestom.server.instance.block.rule.BlockPlacementRule;
import net.swofty.type.skyblockgeneric.block.placement.BlockUtils;
import net.swofty.type.skyblockgeneric.block.placement.PlacementRule;
import net.swofty.type.skyblockgeneric.block.placement.states.BlockState;

public class WeepingVinesPlacement extends PlacementRule {

    public WeepingVinesPlacement() {
        super(Block.WEEPING_VINES);
    }

    @Override
    public boolean canPlace(BlockState blockState, BlockPlacementRule.PlacementState placementState) {
        return placementState.blockFace() == BlockFace.BOTTOM;
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
        BlockUtils upBlock = selfBlock.above();

        if (upBlock.getBlock() == Block.WEEPING_VINES) {
            instance.setBlock(upBlock.position(), Block.WEEPING_VINES_PLANT);
        }
    }
}
