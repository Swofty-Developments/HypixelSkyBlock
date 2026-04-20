package net.swofty.type.skyblockgeneric.fishing;

import java.util.List;

public record SeaCreatureDefinition(
    String id,
    int requiredFishingLevel,
    double skillXp,
    List<String> tags
) {
}
