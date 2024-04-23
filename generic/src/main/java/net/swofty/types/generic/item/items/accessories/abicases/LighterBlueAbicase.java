package net.swofty.types.generic.item.items.accessories.abicases;

import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.CustomDisplayName;
import net.swofty.types.generic.item.impl.NotFinishedYet;
import net.swofty.types.generic.item.impl.Talisman;
import net.swofty.types.generic.user.SkyBlockPlayer;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class LighterBlueAbicase implements Talisman, CustomDisplayName, NotFinishedYet {

    @Override
    public String getSkullTexture(@Nullable SkyBlockPlayer player, SkyBlockItem item) {
        return "df70fab3246fe027ce0bba885a73c6e82d8ff8f358231e8461f956560cfa58f";
    }

    @Override
    public List<String> getTalismanDisplay() {
        return List.of(
                "§7Fishing Wisdom: §a+1.5",
                "",
                "§7§7Grants §6+1 Magical Power",
                "§6§7per §a2 §7contacts in your",
                "§7Abiphone.",
                "",
                "§7§8Only ONE case will have an effect",
                "§8while in accessory bag."
        );
    }

    @Override
    public String getDisplayName(@Nullable SkyBlockItem item) {
        return "Lighter Blue™ Abicase";
    }
}
