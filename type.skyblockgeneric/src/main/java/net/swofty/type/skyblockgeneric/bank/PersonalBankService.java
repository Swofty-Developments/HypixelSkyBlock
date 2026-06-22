package net.swofty.type.skyblockgeneric.bank;

import net.swofty.commons.skyblock.item.ItemType;
import net.swofty.type.skyblockgeneric.data.SkyBlockDataHandler;
import net.swofty.type.skyblockgeneric.data.datapoints.DatapointBankData;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;

public final class PersonalBankService {
    public static final int[] COLLECTION = {15_000, 50_000, 100_000, 250_000};
    public static final double[] COST = {0, 200_000, 1_000_000, 5_000_000};
    public static final long[] COOLDOWN = {3_600_000L, 1_200_000L, 300_000L, 0L};

    public static DatapointBankData.BankData data(SkyBlockPlayer player) {
        return player.getSkyblockDataHandler().get(SkyBlockDataHandler.Data.BANK_DATA, DatapointBankData.class).getValue();
    }

    public static boolean isUnlocked(SkyBlockPlayer player) {
        return player.getCollection().get(ItemType.EMERALD) >= COLLECTION[0];
    }

    public static long remaining(SkyBlockPlayer player) {
        DatapointBankData.BankData data = data(player);
        long end = data.getLastRemoteBankUse() + COOLDOWN[data.getPersonalBankLevel()];
        return Math.max(0, end - System.currentTimeMillis());
    }

    public static boolean tryUpgrade(SkyBlockPlayer player) {
        DatapointBankData.BankData data = data(player);
        int next = data.getPersonalBankLevel() + 1;
        if (next >= COLLECTION.length || player.getCollection().get(ItemType.EMERALD) < COLLECTION[next]
            || player.getCoins() < COST[next]) return false;
        player.removeCoins(COST[next]);
        data.setPersonalBankLevel(next);
        return true;
    }
}
