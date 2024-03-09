package net.swofty.types.generic.skill;

import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.types.generic.collection.CollectionCategory;
import net.swofty.types.generic.data.DataHandler;
import net.swofty.types.generic.data.datapoints.DatapointDouble;
import net.swofty.types.generic.data.datapoints.DatapointSkills;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.updater.NonPlayerItemUpdater;
import net.swofty.types.generic.region.RegionType;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.user.statistics.ItemStatistic;
import net.swofty.types.generic.user.statistics.ItemStatistics;
import net.swofty.types.generic.utility.StringUtility;

import java.util.Arrays;
import java.util.List;

public abstract class SkillCategory {
    public abstract Material getDisplayIcon();

    public abstract String getName();

    public abstract List<String> getDescription();

    public abstract SkillReward[] getRewards();

    public int getHighestLevel() {
        return getRewards().length;
    }

    public SkillReward getReward(int level) {
        return getRewards()[level - 1];
    }

    public int getLevel(double requirement) {
        int level = 0;

        for (SkillReward reward : getRewards()) {
            if (reward.requirement() <= requirement) {
                level = reward.level();
            }
        }

        return level;
    }

    public record SkillReward(int level, int requirement, Reward... unlocks) {
        public List<String> getDisplay(List<String> lore) {
            lore.add("§7Level " + StringUtility.getAsRomanNumeral(level) + " Rewards:");

            Arrays.stream(unlocks).forEach(unlock -> {
                switch (unlock.type()) {
                    case XP -> {
                        lore.add("§7  §8+§b" + ((XPReward) unlock).getXP() + " SkyBlock XP");
                    }
                    case COINS -> {
                        lore.add("§7  §8+§6" + ((CoinReward) unlock).getCoins() + " §7Coins");
                    }
                    case REGION_ACCESS -> {
                        lore.add("§7  §8+§aAccess to " + ((RegionReward) unlock).getRegion());
                    }
                    case STATS -> {
                        ItemStatistic statistic = ((StatisticReward) unlock).getStatistic();
                        lore.add("§7  §8+§b" + statistic.getColour() +
                                statistic.getSymbol() + ((StatisticReward) unlock).amountAdded()
                                + " " + statistic.getDisplayName());
                    }
                }
            });

            return lore;
        }
    }

    public abstract static class Reward {
        public abstract UnlockType type();

        public abstract void onUnlock(SkyBlockPlayer player);

        public enum UnlockType {
            REGION_ACCESS,
            COINS,
            XP,
            STATS
        }
    }

    public abstract static class CoinReward extends Reward {
        @Override
        public UnlockType type() {
            return UnlockType.COINS;
        }

        @Override
        public void onUnlock(SkyBlockPlayer player) {
            DatapointDouble coins = player.getDataHandler().get(DataHandler.Data.COINS, DatapointDouble.class);
            coins.setValue(coins.getValue() + getCoins());
        }

        public abstract int getCoins();
    }

    public abstract static class XPReward extends Reward {
        @Override
        public UnlockType type() {
            return UnlockType.XP;
        }

        @Override
        public void onUnlock(SkyBlockPlayer player) {
            // TODO
        }

        public abstract int getXP();
    }

    public abstract static class StatisticReward extends Reward {
        @Override
        public UnlockType type() {
            return UnlockType.STATS;
        }

        @Override
        public void onUnlock(SkyBlockPlayer player) {
            DatapointSkills.PlayerSkills skills = player.getSkills();
            skills.addStatistic(getStatistic(), amountAdded());
        }

        public abstract ItemStatistic getStatistic();
        public abstract Double amountAdded();
    }

    public abstract static class RegionReward extends Reward {
        @Override
        public UnlockType type() {
            return UnlockType.REGION_ACCESS;
        }

        @Override
        public void onUnlock(SkyBlockPlayer player) {
            // Handled by the region system
        }

        public abstract RegionType getRegion();
    }
}
