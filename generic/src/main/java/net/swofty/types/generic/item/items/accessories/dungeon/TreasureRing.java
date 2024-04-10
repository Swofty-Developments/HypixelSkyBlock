package net.swofty.types.generic.item.items.accessories.dungeon;

import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.NotFinishedYet;
import net.swofty.types.generic.item.impl.Talisman;
import net.swofty.types.generic.user.SkyBlockPlayer;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class TreasureRing implements Talisman, NotFinishedYet {
    @Override
    public List<String> getTalismanDisplay() {
        return List.of("§7Grants §a+2% §7extra loot to end",
                "§7of dungeon chests.");
    }

    @Override
    public String getSkullTexture(@Nullable SkyBlockPlayer player, SkyBlockItem item) {
        return "6a1cc5525a217a399b5b86c32f0f22dd91378874b5f44d5a383e18bc0f3bc301";
    }
}
