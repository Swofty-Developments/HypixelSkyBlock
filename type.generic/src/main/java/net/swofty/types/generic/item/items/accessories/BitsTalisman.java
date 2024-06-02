package net.swofty.types.generic.item.items.accessories;

import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.NotFinishedYet;
import net.swofty.types.generic.item.impl.Talisman;
import net.swofty.types.generic.user.SkyBlockPlayer;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class BitsTalisman implements Talisman, NotFinishedYet {
    @Override
    public String getSkullTexture(@Nullable SkyBlockPlayer player, SkyBlockItem item) {
        return "2ebadb1725aa85bb2810d0b73bf7cd74db3d9d8fc61c4cf9e543dbcc199187cc";
    }

    @Override
    public List<String> getTalismanDisplay() {
        return List.of(
                "§7Whenever you gain bits, grants a §b10%",
                "§b§7chance to gain double!"
        );
    }
}
