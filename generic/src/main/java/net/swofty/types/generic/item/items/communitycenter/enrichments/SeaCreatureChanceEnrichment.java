package net.swofty.types.generic.item.items.communitycenter.enrichments;

import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.CustomSkyBlockItem;
import net.swofty.types.generic.item.impl.NotFinishedYet;
import net.swofty.types.generic.item.impl.SkullHead;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.user.statistics.ItemStatistics;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class SeaCreatureChanceEnrichment implements CustomSkyBlockItem, SkullHead, NotFinishedYet {
    @Override
    public ItemStatistics getStatistics(SkyBlockItem instance) {
        return ItemStatistics.empty();
    }

    @Override
    public String getSkullTexture(@Nullable SkyBlockPlayer player, SkyBlockItem item) {
        return "92c77045a46982b40621bfc5b058159e961340d1076c81b96517664745eb8ccf";
    }

    @Override
    public List<String> getLore(SkyBlockPlayer player, SkyBlockItem item) {
        return List.of(
                "§7Enriches an accessory with the",
                "§7power of §3+0.3α Sea Creature",
                "§3Chance §7when applied to a §6Legendary",
                "§6§7or §dMythic §7accessory.",
                "",
                "§7Only §7§lONE §7enrichment can be applied",
                "§7to a given accessory.");
    }
}
