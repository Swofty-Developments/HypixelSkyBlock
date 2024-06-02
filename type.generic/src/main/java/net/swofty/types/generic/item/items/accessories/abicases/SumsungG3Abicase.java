package net.swofty.types.generic.item.items.accessories.abicases;

import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.CustomDisplayName;
import net.swofty.types.generic.item.impl.NotFinishedYet;
import net.swofty.types.generic.item.impl.Talisman;
import net.swofty.types.generic.user.SkyBlockPlayer;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class SumsungG3Abicase implements Talisman, CustomDisplayName, NotFinishedYet {

    @Override
    public String getSkullTexture(@Nullable SkyBlockPlayer player, SkyBlockItem item) {
        return "70ce32b90e16419319daf35e6ad01bb715d898548b9ef66dd799a00107e87376";
    }

    @Override
    public List<String> getTalismanDisplay() {
        return List.of(
                "§7Adds §a+1 §7contact slot.",
                "",
                "§7Grants §6+1 Magical Power §7per §a2",
                "§7contacts in your Abiphone.",
                "",
                "§8Only ONE case will have an effect",
                "§8while in accessory bag."
        );
    }

    @Override
    public String getDisplayName(@Nullable SkyBlockItem item) {
        return "Sumsung© G3 Abicase";
    }
}
