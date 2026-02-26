package net.swofty.type.skyblockgeneric.item.components;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import net.minestom.server.coordinate.BlockVec;
import net.swofty.type.skyblockgeneric.item.SkyBlockItemComponent;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;

import java.util.HashMap;

public class RegionSelectorComponent extends SkyBlockItemComponent {

    @Getter
    public static final HashMap<SkyBlockPlayer, SelectedRegion> playerRegionSelection = new HashMap<>();

    @Getter
    @Setter
    @AllArgsConstructor
    public static class SelectedRegion {
        private BlockVec pos1, pos2;
    }

    public RegionSelectorComponent() {
        addInheritedComponent(new TrackedUniqueComponent());
    }

}
