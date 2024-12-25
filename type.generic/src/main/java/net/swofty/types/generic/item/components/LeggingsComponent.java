package net.swofty.types.generic.item.components;

import net.swofty.commons.item.ReforgeType;
import net.swofty.types.generic.item.SkyBlockItemComponent;
import net.swofty.types.generic.utility.groups.EnchantItemGroups;

import java.util.List;

public class LeggingsComponent extends SkyBlockItemComponent {
    public LeggingsComponent() {
        addInheritedComponent(new ExtraRarityComponent("LEGGINGS"));
        addInheritedComponent(new ReforgableComponent(ReforgeType.ARMOR));
        addInheritedComponent(new EnchantableComponent(List.of(EnchantItemGroups.ARMOR), true));
    }
}