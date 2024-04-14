package net.swofty.types.generic.item.items.accessories;

import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.NotFinishedYet;
import net.swofty.types.generic.item.impl.Talisman;
import net.swofty.types.generic.user.SkyBlockPlayer;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class LavaTalisman implements Talisman, NotFinishedYet {
    @Override
    public List<String> getTalismanDisplay() {
        return List.of("§7Provides immunity against damage",
                "§7from §omost §cLava§7.",
                "",
                "§7While in the §cCrimson Isle§7, grants",
                "§7a §a20% §7damage reduction instead.");
    }

    @Override
    public String getSkullTexture(@Nullable SkyBlockPlayer player, SkyBlockItem item) {
        return "1c73b35c-ecd3-3f79-a79a-c68e72841b56";
    }
}
