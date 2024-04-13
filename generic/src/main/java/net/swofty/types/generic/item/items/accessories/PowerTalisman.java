package net.swofty.types.generic.item.items.accessories;

import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.NotFinishedYet;
import net.swofty.types.generic.item.impl.Talisman;
import net.swofty.types.generic.user.SkyBlockPlayer;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class PowerTalisman implements Talisman , NotFinishedYet {
    @Override
    public List<String> getTalismanDisplay() {
        return List.of("§7Allows you to harness the power",
                "§7of §a2§7 Gemstones, although",
                "§7only half of the normal power",
                "§7can be harnessed!");
    }

    @Override
    public String getSkullTexture(@Nullable SkyBlockPlayer player, SkyBlockItem item) {
        return "8b89456a-6cf8-3879-b987-fb9e662db718";
    }
}
