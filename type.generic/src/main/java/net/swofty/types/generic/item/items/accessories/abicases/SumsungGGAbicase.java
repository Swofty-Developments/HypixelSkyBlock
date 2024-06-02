package net.swofty.types.generic.item.items.accessories.abicases;

import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.CustomDisplayName;
import net.swofty.types.generic.item.impl.NotFinishedYet;
import net.swofty.types.generic.item.impl.Talisman;
import net.swofty.types.generic.user.SkyBlockPlayer;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class SumsungGGAbicase implements Talisman, CustomDisplayName, NotFinishedYet {

    @Override
    public String getSkullTexture(@Nullable SkyBlockPlayer player, SkyBlockItem item) {
        return "36a10ee2155fc0134d9392000a9eb9ebcba8526eff3893e54434e825e558fb55";
    }

    @Override
    public List<String> getTalismanDisplay() {
        return List.of(
                "§7Adds §a+2 §7contact slots.",
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
        return "Sumsung© GG Abicase";
    }
}
