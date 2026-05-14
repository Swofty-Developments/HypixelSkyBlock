package net.swofty.type.skyblockgeneric.fishing;

import net.swofty.commons.skyblock.statistics.ItemStatistics;
import net.swofty.type.skyblockgeneric.item.SkyBlockItem;
import net.swofty.type.skyblockgeneric.item.components.FishingBaitComponent;
import net.swofty.type.skyblockgeneric.item.components.FishingRodPartComponent;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;
import org.jetbrains.annotations.Nullable;

public record FishingContext(
    SkyBlockPlayer player,
    SkyBlockItem rod,
    FishingMedium medium,
    @Nullable FishingBaitComponent bait,
    @Nullable FishingRodPartComponent hook,
    @Nullable FishingRodPartComponent line,
    @Nullable FishingRodPartComponent sinker,
    @Nullable String regionId,
    boolean hotspotActive,
    ItemStatistics hotspotBuffs,
    long castDurationMs
) {
}
