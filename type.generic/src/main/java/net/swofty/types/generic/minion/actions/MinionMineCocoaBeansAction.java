package net.swofty.types.generic.minion.actions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.minestom.server.instance.Instance;
import net.minestom.server.instance.block.Block;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.minion.IslandMinionData;
import net.swofty.types.generic.minion.MinionAction;

@AllArgsConstructor
@Getter
public class MinionMineCocoaBeansAction extends MinionAction {
    private final Block toMine;

    @Override
    public SkyBlockItem onAction(MinionActionEvent event, IslandMinionData.IslandMinion minion, Instance island) {
        return null;
    }

    @Override
    public boolean checkMaterials(IslandMinionData.IslandMinion minion, Instance island) {
        return false;
    }
}
