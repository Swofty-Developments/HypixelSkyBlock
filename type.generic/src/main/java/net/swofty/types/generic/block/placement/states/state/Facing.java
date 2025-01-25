package net.swofty.types.generic.block.placement.states.state;

import net.minestom.server.coordinate.Pos;
import net.minestom.server.instance.block.BlockFace;
import net.minestom.server.utils.Direction;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.stream.Stream;

public enum Facing implements State<Facing> {
    SELF(0, 0, 0, null, 0),
    WEST(-1, 0, 0, BlockFace.WEST, 4),
    EAST(1, 0, 0, BlockFace.EAST, 5),
    NORTH(0, 0, -1, BlockFace.NORTH, 2),
    SOUTH(0, 0, 1, BlockFace.SOUTH, 3),
    UP(0, 1, 0, BlockFace.TOP, 1),
    DOWN(0, -1, 0, BlockFace.BOTTOM, 0);

    private final int x, y, z;
    private final BlockFace blockFace;
    private final int packetIndex;

    Facing(int x, int y, int z, BlockFace face, int packetIndex) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.blockFace = face;
        this.packetIndex = packetIndex;
    }

    public int getPacketIndex() {
        return packetIndex;
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

    public Pos relative(Pos from) {
        return blockFace == null ? from : from.relative(getMinestomBlockFace());
    }

    public Facing rotateYCCW() {
        return switch (this) {
            case NORTH -> WEST;
            case SOUTH -> EAST;
            case WEST -> SOUTH;
            case EAST -> NORTH;
            default -> throw new IllegalStateException("Unable to get CCW facing of " + this);
        };
    }

    public Facing rotateY() {
        return switch (this) {
            case NORTH -> EAST;
            case SOUTH -> WEST;
            case WEST -> NORTH;
            case EAST -> SOUTH;
            default -> throw new IllegalStateException("Unable to get Y-rotated facing of " + this);
        };
    }

    public static Facing fromYaw(float yaw) {
        return axis[Math.round(yaw / 90F) & 3].opposite();
    }

    public Axis getAxis() {
        return Axis.of(this);
    }

    @Override
    public @NotNull String getKey() {
        return "facing";
    }

    @Override
    public @NotNull String getValue() {
        return name().trim().toLowerCase();
    }

    public static final Facing[] axis = {Facing.NORTH, Facing.EAST, Facing.SOUTH, Facing.WEST};

    public Facing opposite() {
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

    public Facing parse(String input) {
        if (input == null || input.isEmpty())
            return SELF;
        return Stream.of(values()).filter(shape -> shape.name().equalsIgnoreCase(input.trim())).findFirst().orElse(SELF);
    }

    public static Facing parse(BlockFace input) {
        return Stream.of(values()).filter(facing -> facing.blockFace == input).findFirst().orElse(SELF);
    }

    public static Facing parse(Direction input) {
        return Stream.of(values()).filter(facing -> facing.name().trim().toLowerCase().equalsIgnoreCase(input.name().trim())).findFirst().orElse(SELF);
    }
}
