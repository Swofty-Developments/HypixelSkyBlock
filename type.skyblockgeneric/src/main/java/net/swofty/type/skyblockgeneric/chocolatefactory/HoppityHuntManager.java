package net.swofty.type.skyblockgeneric.chocolatefactory;

import lombok.Getter;
import lombok.Setter;
import net.swofty.type.skyblockgeneric.data.datapoints.DatapointChocolateFactory;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class HoppityHuntManager {
    @Getter
    private static final HoppityHuntManager instance = new HoppityHuntManager();

    @Setter
    private static Runnable onHuntStartCallback;
    @Setter
    private static Runnable onHuntStopCallback;

    @Getter
    private boolean active;
    @Getter
    private final Map<HoppityEggLocations, HoppityEggType> locationEggTypes = new HashMap<>();

    private static final NavigableMap<Double, ChocolateRabbit.Rarity> RARITY_WEIGHTS = new TreeMap<>();

    static {
        double cumulative = 0;
        cumulative += 608;
        RARITY_WEIGHTS.put(cumulative, ChocolateRabbit.Rarity.COMMON);
        cumulative += 250;
        RARITY_WEIGHTS.put(cumulative, ChocolateRabbit.Rarity.UNCOMMON);
        cumulative += 100;
        RARITY_WEIGHTS.put(cumulative, ChocolateRabbit.Rarity.RARE);
        cumulative += 30;
        RARITY_WEIGHTS.put(cumulative, ChocolateRabbit.Rarity.EPIC);
        cumulative += 10;
        RARITY_WEIGHTS.put(cumulative, ChocolateRabbit.Rarity.LEGENDARY);
        cumulative += 2;
        RARITY_WEIGHTS.put(cumulative, ChocolateRabbit.Rarity.MYTHIC);
        cumulative += 0.5;
        RARITY_WEIGHTS.put(cumulative, ChocolateRabbit.Rarity.DIVINE);
    }

    private static final Map<ChocolateRabbit.Rarity, Integer> DUPLICATE_MULTIPLIERS = Map.of(
            ChocolateRabbit.Rarity.COMMON, 150,
            ChocolateRabbit.Rarity.UNCOMMON, 300,
            ChocolateRabbit.Rarity.RARE, 750,
            ChocolateRabbit.Rarity.EPIC, 1500,
            ChocolateRabbit.Rarity.LEGENDARY, 4000,
            ChocolateRabbit.Rarity.MYTHIC, 8000,
            ChocolateRabbit.Rarity.DIVINE, 15000
    );

    private HoppityHuntManager() {}

    public void startHunt() {
        if (active) return;

        active = true;
        locationEggTypes.clear();

        HoppityEggType[] eggTypes = {
                HoppityEggType.BREAKFAST, HoppityEggType.LUNCH, HoppityEggType.DINNER,
                HoppityEggType.BRUNCH, HoppityEggType.DEJEUNER, HoppityEggType.SUPPER
        };

        HoppityEggLocations[] locations = HoppityEggLocations.values();
        for (int i = 0; i < locations.length; i++) {
            locationEggTypes.put(locations[i], eggTypes[i % eggTypes.length]);
        }

        if (onHuntStartCallback != null) {
            onHuntStartCallback.run();
        }
    }

    public void stopHunt() {
        if (!active) return;

        active = false;

        if (onHuntStopCallback != null) {
            onHuntStopCallback.run();
        }

        locationEggTypes.clear();
    }

    public void claimEgg(SkyBlockPlayer player, HoppityEggLocations location) {
        if (!active) return;

        DatapointChocolateFactory.ChocolateFactoryData data = ChocolateFactoryHelper.getData(player);

        if (data.hasClaimedEgg(location.name())) {
            player.sendMessage("§cYou have already claimed this egg!");
            return;
        }

        HoppityEggType eggType = locationEggTypes.get(location);
        if (eggType == null) return;

        data.claimEgg(location.name());

        ChocolateRabbit.Rarity rarity = rollRarity();
        ChocolateRabbit rabbit = rollRabbit(rarity);

        player.sendMessage("§d§lHOPPITY'S HUNT §r" + eggType.getFormattedName() + " §r" + location.getLocationMessage());

        if (rabbit != null && !data.hasFoundRabbit(rabbit.name())) {
            data.addFoundRabbit(rabbit.name());
            player.sendMessage("§d§lNEW RABBIT! §r§" + rarity.getColorCode() + rabbit.getDisplayName() + " §7(" + rarity.getFormattedName() + "§7)");
            player.sendMessage("§7+§6" + rarity.getChocolateBonus() + " Chocolate §7per second");
        } else {
            long duplicateChocolate = getDuplicateChocolate(rarity, data);
            data.addChocolate(duplicateChocolate);
            String rabbitName = rabbit != null ? rabbit.getDisplayName() : "Unknown";
            player.sendMessage("§d§lDUPLICATE RABBIT! §r§" + rarity.getColorCode() + rabbitName);
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
        List<ChocolateRabbit> eligible = new ArrayList<>();
        for (ChocolateRabbit rabbit : ChocolateRabbit.values()) {
            if (rabbit.getRarity() != rarity) continue;
            if (rabbit.getObtainMethod() != null) continue;
            if (rabbit.getLocation() != null && !rabbit.getLocation().equals("Hub")) continue;
            eligible.add(rabbit);
        }

        if (eligible.isEmpty()) return null;
        return eligible.get(ThreadLocalRandom.current().nextInt(eligible.size()));
    }

    private long getDuplicateChocolate(ChocolateRabbit.Rarity rarity, DatapointChocolateFactory.ChocolateFactoryData data) {
        int multiplier = DUPLICATE_MULTIPLIERS.getOrDefault(rarity, 150);
        double cps = data.getChocolatePerSecond();
        return Math.max(multiplier, (long) (cps * multiplier));
    }

    private String formatNumber(long number) {
        if (number >= 1_000_000) {
            return String.format("%.1fM", number / 1_000_000.0);
        } else if (number >= 1_000) {
            return String.format("%,d", number);
        }
        return String.valueOf(number);
    }
}
