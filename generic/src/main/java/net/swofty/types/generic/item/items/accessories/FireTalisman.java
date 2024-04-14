package net.swofty.types.generic.item.items.accessories;

import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.NotFinishedYet;
import net.swofty.types.generic.item.impl.Talisman;
import net.swofty.types.generic.user.SkyBlockPlayer;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class FireTalisman implements Talisman, NotFinishedYet {
    @Override
    public String getSkullTexture(@Nullable SkyBlockPlayer player, SkyBlockItem item) {
        return "9af328c87b068509aca9834eface197705fe5d4f0871731b7b21cd99b9fddc";
    }

    @Override
    public List<String> getTalismanDisplay() {
        return List.of("§7Provides immunity against damage",
                "§7from §cFire§7.",
                "",
                "§7While in the §cCrimson Isle§7, grants",
                "§7a §a20% §7damage reduction instead."
        );
    }
}
