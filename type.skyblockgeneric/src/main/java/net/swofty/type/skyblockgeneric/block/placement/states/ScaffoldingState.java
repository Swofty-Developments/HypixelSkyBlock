package net.swofty.type.skyblockgeneric.block.placement.states;

import net.minestom.server.instance.block.Block;
import net.swofty.type.generic.block.placement.states.state.BooleanState;
import org.jetbrains.annotations.NotNull;

public class ScaffoldingState extends WaterloggedState {
    public ScaffoldingState(Block block) {
        super(block);
    }

    public boolean hasBottom() {
        return get(BooleanState.Of("bottom"));
    }

    public void setBottom(boolean bottom) {
        set(BooleanState.Of("bottom", bottom));
    }

    @NotNull
    @Override
    public ScaffoldingState clone() {
        return new ScaffoldingState(block());
    }
}
