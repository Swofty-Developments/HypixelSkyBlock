package net.swofty.types.generic.item.components;

import net.swofty.types.generic.item.SkyBlockItemComponent;
import net.swofty.types.generic.utility.groups.EnchantItemGroups;

import java.util.List;

public class EnchantableComponent extends SkyBlockItemComponent {
    private final List<EnchantItemGroups> enchantGroups;
    private final boolean showEnchantLores;

    public EnchantableComponent(List<EnchantItemGroups> enchants, boolean showEnchantLores) {
        this.enchantGroups = enchants;
        this.showEnchantLores = showEnchantLores;
    }

    public List<EnchantItemGroups> getEnchantItemGroups() {
        return enchantGroups;
    }

    public boolean showEnchantLores() {
        return showEnchantLores;
    }
}