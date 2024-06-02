package net.swofty.types.generic.item.items.communitycenter.enrichments;

import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.CustomSkyBlockItem;
import net.swofty.types.generic.item.impl.NotFinishedYet;
import net.swofty.types.generic.item.impl.SkullHead;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.user.statistics.ItemStatistics;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class FerocityEnrichment implements CustomSkyBlockItem, SkullHead, NotFinishedYet {
    @Override
    public ItemStatistics getStatistics(SkyBlockItem instance) {
        return ItemStatistics.empty();
    }

    @Override
    public String getSkullTexture(@Nullable SkyBlockPlayer player, SkyBlockItem item) {
        return "f1e8771719abdeb11b864c0104b3bd7fc5d26d21f0f0d8ab47d13e65cb74c40";
    }

    @Override
    public List<String> getLore(SkyBlockPlayer player, SkyBlockItem item) {
        return List.of(
                "§7Enriches an accessory with the",
                "§7power of §c+0.3⫽ Ferocity §7when",
                "§7applied to a §6Legendary §7or §dMythic",
                "§d§7accessory.",
                "",
                "§7Only §7§lONE §7enrichment can be applied",
                "§7to a given accessory.");
    }
}
