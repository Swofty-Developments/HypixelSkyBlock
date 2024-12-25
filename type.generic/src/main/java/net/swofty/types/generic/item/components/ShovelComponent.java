package net.swofty.types.generic.item.components;

import net.swofty.types.generic.item.SkyBlockItemComponent;
import net.swofty.types.generic.utility.groups.EnchantItemGroups;

import java.util.List;

public class ShovelComponent extends SkyBlockItemComponent {
    public ShovelComponent() {
        addInheritedComponent(new ExtraRarityComponent("SHOVEL"));
        addInheritedComponent(new EnchantableComponent(List.of(EnchantItemGroups.TOOLS), true));
    }
}