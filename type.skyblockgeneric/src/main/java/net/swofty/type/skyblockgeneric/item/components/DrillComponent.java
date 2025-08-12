package net.swofty.type.skyblockgeneric.item.components;

import net.swofty.commons.item.reforge.ReforgeType;
import net.swofty.type.generic.item.SkyBlockItemComponent;
import net.swofty.type.generic.utility.groups.EnchantItemGroups;

import java.util.List;

public class DrillComponent extends SkyBlockItemComponent {
    public DrillComponent() {
        addInheritedComponent(new EnchantableComponent(List.of(EnchantItemGroups.DRILL), true));
        addInheritedComponent(new ReforgableComponent(ReforgeType.PICKAXES));
        addInheritedComponent(new ExtraRarityComponent("DRILL"));
        addInheritedComponent(new TrackedUniqueComponent());
    }
}