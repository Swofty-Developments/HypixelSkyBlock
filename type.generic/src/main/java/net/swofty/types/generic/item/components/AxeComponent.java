package net.swofty.types.generic.item.components;

import net.swofty.commons.item.ReforgeType;
import net.swofty.types.generic.item.SkyBlockItemComponent;
import net.swofty.types.generic.utility.groups.EnchantItemGroups;

import java.util.List;

public class AxeComponent extends SkyBlockItemComponent {
    public AxeComponent() {
        addInheritedComponent(new ExtraRarityComponent("AXE"));
        addInheritedComponent(new ReforgableComponent(ReforgeType.AXES));
        addInheritedComponent(new EnchantableComponent(List.of(EnchantItemGroups.TOOLS), true));
        addInheritedComponent(new TrackedUniqueComponent());
    }
}