package net.swofty.types.generic.block.placement.states;

import net.minestom.server.instance.block.Block;
import net.minestom.server.utils.validate.Check;
import net.swofty.types.generic.block.placement.states.state.Attachment;
import net.swofty.types.generic.block.placement.states.state.Facing;
import org.jetbrains.annotations.NotNull;

public class BellState extends BlockState {

    public BellState(Block blockAlternative) {
        super(blockAlternative);
        Check.argCondition(block() != Block.BELL, "Only Bell block is allowed here !");
    }

    public Attachment getAttachment() {
        return getOr(Attachment.class, Attachment.CEILING);
    }

    public Facing getFacing() {
        return getOr(Facing.class, Facing.NORTH);
    }

    public void setFacing(Facing facing) {
        set(facing);
    }

    public void setAttachment(Attachment attachment) {
        set(attachment);
    }

    @NotNull
    @Override
    public BellState clone() {
        return new BellState(block());
    }
}
