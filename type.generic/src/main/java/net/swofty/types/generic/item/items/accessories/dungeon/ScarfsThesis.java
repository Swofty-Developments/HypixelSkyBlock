package net.swofty.types.generic.item.items.accessories.dungeon;

import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.NotFinishedYet;
import net.swofty.types.generic.item.impl.Talisman;
import net.swofty.types.generic.user.SkyBlockPlayer;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ScarfsThesis implements Talisman, NotFinishedYet {
    @Override
    public List<String> getTalismanDisplay() {
        return List.of("§7Gain dungeon class experience",
                "§a+4% faster.");
    }

    @Override
    public String getSkullTexture(@Nullable SkyBlockPlayer player, SkyBlockItem item) {
        return "8ce4c87eb4dde27459e3e7f85921e7e57b11199260caa5ce63f139ee3d188c";
    }
}
