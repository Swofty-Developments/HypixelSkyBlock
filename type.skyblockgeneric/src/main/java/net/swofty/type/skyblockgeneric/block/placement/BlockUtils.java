package net.swofty.type.skyblockgeneric.block.placement;

import net.minestom.server.coordinate.Point;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.instance.Instance;
import net.minestom.server.instance.block.Block;
import net.swofty.type.generic.block.placement.states.BlockState;
import net.swofty.type.generic.block.placement.states.BlockStateManager;
import net.swofty.type.generic.block.placement.states.state.Directional;
import net.swofty.type.generic.block.placement.states.state.Facing;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class BlockUtils {
    private final Instance instance;
    private final Point position;
    private final BlockState blockState;

    public BlockUtils(Instance instance, Point position) {
        this.instance = instance;
        this.position = Pos.fromPoint(position);
        this.blockState = BlockStateManager.get(instance.getBlock(this.position));
    }

    public BlockUtils above() {
        return getRelativeTo(0, 1, 0);
    }

    public BlockUtils below() {
        return getRelativeTo(0, -1, 0);
    }

    public BlockUtils north() {
        return getRelativeTo(0, 0, -1);
    }

    public BlockUtils east() {
        return getRelativeTo(1, 0, 0);
    }

    public BlockUtils south() {
        return getRelativeTo(0, 0, 1);
    }

    public BlockUtils west() {
        return getRelativeTo(-1, 0, 0);
    }

    public Block getBlock() {
        return instance.getBlock(position);
    }

    public boolean equals(Block block) {
        return getBlock().compare(block, Block.Comparator.ID);
    }

    public BlockUtils getRelativeTo(int x, int y, int z) {
        return new BlockUtils(instance, position.add(x, y, z));
    }

    public BlockUtils getRelativeTo(Facing blockFace) {
        return getRelativeTo(blockFace.getRelativeX(), blockFace.getRelativeY(), blockFace.getRelativeZ());
    }

    public BlockUtils getRelativeTo(Directional directional) {
        return getRelativeTo(directional.toBlockFacing());
    }

    public BlockState getState() {
        return blockState;
    }

    public Instance instance() {
        return instance;
    }

    public Point position() {
        return position;
    }

    @Override
    public String toString() {
        return "BlockUtil{" +
                "instance=" + instance +
                ", position=" + position +
                ", blockState=" + blockState +
                '}';
    }

    @NotNull
    public static Block getOrDefault(@Nullable Block block, @NotNull Block defaults) {
        return block == null ? defaults : block;
    }
}