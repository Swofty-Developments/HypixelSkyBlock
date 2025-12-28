package net.swofty.type.generic.quest;

import lombok.Getter;

@Getter
public enum QuestType {
    DAILY("Daily", "Resets every day at midnight UTC"),
    WEEKLY("Weekly", "Resets every Thursday at midnight UTC"),
    SPECIAL_DAILY("Special Daily", "Special daily quests with unique rewards"),
    CHALLENGE("Challenge", "Can be completed multiple times per day, once per game");

    private final String displayName;
    private final String description;

    QuestType(String displayName, String description) {
        this.displayName = displayName;
        this.description = description;
    }
}
