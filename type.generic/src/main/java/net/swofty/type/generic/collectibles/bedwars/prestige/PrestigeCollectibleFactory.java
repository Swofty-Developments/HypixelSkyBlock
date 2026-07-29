package net.swofty.type.generic.collectibles.bedwars.prestige;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import net.swofty.type.generic.collectibles.CollectibleCategory;
import net.swofty.type.generic.collectibles.CollectibleCurrency;
import net.swofty.type.generic.collectibles.CollectibleDefinition;
import net.swofty.type.generic.collectibles.CollectibleGamemode;
import net.swofty.type.generic.collectibles.CollectibleRarity;
import net.swofty.type.generic.collectibles.CollectibleUnlockMethod;
import net.swofty.type.generic.collectibles.CollectibleUnlockRequirement;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class PrestigeCollectibleFactory {

    public static List<CollectibleDefinition> definitions() {
        List<CollectibleDefinition> definitions = new ArrayList<>();
        int sortIndex = 0;
        for (BedWarsPrestigeDefinitions.Scheme scheme : BedWarsPrestigeDefinitions.SCHEMES) {
            definitions.add(definition(
                CollectibleCategory.PRESTIGE_SCHEMES,
                BedWarsPrestigeDefinitions.schemeCollectibleId(scheme.id()),
                scheme.name(),
                scheme.material(),
                sortIndex++,
                scheme.requiredLevel(),
                Map.of("prestigeId", scheme.id())
            ));
        }

        sortIndex = 0;
        for (BedWarsPrestigeDefinitions.Star star : BedWarsPrestigeDefinitions.STARS) {
            definitions.add(definition(
                CollectibleCategory.PRESTIGE_STARS,
                BedWarsPrestigeDefinitions.starCollectibleId(star.id()),
                star.name(),
                star.material(),
                sortIndex++,
                star.requiredLevel(),
                Map.of("symbol", star.symbol(), "prestigeId", star.id())
            ));
        }

        sortIndex = 0;
        for (BedWarsPrestigeDefinitions.Bracket bracket : BedWarsPrestigeDefinitions.BRACKETS) {
            definitions.add(definition(
                CollectibleCategory.PRESTIGE_BRACKETS,
                BedWarsPrestigeDefinitions.bracketCollectibleId(bracket.id()),
                bracket.name(),
                bracket.material(),
                sortIndex++,
                bracket.requiredLevel(),
                Map.of("open", bracket.open(), "close", bracket.close(), "prestigeId", bracket.id())
            ));
        }
        return List.copyOf(definitions);
    }

    private static CollectibleDefinition definition(
        CollectibleCategory category,
        String id,
        String name,
        net.minestom.server.item.Material material,
        int sortIndex,
        int requiredLevel,
        Map<String, String> customData
    ) {
        CollectibleUnlockRequirement requirement = requiredLevel <= 0
            ? CollectibleUnlockRequirement.free()
            : new CollectibleUnlockRequirement(
            CollectibleUnlockMethod.BEDWARS_LEVEL,
            null,
            CollectibleCurrency.BEDWARS_TOKENS,
            (long) requiredLevel,
            null,
            null,
            null
        );

        return new CollectibleDefinition(
            id,
            CollectibleGamemode.BEDWARS,
            category,
            name,
            material,
            null,
            List.of(),
            null,
            customData,
            rarity(requiredLevel),
            sortIndex,
            id,
            requirement
        );
    }

    private static CollectibleRarity rarity(int requiredLevel) {
        if (requiredLevel >= 5000) return CollectibleRarity.MYTHIC;
        if (requiredLevel >= 1000) return CollectibleRarity.LEGENDARY;
        if (requiredLevel > 0) return CollectibleRarity.RARE;
        return CollectibleRarity.COMMON;
    }
}
