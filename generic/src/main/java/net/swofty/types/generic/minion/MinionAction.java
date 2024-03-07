package net.swofty.types.generic.minion;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.instance.Instance;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.user.SkyBlockIsland;

public abstract class MinionAction {

    public abstract SkyBlockItem onAction(MinionActionEvent event, IslandMinionData.IslandMinion minion, Instance island);
    public abstract boolean checkMaterials(IslandMinionData.IslandMinion minion, Instance island);

    @Getter
    @Setter
    public static class MinionActionEvent {
        private Pos toLook;
        private Runnable action;
    }

    public static void onMinionIteration(IslandMinionData.IslandMinion islandMinion,
                                             SkyBlockMinion minion,
                                             SkyBlockItem item) {
        islandMinion.addItem(item);
    }
}
