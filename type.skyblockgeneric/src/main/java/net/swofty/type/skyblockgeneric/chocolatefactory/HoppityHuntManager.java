package net.swofty.type.skyblockgeneric.chocolatefactory;

import lombok.Getter;
import lombok.Setter;
import net.swofty.type.skyblockgeneric.data.datapoints.DatapointChocolateFactory;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;
import java.util.concurrent.ThreadLocalRandom;

public class HoppityHuntManager {
    private static final String HUB_LOCATION = "Hub";
    private static final String EGG_ALREADY_CLAIMED_MESSAGE = "§cYou have already claimed this egg!";
    private static final String NEW_RABBIT_MESSAGE_PREFIX = "§d§lNEW RABBIT! §r§";
    private static final String DUPLICATE_RABBIT_MESSAGE_PREFIX = "§d§lDUPLICATE RABBIT! §r§";
    private static final String HUNT_MESSAGE_PREFIX = "§d§lHOPPITY'S HUNT §r";
    private static final int DEFAULT_DUPLICATE_MULTIPLIER = 150;
    private static final double ONE_MILLION = 1_000_000.0;
    private static final int ONE_THOUSAND = 1_000;

    private static final HoppityEggType[] HUNT_EGG_TYPES = {
            HoppityEggType.BREAKFAST, HoppityEggType.LUNCH, HoppityEggType.DINNER,
            HoppityEggType.BRUNCH, HoppityEggType.DEJEUNER, HoppityEggType.SUPPER
    };

    @Getter
    private static final HoppityHuntManager instance = new HoppityHuntManager();

    @Setter
    private static Runnable onHuntStartCallback;
    @Setter
    private static Runnable onHuntStopCallback;

    @Getter
    private boolean active;
    @Getter
    private final Map<HoppityEggLocations, HoppityEggType> locationEggTypes = new EnumMap<>(HoppityEggLocations.class);

    private static final NavigableMap<Double, ChocolateRabbit.Rarity> RARITY_WEIGHTS = createRarityWeights();
    private static final Map<ChocolateRabbit.Rarity, Integer> DUPLICATE_MULTIPLIERS = createDuplicateMultipliers();
    private static final Map<ChocolateRabbit.Rarity, List<ChocolateRabbit>> ELIGIBLE_RABBITS = createEligibleRabbits();

    private HoppityHuntManager() {
    }

    public void startHunt() {
        if (active) return;

        active = true;
        locationEggTypes.clear();

        HoppityEggLocations[] locations = HoppityEggLocations.values();
        for (int i = 0; i < locations.length; i++) {
            locationEggTypes.put(locations[i], HUNT_EGG_TYPES[i % HUNT_EGG_TYPES.length]);
        }

        runCallback(onHuntStartCallback);
    }

    public void stopHunt() {
        if (!active) return;

        active = false;

        runCallback(onHuntStopCallback);

        locationEggTypes.clear();
    }

    public void claimEgg(SkyBlockPlayer player, HoppityEggLocations location) {
        if (!active) return;

        DatapointChocolateFactory.ChocolateFactoryData data = ChocolateFactoryHelper.getData(player);

        if (data.hasClaimedEgg(location.name())) {
            player.sendMessage(EGG_ALREADY_CLAIMED_MESSAGE);
            return;
        }

        HoppityEggType eggType = locationEggTypes.get(location);
        if (eggType == null) return;

        data.claimEgg(location.name());

        ChocolateRabbit.Rarity rarity = rollRarity();
        ChocolateRabbit rabbit = rollRabbit(rarity);

        player.sendMessage(HUNT_MESSAGE_PREFIX + eggType.getFormattedName() + " §r" + location.getLocationMessage());

        if (rabbit != null && !data.hasFoundRabbit(rabbit.name())) {
            data.addFoundRabbit(rabbit.name());
            player.sendMessage(NEW_RABBIT_MESSAGE_PREFIX + rarity.getColorCode() + rabbit.getDisplayName() + " §7(" + rarity.getFormattedName() + "§7)");
            player.sendMessage("§7+§6" + rarity.getChocolateBonus() + " Chocolate §7per second");
        } else {
            long duplicateChocolate = getDuplicateChocolate(rarity, data);
            data.addChocolate(duplicateChocolate);
            String rabbitName = rabbit != null ? rabbit.getDisplayName() : "Unknown";
            player.sendMessage(DUPLICATE_RABBIT_MESSAGE_PREFIX + rarity.getColorCode() + rabbitName);
            player.sendMessage("§7+§6" + formatNumber(duplicateChocolate) + " Chocolate");
        }

        ChocolateFactoryHelper.getDatapoint(player).setValue(data);
    }

