package net.swofty.types.generic.item.set.sets;

import net.swofty.types.generic.item.set.impl.ArmorSet;

import java.util.Arrays;
import java.util.List;

public class CheapTuxedoSet implements ArmorSet {
    @Override
    public String getName() {
        return "Dashing";
    }

    @Override
    public List<String> getDescription() {
        return Arrays.asList(
                "§7Max health set to §c75♥§7.",
                "§7Deal §c+50% §7damage!",
                "§8Very stylish.");
    }
}
