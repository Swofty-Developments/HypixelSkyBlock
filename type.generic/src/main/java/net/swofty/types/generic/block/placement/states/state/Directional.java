package net.swofty.types.generic.block.placement.states.state;

import net.minestom.server.coordinate.Pos;
import net.minestom.server.instance.block.BlockFace;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.stream.Stream;

public enum Directional implements State<Directional> {
    SELF(0, 0, 0, null),
    WEST(-1, 0, 0, BlockFace.WEST),
    EAST(1, 0, 0, BlockFace.EAST),
    NORTH(0, 0, -1, BlockFace.NORTH),
    SOUTH(0, 0, 1, BlockFace.SOUTH),
    UP(0, 1, 0, BlockFace.TOP),
    DOWN(0, -1, 0, BlockFace.BOTTOM);

    private final int x, y, z;
    private final BlockFace blockFace;

    Directional(int x, int y, int z, BlockFace face) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.blockFace = face;
    }

    public int getRelativeX() {
        return x;
    }

    public int getRelativeY() {
        return y;
    }

    public int getRelativeZ() {
        return z;
    }

    @Nullable
    public BlockFace getMinestomBlockFace() {
        return blockFace;
    }

    public Facing toBlockFacing() {
        return Facing.parse(getMinestomBlockFace());
    }

    public Pos relative(Pos from) {
        return blockFace == null ? from : from.relative(getMinestomBlockFace());
    }

    public Directional getOpposite() {
        return switch (this) {
            case UP -> DOWN;
            case DOWN -> UP;
            case EAST -> WEST;
            case WEST -> EAST;
            case NORTH -> SOUTH;
            case SOUTH -> NORTH;
            default -> SELF;
        };
    }

    public static final Directional[] axis = {NORTH, EAST, SOUTH, WEST};
    public static final Directional[] order_update = {NORTH, EAST, SOUTH, WEST, UP, DOWN};

    public @NotNull Directional parse(String input) {
        if (input == null || input.isEmpty())
            return SELF;
        return Stream.of(values()).filter(shape -> shape.name().equalsIgnoreCase(input.trim())).findFirst().orElse(SELF);
    }

    public static Directional parse(BlockFace input) {
        return Stream.of(values()).filter(facing -> facing.blockFace == input).findFirst().orElse(SELF);
    }

    public static Directional parse(Facing input) {
        return values()[input.ordinal()];
    }

    @Override
    public @NotNull String getKey() {
        return name().toLowerCase().trim();
    }

    @Override
    public @NotNull String getValue() {
        return name().trim().toLowerCase();
    }

    public Axis getAxis() {
        return toBlockFacing().getAxis();
    }

    public Directional rotateY() {
        return switch (this) {
            case NORTH -> EAST;
            case SOUTH -> WEST;
            case WEST -> NORTH;
            case EAST -> SOUTH;
            default -> throw new IllegalStateException("Unable to get Y-rotated facing of " + this);
        };
    }

    public Directional rotateYCCW() {
        return switch (this) {
            case NORTH -> WEST;
            case SOUTH -> EAST;
            case WEST -> SOUTH;
            case EAST -> NORTH;
            default -> throw new IllegalStateException("Unable to get CCW facing of " + this);
        };
    }
}
