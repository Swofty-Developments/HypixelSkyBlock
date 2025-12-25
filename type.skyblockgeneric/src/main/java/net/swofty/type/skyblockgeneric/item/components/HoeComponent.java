package net.swofty.type.skyblockgeneric.item.components;

import net.swofty.commons.skyblock.item.reforge.ReforgeType;
import net.swofty.type.skyblockgeneric.item.SkyBlockItemComponent;
import net.swofty.type.skyblockgeneric.utility.groups.EnchantItemGroups;

import java.util.List;

/**
 * Component for hoe tools.
 * Placeholder for future farming speed bonuses.
 */
public class HoeComponent extends SkyBlockItemComponent {
    public HoeComponent() {
        addInheritedComponent(new ExtraRarityComponent("HOE"));
        addInheritedComponent(new ReforgableComponent(ReforgeType.HOES));
        addInheritedComponent(new EnchantableComponent(List.of(EnchantItemGroups.TOOLS), true));
        addInheritedComponent(new TrackedUniqueComponent());
    }
}
