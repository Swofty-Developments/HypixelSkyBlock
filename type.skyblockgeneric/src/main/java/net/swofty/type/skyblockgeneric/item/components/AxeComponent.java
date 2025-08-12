package net.swofty.type.skyblockgeneric.item.components;

import net.swofty.commons.item.reforge.ReforgeType;
import net.swofty.type.generic.item.SkyBlockItemComponent;
import net.swofty.type.generic.utility.groups.EnchantItemGroups;

import java.util.List;

public class AxeComponent extends SkyBlockItemComponent {
    public AxeComponent() {
        addInheritedComponent(new ExtraRarityComponent("AXE"));
        addInheritedComponent(new ReforgableComponent(ReforgeType.AXES));
        addInheritedComponent(new EnchantableComponent(List.of(EnchantItemGroups.TOOLS), true));
        addInheritedComponent(new TrackedUniqueComponent());
    }
}