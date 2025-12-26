package net.swofty.type.generic.quest;

import lombok.Builder;
import lombok.Getter;
import net.swofty.type.generic.achievement.AchievementCategory;

@Getter
@Builder
public class QuestDefinition {
    private final String id;
    private final String name;
    private final String description;
    private final QuestType type;
    private final AchievementCategory category;
    private final String trigger;
    private final int goal;
    private final QuestReward reward;
    private final String headTexture;

    public String formatProgress(int current) {
        return "§b(§6" + current + "§b/§6" + goal + "§b)";
    }
}
