package net.swofty.type.skyblockgeneric.item.handlers.pet.abilities;

import net.swofty.commons.skyblock.statistics.ItemStatistic;
import net.swofty.commons.skyblock.statistics.ItemStatistics;
import net.swofty.type.skyblockgeneric.item.SkyBlockItem;
import net.swofty.type.skyblockgeneric.item.handlers.pet.PetAbility;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;
import net.swofty.type.skyblockgeneric.utility.RarityValue;

import java.util.Arrays;
import java.util.List;

public class HiveAbility implements PetAbility {
    private static final RarityValue<Integer> INTELLIGENCE = new RarityValue<>(3, 6, 6, 10, 10, 0);
    private static final RarityValue<Integer> STRENGTH = new RarityValue<>(3, 5, 5, 8, 8, 0);
    private static final RarityValue<Integer> DEFENSE = new RarityValue<>(2, 3, 3, 5, 5, 0);

    @Override
    public String getName() {
        return "Hive";
    }

    @Override
    public List<String> getDescription(SkyBlockItem instance) {
        var rarity = instance.getAttributeHandler().getRarity();

        return Arrays.asList(
                "§7For each player within §a25 §7 blocks:",
                " §7Gain §b+" + INTELLIGENCE.getForRarity(rarity) + "✎ Intelligence",
                " §7Gain §c+" + STRENGTH.getForRarity(rarity) + "❁ Strength",
                " §7Gain §a+" + DEFENSE.getForRarity(rarity) + "❈ Defense",
                "§8Max 15 players"
        );
    }

    @Override
    public ItemStatistics getStatistics(SkyBlockPlayer player, SkyBlockItem pet) {
        var rarity = pet.getAttributeHandler().getRarity();
        int count = (int) player.getInstance().getPlayers().stream()
                .filter(p -> p != player && p.getPosition().distance(player.getPosition()) <= 25)
                .count();
        count = Math.min(count, 15);

        int intel = INTELLIGENCE.getForRarity(rarity) * count;
        int str = STRENGTH.getForRarity(rarity) * count;
        int def = DEFENSE.getForRarity(rarity) * count;

        return ItemStatistics.builder()
                .withBase(ItemStatistic.INTELLIGENCE, (double) intel)
                .withBase(ItemStatistic.STRENGTH, (double) str)
                .withBase(ItemStatistic.DEFENSE, (double) def)
                .build();
    }
}
