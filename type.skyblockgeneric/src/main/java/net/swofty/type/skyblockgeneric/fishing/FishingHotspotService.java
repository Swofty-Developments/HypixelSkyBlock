package net.swofty.type.skyblockgeneric.fishing;

import net.minestom.server.coordinate.Pos;
import net.swofty.commons.skyblock.statistics.ItemStatistic;
import net.swofty.commons.skyblock.statistics.ItemStatistics;
import net.swofty.type.generic.HypixelConst;
import net.swofty.type.skyblockgeneric.region.SkyBlockRegion;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;

public final class FishingHotspotService {
    private static final double HOTSPOT_RADIUS_SQUARED = 25.0D;

    private FishingHotspotService() {
    }

    public static boolean isBobberInHotspot(SkyBlockPlayer player, FishingMedium medium, Pos bobberPos) {
        ItemStatistics buffs = getActiveHotspotBuffs(player, medium, bobberPos);
        for (ItemStatistic statistic : ItemStatistic.values()) {
            if (buffs.getOverall(statistic) != 0.0D) {
                return true;
            }
        }
        return false;
    }

    public static ItemStatistics getActiveHotspotBuffs(SkyBlockPlayer player, FishingMedium medium, Pos bobberPos) {
        String serverType = HypixelConst.getTypeLoader().getType().name();
        SkyBlockRegion playerRegion = player.getRegion();
        String regionId = playerRegion == null ? null : playerRegion.getType().name();
        double sinkerMultiplier = 1.0D;
        if (player.getItemInMainHand() != null) {
            var heldItem = new net.swofty.type.skyblockgeneric.item.SkyBlockItem(player.getItemInMainHand());
            var sinker = FishingRodPartService.getSinker(heldItem);
            if (sinker != null && sinker.getHotspotBuffMultiplier() > 0) {
                sinkerMultiplier = sinker.getHotspotBuffMultiplier();
            }
        }

        ItemStatistics.Builder builder = ItemStatistics.builder();
        for (HotspotDefinition hotspot : FishingRegistry.getHotspots()) {
            if (hotspot.medium() != medium) {
                continue;
            }
            if (!hotspot.regions().isEmpty() && (regionId == null || !hotspot.regions().contains(regionId))) {
                continue;
            }
            if (!matchesAnyPoint(serverType, bobberPos, hotspot)) {
                continue;
            }

            for (ItemStatistic statistic : ItemStatistic.values()) {
                double value = hotspot.buffs().getOverall(statistic) * sinkerMultiplier;
                if (value != 0.0D) {
                    builder.withBase(statistic, value);
                }
            }
        }
        return builder.build();
    }

    private static boolean matchesAnyPoint(String serverType, Pos bobberPos, HotspotDefinition hotspot) {
        for (HotspotDefinition.SpawnPoint point : hotspot.spawnPoints()) {
            if (point.serverType() != null && !point.serverType().equals(serverType)) {
                continue;
            }
            double dx = bobberPos.x() - point.x();
            double dy = bobberPos.y() - point.y();
            double dz = bobberPos.z() - point.z();
            if ((dx * dx) + (dy * dy) + (dz * dz) <= HOTSPOT_RADIUS_SQUARED) {
                return true;
            }
        }
        return false;
    }
}
