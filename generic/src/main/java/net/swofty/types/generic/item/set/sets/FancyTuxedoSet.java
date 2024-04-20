package net.swofty.types.generic.item.set.sets;

import net.swofty.types.generic.item.set.impl.ArmorSet;

public class FancyTuxedoSet implements ArmorSet {
    @Override
    public String getName() {
        return "Dashing";
    }

    @Override
    public String getDescription() {
        return """
                §7Max health set to §c150♥§7.
                §7Deal §c+100% §7damage!
                §8Very stylish.""";
    }
}
