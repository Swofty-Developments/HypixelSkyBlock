package net.swofty.types.generic.item.items.communitycenter.enrichments;

import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.CustomSkyBlockItem;
import net.swofty.types.generic.item.impl.NotFinishedYet;
import net.swofty.types.generic.item.impl.SkullHead;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.user.statistics.ItemStatistics;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class StrengthEnrichment implements CustomSkyBlockItem, SkullHead, NotFinishedYet {
    @Override
    public ItemStatistics getStatistics(SkyBlockItem instance) {
        return ItemStatistics.empty();
    }

    @Override
    public String getSkullTexture(@Nullable SkyBlockPlayer player, SkyBlockItem item) {
        return "14d844fee24d5f27ddb669438528d83b684d901b75a6889fe7488dfc4cf7a1c";
    }

    @Override
    public List<String> getLore(SkyBlockPlayer player, SkyBlockItem item) {
        return List.of(
                "§7Enriches an accessory with the",
                "§7power of §c+1❁ Strength §7when applied",
                "§7to a §6Legendary §7or §dMythic §7accessory.",
                "",
                "§7Only §7§lONE §7enrichment can be applied",
                "§7to a given accessory.");
    }
}
