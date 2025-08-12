package net.swofty.type.skyblockgeneric.item.components;

import net.swofty.type.generic.item.SkyBlockItemComponent;
import net.swofty.type.generic.utility.groups.EnchantItemGroups;

import java.util.List;

public class ShovelComponent extends SkyBlockItemComponent {
    public ShovelComponent() {
        addInheritedComponent(new ExtraRarityComponent("SHOVEL"));
        addInheritedComponent(new EnchantableComponent(List.of(EnchantItemGroups.TOOLS), true));
        addInheritedComponent(new TrackedUniqueComponent());
    }
}