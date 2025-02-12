package net.swofty.types.generic.block.placement.states;

import net.minestom.server.instance.block.Block;
import net.swofty.types.generic.block.placement.states.state.BooleanState;
import org.jetbrains.annotations.NotNull;

public class WaterloggedState extends BlockState {
    private final BooleanState waterlogged = BooleanState.Of("waterlogged", false);

    protected WaterloggedState(Block block) {
        super(block);
    }

    public boolean getWaterlogged() {
        return get(waterlogged);
    }

    public void setWaterlogged(boolean waterlogged) {
        this.waterlogged.setValue(waterlogged);
        set(this.waterlogged);
    }

    @NotNull
    @Override
    public WaterloggedState clone() {
        return new WaterloggedState(block());
    }
}
