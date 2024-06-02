package net.swofty.types.generic.museum;

import net.swofty.types.generic.museum.rewards.MuseumXPReward;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public enum MuseumRewards {
    REWARD_1(RewardType.MINOR, 1, new MuseumXPReward(4)),
    REWARD_2(RewardType.MINOR, 2, new MuseumXPReward(4)),
    REWARD_3(RewardType.MINOR, 3, new MuseumXPReward(4)),
    REWARD_4(RewardType.MINOR, 4, new MuseumXPReward(4)),
    ;

    private final RewardType type;
    private final int requiredDonations;
    private final List<MuseumReward> rewards;

    MuseumRewards(RewardType type, int requiredDonations, MuseumReward... rewards) {
        this.type = type;
        this.requiredDonations = requiredDonations;
        this.rewards = List.of(rewards);
    }

    public @Nullable MuseumXPReward getXPReward() {
        for (MuseumReward reward : rewards) {
            if (reward instanceof MuseumXPReward) {
                return (MuseumXPReward) reward;
            }
        }
        return null;
    }

    public enum RewardType {
        MINOR,
        INTERMEDIATE,
        MAJOR
    }
}
