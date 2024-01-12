package net.swofty.types.generic.minion;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import net.minestom.server.instance.Instance;
import net.swofty.types.generic.user.SkyBlockIsland;

public abstract class MinionAction {

    public abstract void onAction(IslandMinionData.IslandMinion minion, Instance island);
    public abstract boolean checkMaterials(IslandMinionData.IslandMinion minion, Instance island);

}
