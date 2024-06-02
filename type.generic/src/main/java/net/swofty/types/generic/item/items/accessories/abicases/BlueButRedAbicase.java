package net.swofty.types.generic.item.items.accessories.abicases;

import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.CustomDisplayName;
import net.swofty.types.generic.item.impl.NotFinishedYet;
import net.swofty.types.generic.item.impl.Talisman;
import net.swofty.types.generic.user.SkyBlockPlayer;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class BlueButRedAbicase implements Talisman, CustomDisplayName, NotFinishedYet {

    @Override
    public String getSkullTexture(@Nullable SkyBlockPlayer player, SkyBlockItem item) {
        return "85e4f9da68c81fa481eecdca48a138cecde2cddffeeae84ab1afd24a363e028";
    }

    @Override
    public List<String> getTalismanDisplay() {
        return List.of(
                "§7Combat Wisdom: §a+1.5",
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
        return "Blue™ but Red Abicase";
    }
}
