package net.swofty.types.generic.item.items.accessories.dungeon;

import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.NotFinishedYet;
import net.swofty.types.generic.item.impl.Talisman;
import net.swofty.types.generic.user.SkyBlockPlayer;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class CatacombsExpertRing implements Talisman, NotFinishedYet {
    @Override
    public List<String> getTalismanDisplay() {
        return List.of("§7Increases §cCatacombs §7dungeon",
                "§7experience by §a+10%§7.");
    }

    @Override
    public String getSkullTexture(@Nullable SkyBlockPlayer player, SkyBlockItem item) {
        return "c078c68f6f9669370ea39be72945a3a8688e0e024e5d6158fd854fb2b80fb";
    }
}
