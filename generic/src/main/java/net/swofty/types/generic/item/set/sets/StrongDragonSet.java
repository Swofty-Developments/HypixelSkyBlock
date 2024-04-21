package net.swofty.types.generic.item.set.sets;

import net.swofty.types.generic.item.set.impl.ArmorSet;

import java.util.Arrays;
import java.util.List;

public class StrongDragonSet implements ArmorSet {

    @Override
    public String getName() {
        return "Strong Blood";
    }

    @Override
    public List<String> getDescription() {
        return Arrays.asList(
                "§7Improves §9Aspect of the End",
                "§8⋗§c +75 Damage",
                "",
                "§7Instant Transmission:",
                "§8⋗ §a+2 teleport range",
                "§8⋗ §a+3 seconds",
                "§8⋗ §c+5 ❁ Strength §7on cast.");
    }
}
