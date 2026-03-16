package net.swofty.type.skyblockgeneric.fishing;

import net.swofty.type.generic.data.datapoints.DatapointToggles;
import net.swofty.type.skyblockgeneric.data.SkyBlockDataHandler;
import net.swofty.type.skyblockgeneric.data.datapoints.DatapointCollection;
import net.swofty.type.skyblockgeneric.data.datapoints.DatapointTrophyFish;
import net.swofty.type.skyblockgeneric.entity.FishingHook;
import net.swofty.type.skyblockgeneric.item.SkyBlockItem;
import net.swofty.type.skyblockgeneric.region.SkyBlockRegion;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public final class FishingService {
    private static final Map<UUID, FishingSession> SESSIONS = new ConcurrentHashMap<>();

    private FishingService() {
    }

    public static FishingSession beginCast(SkyBlockPlayer player, SkyBlockItem rod, FishingMedium medium) {
        BaitDefinition bait = FishingBaitService.getFirstAvailableBait(player, medium);
        FishingSession session = new FishingSession(
            player.getUuid(),
            rod.getAttributeHandler().getPotentialType().name(),
            medium,
            bait == null ? null : bait.itemId(),
            System.currentTimeMillis(),
            0L,
            0L,
            false,
            false
        );
        SESSIONS.put(player.getUuid(), session);
        return session;
    }

    public static @Nullable FishingSession getSession(UUID playerUuid) {
        return SESSIONS.get(playerUuid);
    }

    public static void updateSession(FishingSession session) {
        SESSIONS.put(session.ownerUuid(), session);
    }

    public static void clearSession(UUID playerUuid) {
        SESSIONS.remove(playerUuid);
    }

    public static long computeWaitTicks(SkyBlockPlayer player, SkyBlockItem rod, @Nullable BaitDefinition bait) {
        double fishingSpeed = player.getStatistics().allStatistics().getOverall(net.swofty.commons.skyblock.statistics.ItemStatistic.FISHING_SPEED)
            + rod.getAttributeHandler().getStatistics().getOverall(net.swofty.commons.skyblock.statistics.ItemStatistic.FISHING_SPEED)
            + FishingRodPartService.getStatistics(rod).getOverall(net.swofty.commons.skyblock.statistics.ItemStatistic.FISHING_SPEED);
        if (bait != null) {
            fishingSpeed += bait.statistics().getOverall(net.swofty.commons.skyblock.statistics.ItemStatistic.FISHING_SPEED);
            if ("CORRUPTED_BAIT".equals(bait.itemId())) {
                fishingSpeed /= 2.0D;
            }
        }
        long baseTicks = 80L;
        return Math.max(20L, Math.round(baseTicks - Math.min(50D, fishingSpeed / 2D)));
    }

    public static @Nullable FishingCatchResult resolveCatch(SkyBlockPlayer player, SkyBlockItem rod, FishingHook hook) {
        FishingSession session = getSession(player.getUuid());
        if (session == null || session.resolved()) {
            return null;
        }

        BaitDefinition bait = FishingItemCatalog.getBait(session.baitItemId());
        SkyBlockRegion region = player.getRegion();
        var hotspotBuffs = FishingHotspotService.getActiveHotspotBuffs(player, session.medium(), hook.getSpawnPosition());
        boolean hotspotActive = hasAnyBuffValue(hotspotBuffs);
        FishingContext context = new FishingContext(
            player,
            rod,
            session.medium(),
            bait,
            FishingRodPartService.getHook(rod),
            FishingRodPartService.getLine(rod),
            FishingRodPartService.getSinker(rod),
            region == null ? null : region.getType().name(),
            hotspotActive,
            hotspotBuffs,
            System.currentTimeMillis() - session.castAt()
        );

        FishingCatchResult result = FishingCatchResolver.resolve(context);
        if (result == null) {
            return null;
        }

        if (session.baitItemId() != null) {
            boolean preserve = false;
            var caster = rod.getAttributeHandler().getEnchantment(net.swofty.type.skyblockgeneric.enchantment.EnchantmentType.CASTER);
            if (caster != null) {
                preserve |= Math.random() * 100 < caster.level();
            }
            RodPartDefinition sinker = FishingRodPartService.getSinker(rod);
            if (!preserve && sinker != null && sinker.baitPreservationChance() > 0) {
                preserve = Math.random() * 100 < sinker.baitPreservationChance();
            }
            if (!preserve) {
                FishingBaitService.consumeOneBait(player, session.baitItemId());
            }
        }

        awardCatch(player, rod, hook, result);
        updateSession(session.withResolved(true));
        return result;
    }

    private static void awardCatch(SkyBlockPlayer player, SkyBlockItem rod, FishingHook hook, FishingCatchResult result) {
        if (!player.getToggles().get(DatapointToggles.Toggles.ToggleType.HAS_CAUGHT_FIRST_FISH)) {
            player.getToggles().set(DatapointToggles.Toggles.ToggleType.HAS_CAUGHT_FIRST_FISH, true);
        }

        if (result.itemId() != null) {
            player.addAndUpdateItem(net.swofty.commons.skyblock.item.ItemType.valueOf(result.itemId()), result.amount());
            player.getCollection().increase(net.swofty.commons.skyblock.item.ItemType.valueOf(result.itemId()), result.amount());
            player.getSkyblockDataHandler().get(SkyBlockDataHandler.Data.COLLECTION, DatapointCollection.class).setValue(player.getCollection());
        }

        RodPartDefinition sinker = FishingRodPartService.getSinker(rod);
        if (sinker != null && sinker.materializedItemId() != null && Math.random() <= sinker.materializedChance()) {
            player.addAndUpdateItem(net.swofty.commons.skyblock.item.ItemType.valueOf(sinker.materializedItemId()));
        }

        if (result.trophyFishId() != null) {
            String tier = result.itemId() != null && result.itemId().contains("_DIAMOND") ? "DIAMOND" :
                result.itemId() != null && result.itemId().contains("_GOLD") ? "GOLD" :
                    result.itemId() != null && result.itemId().contains("_SILVER") ? "SILVER" : "BRONZE";
            DatapointTrophyFish.TrophyFishData data = player.getTrophyFishData();
            data.getProgress(result.trophyFishId()).increment(tier);
            player.getSkyblockDataHandler().get(SkyBlockDataHandler.Data.TROPHY_FISH, DatapointTrophyFish.class).setValue(data);
        }

        if (result.kind() != FishingCatchKind.SEA_CREATURE && result.skillXp() > 0) {
            player.getSkills().increase(player, net.swofty.type.skyblockgeneric.skill.SkillCategories.FISHING, result.skillXp());
        }

        if (result.kind() == FishingCatchKind.SEA_CREATURE && result.seaCreatureId() != null) {
            // TODO: sea creatures
        }
        player.setItemInHand(rod);
    }

    private static boolean hasAnyBuffValue(net.swofty.commons.skyblock.statistics.ItemStatistics statistics) {
        for (net.swofty.commons.skyblock.statistics.ItemStatistic statistic : net.swofty.commons.skyblock.statistics.ItemStatistic.values()) {
            if (statistics.getOverall(statistic) != 0.0D) {
                return true;
            }
        }
        return false;
    }
}
