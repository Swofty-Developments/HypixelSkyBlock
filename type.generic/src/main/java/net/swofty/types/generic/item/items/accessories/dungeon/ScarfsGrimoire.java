package net.swofty.types.generic.item.items.accessories.dungeon;

import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.NotFinishedYet;
import net.swofty.types.generic.item.impl.Talisman;
import net.swofty.types.generic.user.SkyBlockPlayer;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ScarfsGrimoire implements Talisman, NotFinishedYet {
    @Override
    public List<String> getTalismanDisplay() {
        return List.of("§7Gain dungeon class experience",
                "§a+6% faster.");
    }

    @Override
    public String getSkullTexture(@Nullable SkyBlockPlayer player, SkyBlockItem item) {
        return "bafb195cc75f31b619a077b7853653254ac18f220dc32d1412982ff437b4d57a";
    }
}
