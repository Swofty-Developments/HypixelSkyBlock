package net.swofty.type.skyblockgeneric.fishing;

import net.swofty.commons.skyblock.statistics.ItemStatistics;
import net.swofty.type.skyblockgeneric.item.SkyBlockItem;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;
import org.jetbrains.annotations.Nullable;

public record FishingContext(
    SkyBlockPlayer player,
    SkyBlockItem rod,
    FishingMedium medium,
    @Nullable BaitDefinition bait,
    @Nullable RodPartDefinition hook,
    @Nullable RodPartDefinition line,
    @Nullable RodPartDefinition sinker,
    @Nullable String regionId,
    boolean hotspotActive,
    ItemStatistics hotspotBuffs,
    long castDurationMs
) {
}
