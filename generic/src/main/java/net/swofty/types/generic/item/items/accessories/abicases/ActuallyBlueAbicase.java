package net.swofty.types.generic.item.items.accessories.abicases;

import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.CustomDisplayName;
import net.swofty.types.generic.item.impl.NotFinishedYet;
import net.swofty.types.generic.item.impl.Talisman;
import net.swofty.types.generic.user.SkyBlockPlayer;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ActuallyBlueAbicase implements Talisman, CustomDisplayName, NotFinishedYet {

    @Override
    public String getSkullTexture(@Nullable SkyBlockPlayer player, SkyBlockItem item) {
        return "a3c153c391c34e2d328a60839e683a9f82ad3048299d8bc6a39e6f915cc5a";
    }

    @Override
    public List<String> getTalismanDisplay() {
        return List.of(
                "§7Mining Wisdom: §a+1.5",
                " ",
                "§7Grants §6+1 Magical Power §7per §a2",
                "§7contacts in your Abiphone.",
                " ",
                "§8Only ONE case will have an effect",
                "§8while in accessory bag."
        );
    }

    @Override
    public String getDisplayName(@Nullable SkyBlockItem item) {
        return "Actually Blue™ Abicase";
    }
}
