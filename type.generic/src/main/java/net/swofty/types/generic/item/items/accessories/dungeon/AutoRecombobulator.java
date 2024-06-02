package net.swofty.types.generic.item.items.accessories.dungeon;

import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.NotFinishedYet;
import net.swofty.types.generic.item.impl.Talisman;
import net.swofty.types.generic.user.SkyBlockPlayer;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class AutoRecombobulator implements Talisman, NotFinishedYet {
    @Override
    public List<String> getTalismanDisplay() {
        return List.of("§7Grants a §a1% §7chance to",
                "automatically recombobulate",
                "drops you get from enemies.");
    }

    @Override
    public String getSkullTexture(@Nullable SkyBlockPlayer player, SkyBlockItem item) {
        return "5dff8dbbab15bfbb11e23b1f50b34ef548ad9832c0bd7f5a13791adad0057e1b";
    }
}
