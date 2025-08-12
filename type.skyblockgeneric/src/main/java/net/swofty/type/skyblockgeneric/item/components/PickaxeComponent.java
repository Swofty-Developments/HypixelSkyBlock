package net.swofty.type.skyblockgeneric.item.components;

import net.swofty.commons.item.reforge.ReforgeType;
import net.swofty.type.generic.item.SkyBlockItemComponent;
import net.swofty.type.generic.utility.groups.EnchantItemGroups;

import java.util.List;

public class PickaxeComponent extends SkyBlockItemComponent {
    public PickaxeComponent() {
        addInheritedComponent(new EnchantableComponent(List.of(EnchantItemGroups.PICKAXE), true));
        addInheritedComponent(new ReforgableComponent(ReforgeType.PICKAXES));
        addInheritedComponent(new ExtraRarityComponent("PICKAXE"));
        addInheritedComponent(new TrackedUniqueComponent());
    }
}