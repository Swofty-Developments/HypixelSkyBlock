package net.swofty.type.generic.quest;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuestReward {
    private long hypixelExperience;
    private long gameExperience;
    private long coins;
    private long tokens;
    private long eventExperience;

    public static QuestReward xpOnly(long xp) {
        return QuestReward.builder().hypixelExperience(xp).build();
    }

    public static QuestReward xpAndCoins(long xp, long coins) {
        return QuestReward.builder().hypixelExperience(xp).coins(coins).build();
    }

    public boolean hasReward() {
        return hypixelExperience > 0 || gameExperience > 0 || coins > 0 || tokens > 0 || eventExperience > 0;
    }
}
