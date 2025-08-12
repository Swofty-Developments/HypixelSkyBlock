package net.swofty.type.skyblockgeneric.minion.actions;

import lombok.Getter;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.instance.Instance;
import net.minestom.server.instance.block.Block;
import net.minestom.server.item.Material;
import net.minestom.server.tag.Tag;
import net.swofty.type.generic.item.ItemQuantifiable;
import net.swofty.type.generic.item.SkyBlockItem;
import net.swofty.type.generic.minion.IslandMinionData;
import net.swofty.type.generic.minion.MinionAction;
import net.swofty.type.generic.utility.MathUtility;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

@Getter
public class MinionMineCocoaBeansAction extends MinionAction {

    @Override
    public @NotNull List<SkyBlockItem> onAction(MinionActionEvent event, IslandMinionData.IslandMinion minion, Instance island) {
        List<Pos> logPositions = getLogPositions(minion, island);
        List<Pos> cocoaPositions = getCocoaPositions(minion, island);

        boolean allLogPositionsAreFilled = logPositions.stream().allMatch(pos -> island.getBlock(pos).equals(Block.JUNGLE_LOG));
        if (!allLogPositionsAreFilled) {
            Pos nonLogPosition = logPositions.stream().filter(pos -> !island.getBlock(pos).equals(Block.JUNGLE_LOG)).findFirst().orElse(null);
            if (nonLogPosition == null) throw new IllegalStateException("We should never get here");

            // We need to place a log down, we'll put it in the air pos
            event.setToLook(nonLogPosition);
            event.setAction(() -> {
                minion.getMinionEntity().placeAnimation();
                island.setBlock(nonLogPosition, Block.JUNGLE_LOG);
            });
            return new ArrayList<>();
        }

        boolean allCocoaPositionsAreFilled = cocoaPositions.stream().allMatch(pos -> island.getBlock(pos).equals(Block.COCOA));
        if (!allCocoaPositionsAreFilled) {
            Pos nonCocoaPosition = cocoaPositions.stream().filter(pos -> !island.getBlock(pos).equals(Block.COCOA)).findFirst().orElse(null);
            if (nonCocoaPosition == null) throw new IllegalStateException("We should never get here");

            Pos closestLog = logPositions.stream().min((o1, o2) -> {
                return (int) Math.abs(o1.distance(nonCocoaPosition) - o2.distance(nonCocoaPosition));
            }).orElse(null);
            if (closestLog == null) throw new IllegalStateException("We should never get here");

            event.setToLook(nonCocoaPosition);
            event.setAction(() -> {
                minion.getMinionEntity().swingAnimation();
                Block blockToPlace = Block.COCOA
                        .withTag(Tag.Integer("age"), 3)
                        .withTag(Tag.String("facing"), MathUtility.getDirectionFromPositions(nonCocoaPosition, closestLog).name().toLowerCase());
                island.setBlock(nonCocoaPosition, blockToPlace);
            });
            return new ArrayList<>();
        }

        Pos randomCocoaPosition = MathUtility.getRandomElement(cocoaPositions);
        event.setToLook(randomCocoaPosition);
        event.setAction(() -> {
            minion.getMinionEntity().swingAnimation();
            island.setBlock(randomCocoaPosition, Block.AIR);
        });

        return List.of(new SkyBlockItem(Material.COCOA_BEANS));
    }

    @Override
    public boolean checkMaterials(IslandMinionData.IslandMinion minion, Instance island) {
        List<ItemQuantifiable> items = minion.getItemsInMinion();

        if (items.stream().noneMatch(item -> {
            return item.getItem().getMaterial() == Material.COCOA_BEANS;
        })) {
            return true;
        }

        boolean shouldAllow = false;
        for (ItemQuantifiable item : items) {
            if (item.getItem().getMaterial() == Material.COCOA_BEANS) {
                if (item.getAmount() != 64) {
                    shouldAllow = true;
                }
            }
        }

        return !shouldAllow;
    }

    private List<Pos> getLogPositions(IslandMinionData.IslandMinion minion, Instance island) {
        Pos minionPos = minion.getPosition();

        List<Pos> possibleLogPositions = new ArrayList<>();
        possibleLogPositions.add(minionPos.add(-2, 0, -2));
        possibleLogPositions.add(minionPos.add(-2, 0, 0));
        possibleLogPositions.add(minionPos.add(-2, 0, 2));

        possibleLogPositions.add(minionPos.add(2, 0, -2));
        possibleLogPositions.add(minionPos.add(2, 0, 0));
        possibleLogPositions.add(minionPos.add(2, 0, 2));

        possibleLogPositions.add(minionPos.add(0, 0, 2));
        possibleLogPositions.add(minionPos.add(0, 0, -2));

        return possibleLogPositions;
    }

    private List<Pos> getCocoaPositions(IslandMinionData.IslandMinion minion, Instance island) {
        Pos minionPos = minion.getPosition();
        List<Pos> possibleCocoaPositions = new ArrayList<>();

        possibleCocoaPositions.add(minionPos.add(1, 0, 0));
        possibleCocoaPositions.add(minionPos.add(-1, 0, 0));
        possibleCocoaPositions.add(minionPos.add(0, 0, 1));
        possibleCocoaPositions.add(minionPos.add(0, 0, -1));

        possibleCocoaPositions.add(minionPos.add(2, 0, -1));
        possibleCocoaPositions.add(minionPos.add(2, 0, 1));

        possibleCocoaPositions.add(minionPos.add(-2, 0, -1));
        possibleCocoaPositions.add(minionPos.add(-2, 0, 1));

        possibleCocoaPositions.add(minionPos.add(-1, 0, 2));
        possibleCocoaPositions.add(minionPos.add(1, 0, 2));

        possibleCocoaPositions.add(minionPos.add(1, 0, -2));
        possibleCocoaPositions.add(minionPos.add(-1, 0, -2));

        return possibleCocoaPositions;
    }
}
