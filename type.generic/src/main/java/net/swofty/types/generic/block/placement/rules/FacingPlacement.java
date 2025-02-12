package net.swofty.types.generic.block.placement.rules;

import net.minestom.server.coordinate.Pos;
import net.minestom.server.instance.block.Block;
import net.minestom.server.instance.block.BlockFace;
import net.minestom.server.instance.block.rule.BlockPlacementRule;
import net.swofty.types.generic.block.placement.states.state.Facing;
import net.swofty.types.generic.block.placement.PlacementRule;
import net.swofty.types.generic.block.placement.states.BlockState;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

public class FacingPlacement extends PlacementRule {
    private final List<Block> needToBeInverted =
            Arrays.asList(Block.HOPPER, Block.LECTERN, Block.REPEATER, Block.COMPARATOR, Block.CHEST, Block.ENDER_CHEST,
                    Block.TRAPPED_CHEST, Block.END_PORTAL_FRAME);

    private final List<Block> onlyDirectional =
            Arrays.asList(Block.DISPENSER, Block.PISTON, Block.STICKY_PISTON, Block.DROPPER, Block.OBSERVER);

    public FacingPlacement(@NotNull Block block) {
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
        BlockFace facing = blockFace;
        Pos playerPosition = placementState.playerPosition();

        if (!hasUpAndDown(blockState) && (blockFace == BlockFace.BOTTOM || blockFace == BlockFace.TOP)
                || (onlyDirectional.contains(block()) && playerPosition.pitch() < 45.5)) {
            facing = Facing.fromYaw(playerPosition.yaw()).getMinestomBlockFace();
        }

        if (needToBeInverted.contains(block()) || onlyDirectional.contains(block())) {
            facing = facing.getOppositeFace();
        }

        blockState.set(Facing.parse(facing));
    }

    public boolean hasUpAndDown(BlockState blockState) {
        List<Facing> facingValue = blockState.getAllStateValue(Facing.class);
        return facingValue.stream().anyMatch(facing -> facing == Facing.UP || facing == Facing.DOWN);
    }
}
