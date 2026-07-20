package net.swofty.type.skyblockgeneric.hunting;

import net.swofty.type.skyblockgeneric.data.datapoints.DatapointHunting;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ThreadLocalRandom;

public final class AttributeFusionService {
    private AttributeFusionService() {
    }

    public static int required(AttributeDefinition definition) {
        String family = definition.family().name().toLowerCase(Locale.ROOT);
        if (definition.shard().equalsIgnoreCase("Chameleon")) return 1;
        if (family.contains("reptile") || family.contains("amphibian") || family.contains("elemental")) return 2;
        return 5;
    }

    public static List<FusionResult> results(AttributeDefinition first, AttributeDefinition second) {
        if (first == null || second == null || first.id().equals(second.id())) return List.of();
        boolean chameleon = first.shard().equalsIgnoreCase("Chameleon") || second.shard().equalsIgnoreCase("Chameleon");
        if (chameleon) {
            AttributeDefinition origin = first.shard().equalsIgnoreCase("Chameleon") ? second : first;
            return nextById(origin, true, first, second).stream()
                    .map(value -> new FusionResult(value, 1, FusionType.CHAMELEON)).toList();
        }
        List<FusionResult> special = AttributeRegistry.values().stream()
                .filter(output -> !output.id().equals(first.id()) && !output.id().equals(second.id()))
                .filter(output -> specialMatch(output, first, second))
                .map(output -> new FusionResult(output, 2, FusionType.SPECIAL)).toList();
        List<FusionResult> result = new ArrayList<>(special);
        for (AttributeDefinition output : nextById(first, false, first, second)) {
            if (result.stream().noneMatch(existing -> existing.definition().id().equals(output.id())))
                result.add(new FusionResult(output, 1, FusionType.ID));
            if (result.size() >= 3) break;
        }
        return result.stream().limit(3).toList();
    }

    public static boolean fuse(DatapointHunting.HuntingData data, AttributeDefinition first,
                               AttributeDefinition second, FusionResult result) {
        int firstCost = required(first);
        int secondCost = required(second);
        if (data.shardCount(first.id()) < firstCost || data.shardCount(second.id()) < secondCost) return false;
        data.removeShards(first.id(), firstCost);
        data.removeShards(second.id(), secondCost);
        int amount = result.amount();
        int pureReptile = data.level("R45");
        if (pureReptile > 0 && (isReptile(first) || isReptile(second))
                && ThreadLocalRandom.current().nextDouble() < pureReptile * 0.02) amount *= 2;
        data.addShards(result.definition().id(), amount);
        return true;
    }

    private static boolean isReptile(AttributeDefinition definition) {
        String family = definition.family().name().toLowerCase(Locale.ROOT);
        return family.contains("reptile") || family.contains("lizard") || family.contains("serpent");
    }

    private static List<AttributeDefinition> nextById(AttributeDefinition origin, boolean chameleon,
                                                      AttributeDefinition first, AttributeDefinition second) {
        List<AttributeDefinition> sorted = AttributeRegistry.values().stream()
                .sorted(Comparator.comparingInt((AttributeDefinition value) -> value.rarity().ordinal())
                        .thenComparingInt(AttributeDefinition::numericId)).toList();
        int index = sorted.indexOf(origin);
        List<AttributeDefinition> result = new ArrayList<>();
        for (int i = index + 1; i < sorted.size() && result.size() < 3; i++) {
            AttributeDefinition candidate = sorted.get(i);
            if (candidate.id().equals(first.id()) || candidate.id().equals(second.id())) continue;
            if (chameleon || candidate.rarity() == origin.rarity() && candidate.category() == origin.category())
                result.add(candidate);
            if (!chameleon && candidate.rarity() != origin.rarity()) break;
        }
        if (!chameleon && result.size() < 3) {
            for (AttributeDefinition candidate : sorted) {
                if (result.size() >= 3 || candidate.rarity() != origin.rarity()) break;
                if (candidate.category() != origin.category() || candidate.id().equals(first.id())
                        || candidate.id().equals(second.id()) || result.contains(candidate)) continue;
                result.add(candidate);
            }
        }
        return result;
    }

    private static boolean specialMatch(AttributeDefinition output, AttributeDefinition first,
                                        AttributeDefinition second) {
        return false;
    }

    public record FusionResult(AttributeDefinition definition, int amount, FusionType type) {
    }

    public enum FusionType {ID, SPECIAL, CHAMELEON}
}
