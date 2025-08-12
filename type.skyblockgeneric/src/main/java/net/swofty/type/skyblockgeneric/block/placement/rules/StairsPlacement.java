package net.swofty.type.skyblockgeneric.block.placement.rules;

import net.minestom.server.coordinate.Point;
import net.minestom.server.instance.Instance;
import net.minestom.server.instance.block.Block;
import net.minestom.server.instance.block.BlockFace;
import net.minestom.server.instance.block.rule.BlockPlacementRule;
import net.swofty.type.generic.block.placement.BlockUtils;
import net.swofty.type.generic.block.placement.PlacementRule;
import net.swofty.type.generic.block.placement.states.BlockState;
import net.swofty.type.generic.block.placement.states.BlockStateManager;
import net.swofty.type.generic.block.placement.states.state.Facing;
import net.swofty.type.generic.block.placement.states.state.Half;
import net.swofty.type.generic.block.placement.states.state.StairsShape;

public class StairsPlacement extends PlacementRule {

    public StairsPlacement(Block block) {
        super(block);
    }

    @Override
    public boolean canPlace(BlockState blockState, BlockPlacementRule.PlacementState placementState) {
        return true;
    }

    @Override
    public boolean canUpdate(BlockState blockState, BlockPlacementRule.UpdateState updateState) {
        return true;
    }

    @Override
    public void update(BlockState blockState, BlockPlacementRule.UpdateState updateState) {
        Facing facing = blockState.get(Facing.class);
        Instance instance = (Instance) updateState.instance();
        Point blockPosition = updateState.blockPosition();

        BlockUtils selfBlock = new BlockUtils(instance, blockPosition);
        BlockUtils facingBlock = selfBlock.getRelativeTo(facing);
        BlockUtils oppositeBlock = selfBlock.getRelativeTo(facing.opposite());

        BlockState facingStates = facingBlock.getState();

        if (isStairs(facingBlock) && facingStates.get(Half.class) == blockState.get(Half.class)) {
            Facing facingOfFacing = facingStates.get(Facing.class);
            if (facingOfFacing.getAxis() != facing.getAxis() && isDifferentStairs(blockState, instance, selfBlock, facingOfFacing)) {
                if (facingOfFacing == facing.rotateYCCW())
                    blockState.set(StairsShape.OUTER_LEFT);
                else
                    blockState.set(StairsShape.OUTER_RIGHT);
                return;
            }
        }

        BlockState oppositeStates = oppositeBlock.getState();

        if (isStairs(oppositeStates) && blockState.get(Half.class) == oppositeStates.get(Half.class)) {
            Facing oppositeFacing = oppositeStates.get(Facing.class);
            if (oppositeFacing.getAxis() != facing.getAxis() && isDifferentStairs(blockState, instance, oppositeBlock, oppositeFacing)) {
                if (oppositeFacing == facing.rotateYCCW()) {
                    blockState.set(StairsShape.INNER_LEFT);
                } else {
                    blockState.set(StairsShape.INNER_RIGHT);
                }
                return;
            }
        }

        blockState.set(StairsShape.STRAIGHT);
    }

    @Override
    public void place(BlockState blockState, BlockPlacementRule.PlacementState placementState) {
        BlockFace blockFace = placementState.blockFace();

        if (blockFace == BlockFace.BOTTOM || blockFace == BlockFace.TOP) {
            if (blockFace == BlockFace.BOTTOM)
                blockState.set(Half.TOP);
            else
                blockState.set(Half.BOTTOM);
        } else {
            Point hit = placementState.cursorPosition();

            if (hit != null) {
                double y = hit.y() - hit.blockY();

                blockState.set((y >= 0.5) ? Half.TOP : Half.BOTTOM);
            }

            blockState.set(blockState.get(Facing.class).opposite());
        }
    }

    public static boolean isStairs(Block block) {
        return block.name().toLowerCase().trim().endsWith("stairs");
    }

    private boolean isDifferentStairs(BlockState states, Instance instance, BlockUtils pos, Facing facing) {
        BlockState blockState = BlockStateManager.get(instance.getBlock(pos.getRelativeTo(facing.opposite()).position()));
        return !isStairs(blockState) ||
                blockState.get(Facing.class) != states.get(Facing.class) ||
                blockState.get(Half.class) != states.get(Half.class);
    }

    private boolean isStairs(BlockUtils alsBlock) {
        return isStairs(alsBlock.getBlock());
    }

    private boolean isStairs(BlockState blockState) {
        return isStairs(blockState.block());
    }
}
