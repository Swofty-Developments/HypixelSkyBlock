package net.swofty.type.skyblockgeneric.block.placement.states;

import net.minestom.server.instance.block.Block;
import net.swofty.type.generic.block.placement.states.state.BooleanState;
import net.swofty.type.generic.block.placement.states.state.Directional;
import net.swofty.type.generic.block.placement.states.state.Facing;
import org.jetbrains.annotations.NotNull;

public class FenceGateState extends WaterloggedState {
    public FenceGateState(Block block) {
        super(block);
    }

    public boolean inWall() {
        return get(BooleanState.Of("in_wall"));
    }

    public boolean isOpen() {
        return get(BooleanState.Of("open"));
    }

    public Facing getFacing() {
        return get(Facing.class);
    }

    public void setFacing(Facing facing) {
        set(facing);
    }

    public void setInWall(boolean bool) {
        set(BooleanState.Of("in_wall", bool));
    }

    public void setIsOpen(boolean bool) {
        set(BooleanState.Of("open", bool));
    }

    public boolean isParallel(BlockState state, Directional directional) {
        return state.getOr(Facing.class, Facing.SELF).getAxis() == directional.rotateY().getAxis();
    }

    @NotNull
    @Override
    public FenceGateState clone() {
        return new FenceGateState(block());
    }
}