    private ChocolateRabbit.Rarity rollRarity() {
        double totalWeight = RARITY_WEIGHTS.lastKey();
        double roll = ThreadLocalRandom.current().nextDouble() * totalWeight;
        return RARITY_WEIGHTS.higherEntry(roll).getValue();
    }

    private ChocolateRabbit rollRabbit(ChocolateRabbit.Rarity rarity) {
        List<ChocolateRabbit> eligible = ELIGIBLE_RABBITS.getOrDefault(rarity, List.of());
        if (eligible.isEmpty()) return null;
        return eligible.get(ThreadLocalRandom.current().nextInt(eligible.size()));
    }

    private long getDuplicateChocolate(ChocolateRabbit.Rarity rarity, DatapointChocolateFactory.ChocolateFactoryData data) {
        int multiplier = DUPLICATE_MULTIPLIERS.getOrDefault(rarity, DEFAULT_DUPLICATE_MULTIPLIER);
        double cps = data.getChocolatePerSecond();
        return Math.max(multiplier, (long) (cps * multiplier));
    }

    private String formatNumber(long number) {
        if (number >= ONE_MILLION) {
            return String.format("%.1fM", number / ONE_MILLION);
        }
        if (number >= ONE_THOUSAND) {
            return String.format("%,d", number);
        }
        return String.valueOf(number);
    }

    private static NavigableMap<Double, ChocolateRabbit.Rarity> createRarityWeights() {
        NavigableMap<Double, ChocolateRabbit.Rarity> weights = new TreeMap<>();
        double cumulative = 0;
        cumulative += 608;
        weights.put(cumulative, ChocolateRabbit.Rarity.COMMON);
        cumulative += 250;
        weights.put(cumulative, ChocolateRabbit.Rarity.UNCOMMON);
        cumulative += 100;
        weights.put(cumulative, ChocolateRabbit.Rarity.RARE);
        cumulative += 30;
        weights.put(cumulative, ChocolateRabbit.Rarity.EPIC);
        cumulative += 10;
        weights.put(cumulative, ChocolateRabbit.Rarity.LEGENDARY);
        cumulative += 2;
        weights.put(cumulative, ChocolateRabbit.Rarity.MYTHIC);
        cumulative += 0.5;
        weights.put(cumulative, ChocolateRabbit.Rarity.DIVINE);
        return weights;
    }

    private static Map<ChocolateRabbit.Rarity, Integer> createDuplicateMultipliers() {
        Map<ChocolateRabbit.Rarity, Integer> multipliers = new EnumMap<>(ChocolateRabbit.Rarity.class);
        multipliers.put(ChocolateRabbit.Rarity.COMMON, 150);
        multipliers.put(ChocolateRabbit.Rarity.UNCOMMON, 300);
        multipliers.put(ChocolateRabbit.Rarity.RARE, 750);
        multipliers.put(ChocolateRabbit.Rarity.EPIC, 1500);
        multipliers.put(ChocolateRabbit.Rarity.LEGENDARY, 4000);
        multipliers.put(ChocolateRabbit.Rarity.MYTHIC, 8000);
        multipliers.put(ChocolateRabbit.Rarity.DIVINE, 15000);
        return multipliers;
    }

    private static Map<ChocolateRabbit.Rarity, List<ChocolateRabbit>> createEligibleRabbits() {
        Map<ChocolateRabbit.Rarity, List<ChocolateRabbit>> rabbitsByRarity = new EnumMap<>(ChocolateRabbit.Rarity.class);
        for (ChocolateRabbit.Rarity rarity : ChocolateRabbit.Rarity.values()) {
            rabbitsByRarity.put(rarity, new ArrayList<>());
        }

        for (ChocolateRabbit rabbit : ChocolateRabbit.values()) {
            if (rabbit.getObtainMethod() != null) {
                continue;
            }

            String rabbitLocation = rabbit.getLocation();
            if (rabbitLocation != null && !HUB_LOCATION.equals(rabbitLocation)) {
                continue;
            }

            rabbitsByRarity.get(rabbit.getRarity()).add(rabbit);
        }

        return rabbitsByRarity;
    }

    private void runCallback(Runnable callback) {
        if (callback != null) {
            callback.run();
        }
    }
}
