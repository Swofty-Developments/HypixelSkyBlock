package net.swofty.type.skyblockgeneric.item.handlers.customstatistics;

import net.swofty.commons.statistics.ItemStatistics;
import net.swofty.type.generic.item.SkyBlockItem;

import java.util.function.Function;

public record CustomStatisticsConfig(Function<SkyBlockItem, ItemStatistics> statisticsProvider) {
}
