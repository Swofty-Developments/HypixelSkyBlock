package net.swofty.types.generic.item.components;

import net.swofty.commons.item.ReforgeType;
import net.swofty.types.generic.item.SkyBlockItemComponent;
import net.swofty.types.generic.utility.groups.EnchantItemGroups;

import java.util.List;

public class DrillComponent extends SkyBlockItemComponent {
    public DrillComponent() {
        addInheritedComponent(new EnchantableComponent(List.of(EnchantItemGroups.DRILL), true));
        addInheritedComponent(new ReforgableComponent(ReforgeType.PICKAXES));
        addInheritedComponent(new ExtraRarityComponent("DRILL"));
    }
}