package net.swofty.type.skyblockgeneric.block.placement.states;

import net.minestom.server.instance.block.Block;
import net.swofty.type.skyblockgeneric.block.placement.states.state.BooleanState;
import net.swofty.type.skyblockgeneric.block.placement.states.state.Facing;
import org.jetbrains.annotations.NotNull;

public class LecternState extends BlockState {

    public LecternState(Block block) {
        super(block);
    }

    public Facing getFacing() {
        return getOr(Facing.class, Facing.NORTH);
    }

    public boolean hasBook() {
        return get(BooleanState.Of("has_book"));
    }

    public void setHasBook(boolean book) {
        set(BooleanState.Of("has_book", book));
    }

    @NotNull
    @Override
    public LecternState clone() {
        return new LecternState(block());
    }
}
