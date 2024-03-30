package net.swofty.types.generic.levels;

import net.swofty.types.generic.skill.SkillCategories;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;

public enum LevelsGuide {
    BEGINNER("§7You are starting on your journey through SkyBlock. Complete these tasks to get acquainted with the game",
            List.of(
            TasksSet.builder()
                    .cause(SkyBlockLevelCause.getSkillCauses(SkillCategories.COMBAT, 6), null)
                    .cause(SkyBlockLevelCause.getSkillCause(SkillCategories.COMBAT, 7), "Combat Skill IV")
                    .build()
    )),
    ;

    private final String description;
    private final List<TasksSet> tasksSets;

    LevelsGuide(String description, List<TasksSet> tasksSets) {
        this.description = description;
        this.tasksSets = tasksSets;
    }


    public static class TasksSet {
        private Map<SkyBlockLevelCauseAbstr, String> causes;
        private String name;

        public static Builder builder() {
            return new Builder();
        }

        public static class Builder {
            private Map<SkyBlockLevelCauseAbstr, String> causes;

            public Builder cause(SkyBlockLevelCauseAbstr cause, @Nullable String display) {
                causes.put(cause, display);
                return this;
            }

            public Builder cause(List<SkyBlockLevelCauseAbstr> cause, @Nullable String display) {
                cause.forEach(c -> causes.put(c, display));
                return this;
            }

            public TasksSet build() {
                TasksSet tasksSet = new TasksSet();
                tasksSet.causes = causes;
                return tasksSet;
            }
        }
    }
}
