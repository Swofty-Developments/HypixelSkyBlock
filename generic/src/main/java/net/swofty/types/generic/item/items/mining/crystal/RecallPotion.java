package net.swofty.types.generic.item.items.mining.crystal;

import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.CustomSkyBlockItem;
import net.swofty.types.generic.item.impl.NotFinishedYet;
import net.swofty.types.generic.item.impl.SkullHead;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.user.statistics.ItemStatistics;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;

public class RecallPotion implements CustomSkyBlockItem, SkullHead, NotFinishedYet {
    @Override
    public ItemStatistics getStatistics(SkyBlockItem instance) {
        return ItemStatistics.empty();
    }

    @Override
    public String getSkullTexture(@Nullable SkyBlockPlayer player, SkyBlockItem item) {
        return "1af8151deb39553292b9a69674832dda8001780ca98180a06eaf9b368930b5bb";
    }

    @Override
    public ArrayList<String> getLore(SkyBlockPlayer player, SkyBlockItem item) {
        return new ArrayList<>(Arrays.asList(
                "§7§7A mystical and glowing elixir",
                "§7imbued with special abilities",
                "§7from another dimension.",
                "",
                "§6Ability: Ancient Recall  §e§lRIGHT CLICK",
                "§7§7When in the §5Crystal Hollows",
                "§5§7allows you to save and §dwarp",
                "§d§7back to a §bsaved location§7."));
    }
}
