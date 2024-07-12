package net.swofty.anticheat.flag;

import net.swofty.anticheat.loader.SwoftyAnticheat;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class FlagManager {
    private final UUID uuid;
    private final Map<FlagType, List<Flag>> flags;
    private static final long FLAG_EXPIRATION_TIME = 1000; // 1 second

    public FlagManager(UUID uuid, Map<FlagType, List<Flag>> flags) {
        this.uuid = uuid;
        this.flags = flags;
    }

    public void addFlag(FlagType flagType, double certainty) {
        Flag flag = flagType.getFlagSupplier().get();
        flag.setCertainty(certainty);

        flags.computeIfAbsent(flagType, k -> new ArrayList<>()).add(flag);
        calculateOverallCertainty(flagType);
    }

    private void calculateOverallCertainty(FlagType flagType) {
        List<Flag> flagList = flags.get(flagType);
        long currentTime = System.currentTimeMillis();

        // Remove expired flags
        flagList.removeIf(flag -> currentTime - flag.getTimestamp() > FLAG_EXPIRATION_TIME);

        if (flagList.isEmpty()) return;

        // Calculate overall certainty
        double overallCertainty = flagList.stream()
                .mapToDouble(Flag::getCertainty)
                .reduce(1, (a, b) -> a * (1 - b));
        overallCertainty = 1 - overallCertainty;

        if (overallCertainty > 0.9) {
            SwoftyAnticheat.getPunishmentHandler().onFlag(uuid, flagType);
        }
    }
}