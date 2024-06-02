package net.swofty.types.generic.item.items.communitycenter.enrichments;

import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.CustomSkyBlockItem;
import net.swofty.types.generic.item.impl.NotFinishedYet;
import net.swofty.types.generic.item.impl.SkullHead;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.user.statistics.ItemStatistics;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class DefenseEnrichment implements CustomSkyBlockItem, SkullHead, NotFinishedYet {
    @Override
    public ItemStatistics getStatistics(SkyBlockItem instance) {
        return ItemStatistics.empty();
    }

    @Override
    public String getSkullTexture(@Nullable SkyBlockPlayer player, SkyBlockItem item) {
        return "32fa8f38c7b22096619c3a6d6498b405530e48d5d4f91e2aacea578844d5c67";
    }

    @Override
    public List<String> getLore(SkyBlockPlayer player, SkyBlockItem item) {
        return List.of(
                "§7Enriches an accessory with the",
                "§7power of §a+1❈ Defense §7when applied",
                "§7to a §6Legendary §7or §dMythic §7accessory.",
                "",
                "§7Only §7§lONE §7enrichment can be applied",
                "§7to a given accessory.");
    }
}
