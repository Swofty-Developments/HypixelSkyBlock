package net.swofty.types.generic.minion.actions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.minestom.server.instance.Instance;
import net.minestom.server.instance.block.Block;
import net.minestom.server.item.Material;
import net.swofty.types.generic.item.MaterialQuantifiable;
import net.swofty.types.generic.minion.IslandMinionData;
import net.swofty.types.generic.minion.MinionAction;
import net.swofty.types.generic.user.SkyBlockIsland;

import java.util.List;

@AllArgsConstructor
@Getter
public class MinionMineAction extends MinionAction {
    private final Block toMine;

    @Override
    public void onAction(IslandMinionData.IslandMinion minion, Instance island) {

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

        return shouldAllow;
    }
}
