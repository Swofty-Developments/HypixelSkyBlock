package net.swofty.type.skyblockgeneric.item.handlers.pet.abilities;

import net.swofty.commons.StringUtility;
import net.swofty.commons.skyblock.item.Rarity;
import net.swofty.commons.skyblock.statistics.ItemStatistic;
import net.swofty.commons.skyblock.statistics.ItemStatistics;
import net.swofty.type.skyblockgeneric.item.SkyBlockItem;
import net.swofty.type.skyblockgeneric.item.handlers.pet.abstr.PetAbility;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;
import net.swofty.type.skyblockgeneric.utility.RarityValue;
import net.minestom.server.instance.Instance;

import java.util.Arrays;
import java.util.List;

public class HiveAbility implements PetAbility {
    private static final Integer INTELLIGENCE = 1;
    private static final Integer STRENGTH = 1;
    private static final Integer DEFENSE = 1;

    private static final RarityValue<Double> INTELLIGENCE_BONUSES = new RarityValue<>(0.02, 0.05, 0.05, 0.09, 0.09, 0.0);
    private static final RarityValue<Double> STRENGTH_BONUSES = new RarityValue<>(0.02, 0.04, 0.04, 0.07, 0.07, 0.0);
    private static final RarityValue<Double> DEFENSE_BONUSES = new RarityValue<>(0.01, 0.02, 0.02, 0.04, 0.04, 0.0);


    @Override
    public String getName() {
        return "Hive";
    }

    @Override
    public List<String> getDescription(SkyBlockItem instance) {
        Rarity rarity = instance.getAttributeHandler().getRarity();
        int level = instance.getAttributeHandler().getPetData().getAsLevel(rarity);
        double perPlayerIntel = INTELLIGENCE + INTELLIGENCE_BONUSES.getForRarity(rarity) * level;
        double perPlayerStr = STRENGTH + STRENGTH_BONUSES.getForRarity(rarity) * level;
        double perPlayerDef = DEFENSE + DEFENSE_BONUSES.getForRarity(rarity) * level;

        return Arrays.asList(
                "§7For each player within §a25 §7 blocks:",
                " §7Gain " + ItemStatistic.INTELLIGENCE.getDisplayColor() + "+" + StringUtility.decimalify(perPlayerIntel, 2) + ItemStatistic.INTELLIGENCE.getFullDisplayName(),
                " §7Gain " + ItemStatistic.STRENGTH.getDisplayColor() + "+" + StringUtility.decimalify(perPlayerStr, 2) + ItemStatistic.STRENGTH.getFullDisplayName(),
                " §7Gain " + ItemStatistic.DEFENSE.getDisplayColor() + "+" + StringUtility.decimalify(perPlayerDef, 2) + ItemStatistic.DEFENSE.getFullDisplayName(),
                "§8Max 15 players"
        );
    }

    @Override
    public ItemStatistics getStatistics(SkyBlockPlayer player, SkyBlockItem pet) {
        var rarity = pet.getAttributeHandler().getRarity();
        int level = pet.getAttributeHandler().getPetData().getAsLevel(rarity);

        Instance instance = player.getInstance();
        int count = 0;
        if (instance != null) {
            count = Math.min((int) instance.getPlayers().stream()
                    .filter(p -> p != player && p.getPosition().distance(player.getPosition()) <= 25)
                    .count(), 15);
        }

        double perPlayerIntel = INTELLIGENCE + INTELLIGENCE_BONUSES.getForRarity(rarity) * level;
        double perPlayerStr = STRENGTH + STRENGTH_BONUSES.getForRarity(rarity) * level;
        double perPlayerDef = DEFENSE + DEFENSE_BONUSES.getForRarity(rarity) * level;

        return ItemStatistics.builder()
                .withBase(ItemStatistic.INTELLIGENCE, perPlayerIntel * count)
                .withBase(ItemStatistic.STRENGTH, perPlayerStr * count)
                .withBase(ItemStatistic.DEFENSE, perPlayerDef * count)
                .build();
    }
}
