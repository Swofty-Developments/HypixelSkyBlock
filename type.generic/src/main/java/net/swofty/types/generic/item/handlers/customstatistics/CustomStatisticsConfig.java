package net.swofty.types.generic.item.handlers.customstatistics;

import net.swofty.commons.statistics.ItemStatistics;
import net.swofty.types.generic.item.SkyBlockItem;

import java.util.function.Function;

public record CustomStatisticsConfig(Function<SkyBlockItem, ItemStatistics> statisticsProvider) {
}
