package net.swofty.type.garden.visitor;

import net.minestom.server.timer.ExecutionType;
import net.minestom.server.timer.Scheduler;
import net.minestom.server.timer.TaskSchedule;
import net.swofty.commons.StringUtility;
import net.swofty.type.garden.GardenCropRegistry;
import net.swofty.type.garden.GardenServices;
import net.swofty.type.garden.config.GardenConfigRegistry;
import net.swofty.type.garden.gui.GardenGuiSupport;
import net.swofty.type.skyblockgeneric.SkyBlockGenericLoader;
import net.swofty.type.skyblockgeneric.garden.GardenData;
import net.swofty.type.skyblockgeneric.skill.SkillCategories;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public final class GardenVisitorRuntime {
    private static final Pattern ANY_PATTERN = Pattern.compile("^ANY(?:\\s+(\\d+))?$", Pattern.CASE_INSENSITIVE);
    private static final List<String> RARITY_ORDER = List.of("UNCOMMON", "RARE", "LEGENDARY", "MYTHIC", "SPECIAL");
    private static final List<String> TRACKED_DIALOGUE_FLAGS = List.of(
        "sam",
        "anita",
        "jacob",
        "jeff",
        "pesthunter_phillip",
        "carpenter",
        "shifty",
        "desk"
    );

    private GardenVisitorRuntime() {
    }

    public static void start(Scheduler scheduler) {
        scheduler.submitTask(() -> {
            tickAllGardens();
            return TaskSchedule.seconds(1);
        }, ExecutionType.TICK_END);
    }

    private static void tickAllGardens() {
        Map<UUID, List<SkyBlockPlayer>> playersByProfile = SkyBlockGenericLoader.getLoadedPlayers().stream()
            .filter(SkyBlockPlayer::isOnGarden)
            .filter(player -> player.getSkyblockDataHandler() != null)
            .collect(Collectors.groupingBy(player -> player.getSkyblockDataHandler().getCurrentProfileId()));

        playersByProfile.values().forEach(GardenVisitorRuntime::tickProfile);
    }

    private static void tickProfile(List<SkyBlockPlayer> viewers) {
        if (viewers.isEmpty()) {
            return;
        }

        SkyBlockPlayer primary = viewers.getFirst();
        GardenData.GardenVisitorsData visitors = GardenGuiSupport.visitors(primary);
        GardenData.GardenCoreData core = GardenGuiSupport.core(primary);
        long now = System.currentTimeMillis();

        if (visitors.getLastProcessedAt() <= 0L) {
            visitors.setLastProcessedAt(now);
        }
        if (visitors.getNextArrivalAt() <= 0L) {
            visitors.setNextArrivalAt(now + arrivalMillis(core));
        }

        long realElapsed = Math.max(0L, now - visitors.getLastProcessedAt());
        if (realElapsed > 0L && isActivelyFarming(visitors, now)) {
            int farmingSpeedup = Math.max(1, GardenServices.visitors().getFarmingSpeedupMultiplier());
            long bonusReduction = realElapsed * (farmingSpeedup - 1L);
            visitors.setNextArrivalAt(visitors.getNextArrivalAt() - bonusReduction);
        }

        int maxCapacity = GardenServices.visitors().getMaxVisibleVisitors() + GardenServices.visitors().getMaxQueuedVisitors();
        while (visitors.getActiveVisitors().size() + visitors.getQueuedVisitors().size() < maxCapacity
            && now >= visitors.getNextArrivalAt()) {
            if (!spawnNextVisitor(primary, viewers, visitors, core, now)) {
                visitors.setNextArrivalAt(now + arrivalMillis(core));
                break;
            }
            visitors.setNextArrivalAt(visitors.getNextArrivalAt() + arrivalMillis(core));
        }

        visitors.setLastProcessedAt(now);
        core.setLastActiveAt(now);
    }

    private static boolean spawnNextVisitor(
        SkyBlockPlayer primary,
        List<SkyBlockPlayer> viewers,
        GardenData.GardenVisitorsData visitors,
        GardenData.GardenCoreData core,
        long now
    ) {
        List<Map<String, Object>> candidates = GardenServices.visitors().getVisitors().stream()
            .filter(definition -> canVisit(primary, visitors, core, definition))
            .toList();
        if (candidates.isEmpty()) {
            return false;
        }

        Map<String, Object> chosen = chooseWeighted(candidates);
        GardenData.GardenVisitorState state = buildVisitorState(primary, visitors, core, chosen, now);

        if (visitors.getActiveVisitors().size() < GardenServices.visitors().getMaxVisibleVisitors()) {
            visitors.getActiveVisitors().add(state);
        } else {
            state.setQueued(true);
            visitors.getQueuedVisitors().add(state);
        }

        String visitorId = state.getVisitorId();
        visitors.getVisitCounts().merge(visitorId, 1, Integer::sum);
        visitors.getLogbookEntries().add(visitorId);

        String displayName = GardenConfigRegistry.getString(chosen, "display_name", StringUtility.toNormalCase(visitorId));
        viewers.forEach(player -> player.sendMessage("§aVisitor §e" + displayName + " §ahas arrived at your Desk."));
        return true;
    }

    private static GardenData.GardenVisitorState buildVisitorState(
        SkyBlockPlayer player,
        GardenData.GardenVisitorsData visitors,
        GardenData.GardenCoreData core,
        Map<String, Object> definition,
        long now
    ) {
        String visitorId = GardenConfigRegistry.getString(definition, "id", "");
        String rarity = GardenConfigRegistry.getString(definition, "rarity", "UNCOMMON").toUpperCase();
        boolean firstVisit = !visitors.getVisitCounts().containsKey(visitorId) && !visitors.getServedCounts().containsKey(visitorId);

        int[] baseRange = GardenServices.visitors().getBaseItemsRange(core.getLevel());
        int baseItems = ThreadLocalRandom.current().nextInt(baseRange[0], baseRange[1] + 1);

        List<String> wantedItems = resolveWantedItems(definition, firstVisit);
        List<GardenData.GardenRequest> requests = new ArrayList<>();
        double farmingXp = 0D;
        int copper = 0;

        for (String wantedItem : wantedItems) {
            double quantityMultiplier = GardenServices.visitors().getQuantityMultiplier(wantedItem);
            int amount = Math.max(1, (int) Math.round(
                baseItems * quantityMultiplier * GardenServices.visitors().getRequestMultiplier(rarity)
            ));

            GardenData.GardenRequest request = new GardenData.GardenRequest();
            request.setItemId(wantedItem);
            request.setAmount(amount);
            request.setItemQuantityMultiplier(quantityMultiplier);
            requests.add(request);

            farmingXp += GardenServices.visitors().calculateFarmingXp(
                baseItems,
                GardenServices.visitors().getItemFarmingXpAmount(wantedItem),
                rarity
            );
            copper += GardenServices.visitors().calculateCopper(baseItems, quantityMultiplier, rarity);
        }

        long farmingXpReward = Math.round(farmingXp);
        int gardenXpReward = GardenServices.visitors().calculateGardenXp(core.getLevel(), rarity);
        int bits = GardenServices.visitors().getBitsBase();

        Map<String, Object> firstVisitOverride = GardenConfigRegistry.getSection(definition, "first_visit_override");
        if (firstVisit && !firstVisitOverride.isEmpty()) {
            farmingXpReward = GardenConfigRegistry.getLong(firstVisitOverride, "farming_xp", farmingXpReward);
            copper = GardenConfigRegistry.getInt(firstVisitOverride, "copper", copper);
            gardenXpReward = GardenConfigRegistry.getInt(firstVisitOverride, "garden_xp", gardenXpReward);
        }

        Map<String, Object> overrideRewards = GardenConfigRegistry.getSection(definition, "override_rewards");
        List<String> guaranteedRewards = new ArrayList<>();
        if (!overrideRewards.isEmpty()) {
            farmingXpReward = GardenConfigRegistry.getLong(overrideRewards, "farming_xp", farmingXpReward);
            copper = GardenConfigRegistry.getInt(overrideRewards, "copper", copper);
            guaranteedRewards.addAll(GardenConfigRegistry.getList(overrideRewards, "items").stream()
                .map(String::valueOf)
                .toList());
        }

        for (Object reward : GardenConfigRegistry.getList(definition, "unique_rewards")) {
            String rewardId = String.valueOf(reward);
            if (!rewardId.endsWith("_ON_FIRST_VISIT") || firstVisit) {
                guaranteedRewards.add(rewardId);
            }
        }

        List<String> bonusRewards = rollBonusRewards(rarity);

        GardenData.GardenVisitorState state = new GardenData.GardenVisitorState();
        state.setVisitorId(visitorId);
        state.setRarity(rarity);
        state.setRequests(requests);
        state.setFarmingXp(farmingXpReward);
        state.setGardenXp(gardenXpReward);
        state.setCopper(copper);
        state.setBits(bits);
        state.setGuaranteedRewards(guaranteedRewards);
        state.setBonusRewards(bonusRewards);
        state.setArrivedAt(now);
        return state;
    }

    private static boolean canVisit(
        SkyBlockPlayer player,
        GardenData.GardenVisitorsData visitors,
        GardenData.GardenCoreData core,
        Map<String, Object> definition
    ) {
        String visitorId = GardenConfigRegistry.getString(definition, "id", "");
        if (visitorId.isBlank()) {
            return false;
        }
        boolean alreadyPresent = visitors.getActiveVisitors().stream().anyMatch(visitor -> visitor.getVisitorId().equalsIgnoreCase(visitorId))
            || visitors.getQueuedVisitors().stream().anyMatch(visitor -> visitor.getVisitorId().equalsIgnoreCase(visitorId));
        if (alreadyPresent) {
            return false;
        }
        if (core.getLevel() < GardenConfigRegistry.getInt(definition, "garden_level", 1)) {
            return false;
        }

        for (Object rawRequirement : GardenConfigRegistry.getList(definition, "requirements")) {
            if (!isRequirementMet(player, core, visitorId, String.valueOf(rawRequirement))) {
                return false;
            }
        }
        return true;
    }

    private static boolean isRequirementMet(SkyBlockPlayer player, GardenData.GardenCoreData core, String visitorId, String requirement) {
        String normalized = requirement == null ? "" : requirement.trim();
        if (normalized.isBlank()) {
            return true;
        }
        if (normalized.regionMatches(true, 0, "Talk to", 0, "Talk to".length())) {
            return isTalkRequirementMet(player, visitorId, normalized);
        }
        if (normalized.regionMatches(true, 0, "Garden ", 0, "Garden ".length())) {
            return core.getLevel() >= parseTrailingRoman(normalized);
        }
        if (normalized.regionMatches(true, 0, "Farming ", 0, "Farming ".length())) {
            return player.getSkills().getCurrentLevel(SkillCategories.FARMING) >= parseTrailingRoman(normalized);
        }
        if (normalized.regionMatches(true, 0, "Fishing ", 0, "Fishing ".length())) {
            return player.getSkills().getCurrentLevel(SkillCategories.FISHING) >= parseTrailingRoman(normalized);
        }
        return false;
    }

    private static boolean isTalkRequirementMet(SkyBlockPlayer player, String visitorId, String requirement) {
        String normalizedNpc = normalizeDialogueFlag(requirement
            .replaceFirst("(?i)^Talk to\\s+", "")
            .replaceFirst("(?i)^the\\s+", "")
            .replaceFirst("(?i)^this\\s+", ""));
        List<String> spokenFlags = new ArrayList<>(GardenGuiSupport.personal(player).getSpokenNpcFlags());
        if (spokenFlags.contains(visitorId.toLowerCase()) || spokenFlags.contains(normalizedNpc)) {
            return true;
        }
        return !TRACKED_DIALOGUE_FLAGS.contains(normalizedNpc);
    }

    private static List<String> resolveWantedItems(Map<String, Object> definition, boolean firstVisit) {
        List<String> configured = GardenConfigRegistry.getList(definition, "wanted_items").stream()
            .map(String::valueOf)
            .toList();
        if (configured.isEmpty()) {
            return List.of("WHEAT");
        }
        if (firstVisit && "jerry".equalsIgnoreCase(GardenConfigRegistry.getString(definition, "id", "")) && configured.contains("BREAD")) {
            return List.of("BREAD");
        }

        List<String> resolved = new ArrayList<>();
        for (String entry : configured) {
            Matcher matcher = ANY_PATTERN.matcher(entry.trim());
            if (matcher.matches()) {
                int amount = matcher.group(1) == null ? 1 : Integer.parseInt(matcher.group(1));
                List<String> pool = new ArrayList<>(GardenCropRegistry.getVisitorCropPool());
                while (amount > 0 && !pool.isEmpty()) {
                    String selected = pool.remove(ThreadLocalRandom.current().nextInt(pool.size()));
                    resolved.add(selected);
                    amount--;
                }
            } else {
                resolved.add(entry.trim().toUpperCase());
            }
        }
        return resolved.isEmpty() ? List.of("WHEAT") : resolved;
    }

    private static List<String> rollBonusRewards(String rarity) {
        List<String> rewards = new ArrayList<>();
        for (Map<String, Object> entry : GardenServices.visitors().getBonusRewards()) {
            String minRarity = GardenConfigRegistry.getString(entry, "min_rarity", "UNCOMMON");
            if (!isAtLeastRarity(rarity, minRarity)) {
                continue;
            }
            if (ThreadLocalRandom.current().nextDouble() <= GardenConfigRegistry.getDouble(entry, "chance", 0D)) {
                rewards.add(GardenConfigRegistry.getString(entry, "item", ""));
            }
        }
        return rewards;
    }

    private static Map<String, Object> chooseWeighted(List<Map<String, Object>> candidates) {
        double totalWeight = candidates.stream()
            .mapToDouble(candidate -> GardenServices.visitors().getRarityWeight(GardenConfigRegistry.getString(candidate, "rarity", "UNCOMMON")))
            .sum();
        if (totalWeight <= 0D) {
            return candidates.getFirst();
        }

        double roll = ThreadLocalRandom.current().nextDouble(totalWeight);
        double cursor = 0D;
        for (Map<String, Object> candidate : candidates) {
            cursor += GardenServices.visitors().getRarityWeight(GardenConfigRegistry.getString(candidate, "rarity", "UNCOMMON"));
            if (roll <= cursor) {
                return candidate;
            }
        }
        return candidates.getLast();
    }

    private static boolean isAtLeastRarity(String rarity, String minRarity) {
        return RARITY_ORDER.indexOf(rarity.toUpperCase()) >= RARITY_ORDER.indexOf(minRarity.toUpperCase());
    }

    private static boolean isActivelyFarming(GardenData.GardenVisitorsData visitors, long now) {
        return now - visitors.getLastFarmingActivityAt() <= 2_500L;
    }

    private static long arrivalMillis(GardenData.GardenCoreData core) {
        return GardenServices.visitors().getArrivalMinutes(core.getServedUniqueVisitors().size()) * 60_000L;
    }

    private static String normalizeDialogueFlag(String value) {
        return value.toLowerCase()
            .replaceAll("[^a-z0-9]+", "_")
            .replaceAll("^_+|_+$", "");
    }

    private static int parseTrailingRoman(String requirement) {
        String[] tokens = requirement.trim().split("\\s+");
        if (tokens.length == 0) {
            return 0;
        }
        return romanToInt(tokens[tokens.length - 1]);
    }

    private static int romanToInt(String roman) {
        return switch (roman.toUpperCase()) {
            case "I" -> 1;
            case "II" -> 2;
            case "III" -> 3;
            case "IV" -> 4;
            case "V" -> 5;
            case "VI" -> 6;
            case "VII" -> 7;
            case "VIII" -> 8;
            case "IX" -> 9;
            case "X" -> 10;
            case "XI" -> 11;
            case "XII" -> 12;
            case "XIII" -> 13;
            case "XIV" -> 14;
            case "XV" -> 15;
            default -> 0;
        };
    }
}
