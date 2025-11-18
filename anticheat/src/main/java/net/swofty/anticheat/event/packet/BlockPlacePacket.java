package net.swofty.anticheat.event.packet;

import lombok.Getter;
import net.swofty.anticheat.math.Pos;

import java.util.UUID;

@Getter
public class BlockPlacePacket extends SwoftyPacket {
    private final Hand hand;
    private final Pos position;
    private final Direction direction;
    private final float cursorX;
    private final float cursorY;
    private final float cursorZ;
    private final boolean insideBlock;
    private final int sequence;

    public BlockPlacePacket(UUID uuid, Hand hand, Pos position, Direction direction,
                           float cursorX, float cursorY, float cursorZ,
                           boolean insideBlock, int sequence) {
        super(uuid);
        this.hand = hand;
        this.position = position;
        this.direction = direction;
        this.cursorX = cursorX;
        this.cursorY = cursorY;
        this.cursorZ = cursorZ;
        this.insideBlock = insideBlock;
        this.sequence = sequence;
    }

    public enum Hand {
        MAIN_HAND, OFF_HAND
    }

    public enum Direction {
        DOWN, UP, NORTH, SOUTH, WEST, EAST
    }
}
