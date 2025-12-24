package net.swofty.type.skyblockgeneric.item.components;

import net.swofty.commons.skyblock.item.reforge.ReforgeType;
import net.swofty.type.skyblockgeneric.item.SkyBlockItemComponent;
import net.swofty.type.skyblockgeneric.utility.groups.EnchantItemGroups;

import java.util.List;

public class DrillComponent extends SkyBlockItemComponent {
    public DrillComponent() {
        addInheritedComponent(new EnchantableComponent(List.of(EnchantItemGroups.DRILL), true));
        addInheritedComponent(new ReforgableComponent(ReforgeType.PICKAXES));
        addInheritedComponent(new ExtraRarityComponent("DRILL"));
        addInheritedComponent(new TrackedUniqueComponent());
    }
}