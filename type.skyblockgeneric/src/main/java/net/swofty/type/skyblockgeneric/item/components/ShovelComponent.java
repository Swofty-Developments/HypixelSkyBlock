package net.swofty.type.skyblockgeneric.item.components;

import net.swofty.type.skyblockgeneric.item.SkyBlockItemComponent;
import net.swofty.type.skyblockgeneric.utility.groups.EnchantItemGroups;

import java.util.List;

public class ShovelComponent extends SkyBlockItemComponent {
    public ShovelComponent() {
        addInheritedComponent(new ExtraRarityComponent("SHOVEL"));
        addInheritedComponent(new EnchantableComponent(List.of(EnchantItemGroups.TOOLS), true));
        addInheritedComponent(new TrackedUniqueComponent());
    }
}