package net.swofty.types.generic.block.placement.states;

import net.minestom.server.instance.block.Block;
import org.jetbrains.annotations.NotNull;

public class WallState extends WaterloggedState {

    //TODO Complete Wall BlockSate

    public WallState(Block alternative) {
        super(alternative);
    }

    @NotNull
    @Override
    public WallState clone() {
        return new WallState(block());
    }
}
