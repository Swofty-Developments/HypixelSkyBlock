package net.swofty.types.generic.item.components;

import net.swofty.commons.statistics.ItemStatistics;
import net.swofty.types.generic.item.SkyBlockItemComponent;

public class AccessoryComponent extends SkyBlockItemComponent {
    public AccessoryComponent() {
        addInheritedComponent(new ExtraRarityComponent("ACCESSORY"));
        addInheritedComponent(new TrackedUniqueComponent());
    }
}
