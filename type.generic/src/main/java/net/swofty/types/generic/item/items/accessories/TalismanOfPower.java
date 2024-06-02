package net.swofty.types.generic.item.items.accessories;

import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.NotFinishedYet;
import net.swofty.types.generic.item.impl.Talisman;
import net.swofty.types.generic.user.SkyBlockPlayer;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class TalismanOfPower implements Talisman , NotFinishedYet {
    @Override
    public List<String> getTalismanDisplay() {
        return List.of("§7Allows you to harness the power",
                "§7of §a2§7 Gemstones, although",
                "§7only half of the normal power",
                "§7can be harnessed!");
    }

    @Override
    public String getSkullTexture(@Nullable SkyBlockPlayer player, SkyBlockItem item) {
        return "c6736fd95d3a6a4aaac46709a07aec7f1c38f0a3aae573e6f483388819412b65";
    }
}
