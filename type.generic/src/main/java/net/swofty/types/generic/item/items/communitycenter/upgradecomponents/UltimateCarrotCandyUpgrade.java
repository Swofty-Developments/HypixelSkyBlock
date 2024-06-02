package net.swofty.types.generic.item.items.communitycenter.upgradecomponents;

import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.CustomSkyBlockItem;
import net.swofty.types.generic.item.impl.SkullHead;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.user.statistics.ItemStatistics;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class UltimateCarrotCandyUpgrade implements CustomSkyBlockItem, SkullHead {
    @Override
    public ItemStatistics getStatistics(SkyBlockItem instance) {
        return ItemStatistics.empty();
    }

    @Override
    public List<String> getLore(SkyBlockPlayer player, SkyBlockItem item) {
        return List.of(
                "§7Craft with §5Superb Carrot Candies",
                "§5§7efficiently to have them grant",
                "§7§a1,500,000 §7pet Exp.");
    }

    @Override
    public String getSkullTexture(@Nullable SkyBlockPlayer player, SkyBlockItem item) {
        return "bf1c52cb15dc2a7192b155e52241e7819b565f3a6492cc4df127d2a1c42a";
    }
}
