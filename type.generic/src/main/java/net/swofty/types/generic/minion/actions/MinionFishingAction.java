package net.swofty.types.generic.minion.actions;

import lombok.Getter;
import net.minestom.server.instance.Instance;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.minion.IslandMinionData;
import net.swofty.types.generic.minion.MinionAction;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@Getter
public class MinionFishingAction extends MinionAction {

    @Override
    public @NotNull List<SkyBlockItem> onAction(MinionActionEvent event, IslandMinionData.IslandMinion minion, Instance island) {
        return null;
    }

    @Override
    public boolean checkMaterials(IslandMinionData.IslandMinion minion, Instance island) {
        return false;
    }
}
