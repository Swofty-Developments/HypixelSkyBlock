package net.swofty.types.generic.block.placement.rules;

import net.minestom.server.instance.Instance;
import net.minestom.server.instance.block.Block;
import net.minestom.server.instance.block.rule.BlockPlacementRule;
import net.swofty.types.generic.block.placement.states.state.ChestType;
import net.swofty.types.generic.block.placement.states.state.Facing;
import net.swofty.types.generic.block.placement.BlockUtils;
import net.swofty.types.generic.block.placement.PlacementRule;
import net.swofty.types.generic.block.placement.states.BlockState;

public class ChestBlockPlacement extends PlacementRule {
    public ChestBlockPlacement(Block block) {
        super(block);
    }

    public static Facing getDirectionToAttached(BlockState state) {
        Facing direction = state.get(Facing.class);
        return state.get(ChestType.class) == ChestType.LEFT ? direction.rotateY() : direction.rotateYCCW();
    }

    @Override
    public boolean canPlace(BlockState blockState, BlockPlacementRule.PlacementState placementState) {
        return true;
    }

    @Override
    public boolean canUpdate(BlockState blockState, BlockPlacementRule.UpdateState updateState) {
        return true;
    }

    @Override
    public void update(BlockState blockState, BlockPlacementRule.UpdateState updateState) {
        Instance instance = (Instance) updateState.instance();
        var blockPosition = updateState.blockPosition();
        BlockUtils selfBlock = new BlockUtils(instance, blockPosition);
        Facing facing = blockState.get(Facing.class);

        ChestType chestType = blockState.get(ChestType.SINGLE);

        if (chestType == ChestType.SINGLE) {
            switch (facing) {
                case NORTH, SOUTH -> {
                    BlockUtils eastBlock = selfBlock.east();
                    BlockUtils westBlock = selfBlock.west();

                    BlockState eastState = eastBlock.getState();
                    BlockState westState = westBlock.getState();

                    if (isSameChest(eastBlock)) {
                        if (eastState.get(Facing.class) == facing && eastState.get(ChestType.class) != ChestType.SINGLE) {
                            ChestType eastType = eastState.get(ChestType.class);
                            Facing facing1 = getDirectionToAttached(eastState);
                            if (eastBlock.getRelativeTo(facing1).position().equals(blockPosition)) {
                                blockState.set(eastType.opposite());
                            }
                        }
                    }

                    if (isSameChest(westBlock)) {
                        if (westState.get(Facing.class) == facing && westState.get(ChestType.class) != ChestType.SINGLE) {
                            ChestType westType = westState.get(ChestType.class);
                            Facing facing1 = getDirectionToAttached(westState);
                            if (westBlock.getRelativeTo(facing1).position().equals(blockPosition)) {
                                blockState.set(westType.opposite());
                            }
                        }
                    }
                }
                case EAST, WEST -> {
                    BlockUtils northBlock = selfBlock.north();
                    BlockUtils southBlock = selfBlock.south();

                    BlockState northState = northBlock.getState();
                    BlockState southState = southBlock.getState();

                    if (isSameChest(northBlock)) {
                        if (northState.get(Facing.class) == facing && northState.get(ChestType.class) != ChestType.SINGLE) {
                            ChestType northType = northState.get(ChestType.class);
                            Facing facing1 = getDirectionToAttached(northState);
                            if (northBlock.getRelativeTo(facing1).position().equals(blockPosition)) {
                                blockState.set(northType.opposite());
                            }
                        }
                    }

                    if (isSameChest(southBlock)) {
                        if (southState.get(Facing.class) == facing && southState.get(ChestType.class) != ChestType.SINGLE) {
                            ChestType southType = southState.get(ChestType.class);
                            Facing facing1 = getDirectionToAttached(southState);
                            if (southBlock.getRelativeTo(facing1).position().equals(blockPosition)) {
                                blockState.set(southType.opposite());
                            }
                        }
                    }
                }
            }
        } else {
            Facing attachedFacing = getDirectionToAttached(blockState);
            BlockUtils attachedBlock = selfBlock.getRelativeTo(attachedFacing);

            if (!isSameChest(attachedBlock)) {
                blockState.set(ChestType.SINGLE);
            }
        }
    }

    @Override
    public void place(BlockState blockState, BlockPlacementRule.PlacementState placementState) {
        Instance instance = (Instance) placementState.instance();
        var blockPosition = placementState.placePosition();
        ChestType chestType = ChestType.SINGLE;

        boolean sneaking = placementState.isPlayerShifting();
        Facing facing = blockState.get(Facing.class);
        BlockUtils alsBlock = new BlockUtils(instance, blockPosition);

        if (!sneaking) {
            if (facing == getDirectionToAttach(alsBlock, facing.rotateY())) {
                chestType = ChestType.LEFT;
            } else if (facing == getDirectionToAttach(alsBlock, facing.rotateYCCW())) {
                chestType = ChestType.RIGHT;
            }
        }

        blockState.set(chestType);
    }

    private Facing getDirectionToAttach(BlockUtils block, Facing directional) {
        BlockUtils relative = block.getRelativeTo(directional.getRelativeX(), directional.getRelativeY(), directional.getRelativeZ());
        BlockState blockState = relative.getState();
        return isSameChest(relative.getBlock()) && blockState.get(ChestType.class) == ChestType.SINGLE
                ? blockState.get(Facing.class)
                : null;
    }

    public boolean isSameChest(Block block) {
        return block == block();
    }

    public boolean isSameChest(BlockUtils block) {
        return isSameChest(block.getBlock());
    }
}
