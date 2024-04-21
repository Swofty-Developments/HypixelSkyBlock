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
        return "c078f3f24b1760f9d4abb0851eb8f6c7dfff8855708e6049d15dc02042ba8436";
    }
}
