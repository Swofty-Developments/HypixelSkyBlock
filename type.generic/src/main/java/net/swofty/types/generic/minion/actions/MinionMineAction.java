package net.swofty.types.generic.minion.actions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.instance.Instance;
import net.minestom.server.instance.block.Block;
import net.minestom.server.item.Material;
import net.swofty.types.generic.item.MaterialQuantifiable;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.minion.IslandMinionData;
import net.swofty.types.generic.minion.MinionAction;
import net.swofty.types.generic.utility.MathUtility;

import java.util.List;

@AllArgsConstructor
@Getter
public class MinionMineAction extends MinionAction {
    private final Block toMine;

    @Override
    public SkyBlockItem onAction(MinionActionEvent event, IslandMinionData.IslandMinion minion, Instance island) {
        List<Pos> minePositions = MathUtility.getRangeExcludingSelf(
                minion.getPosition().sub(0, 1, 0), 2 + minion.getBonusRange()
        );

        boolean hasAir = minePositions.stream().anyMatch(pos -> island.getBlock(pos).isAir());

        List<Pos> possiblePositions = MathUtility.getRangeExcludingSelf(
                minion.getPosition().sub(0, 1, 0), 2 + minion.getBonusRange()
        ).stream().filter(pos -> !hasAir || island.getBlock(pos).isAir()).toList();

        event.setToLook(MathUtility.getRandomElement(possiblePositions));

        event.setAction(() -> {
            if (island.getBlock(event.getToLook()) == toMine || island.getBlock(event.getToLook()).isAir()) {
                if (hasAir) {
                    minion.getMinionEntity().placeAnimation();
                    island.setBlock(event.getToLook(), toMine);
                    return;
                }

                minion.getMinionEntity().swingAnimation();
                island.setBlock(event.getToLook(), Block.AIR);
            }
        });

        return hasAir ? null : new SkyBlockItem(Material.fromNamespaceId(toMine.namespace()));
    }

    @Override
    public boolean checkMaterials(IslandMinionData.IslandMinion minion, Instance island) {
        List<MaterialQuantifiable> items = minion.getItemsInMinion();

        if (items.stream().noneMatch(item -> {
            return item.getMaterial().material == Material.fromNamespaceId(toMine.namespace());
        })) {
            return true;
        }

        boolean shouldAllow = false;
        for (MaterialQuantifiable item : items) {
            if (item.getMaterial().material == Material.fromNamespaceId(toMine.namespace())) {
                if (item.getAmount() != 64) {
                    shouldAllow = true;
                }
            }
        }

        return !shouldAllow;
    }
}
