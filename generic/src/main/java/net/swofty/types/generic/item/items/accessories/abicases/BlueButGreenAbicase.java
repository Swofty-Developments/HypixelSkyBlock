package net.swofty.types.generic.item.items.accessories.abicases;

import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.CustomDisplayName;
import net.swofty.types.generic.item.impl.NotFinishedYet;
import net.swofty.types.generic.item.impl.Talisman;
import net.swofty.types.generic.user.SkyBlockPlayer;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class BlueButGreenAbicase implements Talisman, CustomDisplayName, NotFinishedYet {

    @Override
    public String getSkullTexture(@Nullable SkyBlockPlayer player, SkyBlockItem item) {
        return "a9909a9779b946b9787442fa483af4de4b2f19fd40dc2370f7a9b8f521f21ddc";
    }

    @Override
    public List<String> getTalismanDisplay() {
        return List.of(
                "§7Foraging Wisdom: §a+1.5",
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
        return "Blue™ but Green Abicase";
    }
}
