package net.swofty.types.generic.item.items.communitycenter;

import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.CustomSkyBlockItem;
import net.swofty.types.generic.item.impl.NotFinishedYet;
import net.swofty.types.generic.item.impl.SkullHead;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.user.statistics.ItemStatistics;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class Portalizer implements CustomSkyBlockItem, SkullHead, NotFinishedYet {
    @Override
    public ItemStatistics getStatistics(SkyBlockItem instance) {
        return ItemStatistics.empty();
    }

    @Override
    public List<String> getLore(SkyBlockPlayer player, SkyBlockItem item) {
        return List.of(
                "§7Combine in an anvil with any §aTravel",
                "§aScroll §7to transform it into a Portal",
                "§7item which can be placed on your",
                "§7private island.",
                "",
                "§7§8Note: Some portals are available in",
                "§8various collections with cheap",
                "§8recipes!");
    }

    @Override
    public String getSkullTexture(@Nullable SkyBlockPlayer player, SkyBlockItem item) {
        return "2c915db3fc40a79b63c2c453f0c490981e5227c5027501283272138533dea519";
    }
}
