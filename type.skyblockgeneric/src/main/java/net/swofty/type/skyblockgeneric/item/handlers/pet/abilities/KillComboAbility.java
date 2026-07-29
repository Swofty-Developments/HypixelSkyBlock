package net.swofty.type.skyblockgeneric.item.handlers.pet.abilities;

import net.swofty.commons.skyblock.item.Rarity;
import net.swofty.commons.skyblock.statistics.ItemStatistic;
import net.swofty.commons.skyblock.statistics.ItemStatistics;
import net.swofty.type.skyblockgeneric.entity.mob.SkyBlockMob;
import net.swofty.type.skyblockgeneric.item.SkyBlockItem;
import net.swofty.type.skyblockgeneric.item.handlers.pet.abstr.KillEventPetAbility;
import net.swofty.type.skyblockgeneric.item.handlers.pet.abstr.PetAbility;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;
import net.swofty.type.skyblockgeneric.utility.RarityValue;

import java.util.Arrays;
import java.util.List;

import static net.swofty.commons.StringUtility.decimalify;

public class KillComboAbility implements PetAbility, KillEventPetAbility {
    private static final RarityValue<Double> MAGIC_FIND_5 = new RarityValue<>(1.0, 1.0, 2.0, 2.0, 3.0, 0.0);
    private static final RarityValue<Double> MAGIC_FIND_15 = new RarityValue<>(1.0, 1.0, 2.0, 2.0, 3.0, 0.0);
    private static final RarityValue<Double> MAGIC_FIND_25 = new RarityValue<>(1.0, 1.0, 2.0, 2.0, 3.0, 0.0);

    private static final RarityValue<Integer> COINS_10 = new RarityValue<>(2, 4, 6, 8, 10, 0);
    private static final RarityValue<Integer> COINS_30 = new RarityValue<>(2, 4, 6, 8, 10, 0);

    private static final RarityValue<Integer> COMBAT_WISDOM_20 = new RarityValue<>(5, 7, 9, 12, 15, 0);

    private static final double[] BASE_DURATIONS = {8.02, 6.02, 4.02, 3.02, 3.01, 2.01};
    private static final double[] DURATION_PER_LEVEL = {0.02, 0.02, 0.02, 0.02, 0.01, 0.01};
    private static final int[] THRESHOLDS = {5, 10, 15, 20, 25, 30};

    @Override
    public String getName() {
        return "Kill Combo";
    }

    @Override
    public List<String> getDescription(SkyBlockItem instance) {
        Rarity rarity = instance.getAttributeHandler().getRarity();
        int level = instance.getAttributeHandler().getPetData().getAsLevel(rarity);

        String d5 = decimalify(BASE_DURATIONS[0] + level * DURATION_PER_LEVEL[0], 2);
        String d10 = decimalify(BASE_DURATIONS[1] + level * DURATION_PER_LEVEL[1], 2);
        String d15 = decimalify(BASE_DURATIONS[2] + level * DURATION_PER_LEVEL[2], 2);
        String d20 = decimalify(BASE_DURATIONS[3] + level * DURATION_PER_LEVEL[3], 2);
        String d25 = decimalify(BASE_DURATIONS[4] + level * DURATION_PER_LEVEL[4], 2);
        String d30 = decimalify(BASE_DURATIONS[5] + level * DURATION_PER_LEVEL[5], 2);

        String mf5 = decimalify(MAGIC_FIND_5.getForRarity(rarity), 0);
        String mf15 = decimalify(MAGIC_FIND_15.getForRarity(rarity), 0);
        String mf25 = decimalify(MAGIC_FIND_25.getForRarity(rarity), 0);
        int coins10 = COINS_10.getForRarity(rarity);
        int coins30 = COINS_30.getForRarity(rarity);
        int wisdom20 = COMBAT_WISDOM_20.getForRarity(rarity);

        return Arrays.asList(
                "§7Gain buffs for combo kills. Effects stack as",
                "§7you increase your combo.",
                "",
                " §a5 Combo §8(lasts " + d5 + "s) §b+" + mf5 + "% ✯ Magic Find",
                " §a10 Combo §8(lasts " + d10 + "s) §6+" + coins10 + " coins per kill",
                " §a15 Combo §8(lasts " + d15 + "s) §b+" + mf15 + "% ✯ Magic Find",
                " §a20 Combo §8(lasts " + d20 + "s) §3+" + wisdom20 + " ☯ Combat Wisdom",
                " §a25 Combo §8(lasts " + d25 + "s) §b+" + mf25 + "% ✯ Magic Find",
                " §a30 Combo §8(lasts " + d30 + "s) §6+" + coins30 + " coins per kill"
        );
    }

    @Override
    public ItemStatistics getStatistics(SkyBlockPlayer player, SkyBlockItem pet) {
        if (player.getKillComboCount() <= 0) return ItemStatistics.empty();

        Rarity rarity = pet.getAttributeHandler().getRarity();
        int level = pet.getAttributeHandler().getPetData().getAsLevel(rarity);
        int combo = player.getKillComboCount();

        double activeDuration = 0;
        for (int i = 0; i < THRESHOLDS.length; i++) {
            if (combo >= THRESHOLDS[i]) {
                activeDuration = BASE_DURATIONS[i] + level * DURATION_PER_LEVEL[i];
            }
        }

        if (activeDuration <= 0
                || System.currentTimeMillis() - player.getLastKillTime() > (long) (activeDuration * 1000)) {
            player.setKillComboCount(0);
            return ItemStatistics.empty();
        }

        double totalMf = 0;
        int totalWisdom = 0;

        if (combo >= 5) totalMf += MAGIC_FIND_5.getForRarity(rarity);
        if (combo >= 15) totalMf += MAGIC_FIND_15.getForRarity(rarity);
        if (combo >= 25) totalMf += MAGIC_FIND_25.getForRarity(rarity);
        if (combo >= 20) totalWisdom += COMBAT_WISDOM_20.getForRarity(rarity);

        return ItemStatistics.builder()
                .withBase(ItemStatistic.MAGIC_FIND, totalMf)
                .withBase(ItemStatistic.COMBAT_WISDOM, (double) totalWisdom)
                .build();
    }

    @Override
    public void onPlayerKilledMob(SkyBlockPlayer player, SkyBlockItem pet, SkyBlockMob mob) {
        player.setKillComboCount(player.getKillComboCount() + 1);
        player.setLastKillTime(System.currentTimeMillis());

        Rarity rarity = pet.getAttributeHandler().getRarity();

        if (player.getKillComboCount() >= 10) {
            player.addCoins(COINS_10.getForRarity(rarity));
        }
        if (player.getKillComboCount() >= 30) {
            player.addCoins(COINS_30.getForRarity(rarity));
        }
    }
}
