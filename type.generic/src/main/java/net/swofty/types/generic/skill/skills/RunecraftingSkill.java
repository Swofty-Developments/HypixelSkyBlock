package net.swofty.types.generic.skill.skills;

import net.minestom.server.item.Material;
import net.swofty.types.generic.skill.SkillCategories;
import net.swofty.types.generic.skill.SkillCategory;
import net.swofty.types.generic.user.SkyBlockPlayer;

import java.util.List;

public class RunecraftingSkill extends SkillCategory {
    @Override
    public Material getDisplayIcon() {
        return Material.MAGMA_CREAM;
    }

    @Override
    public String getName() {
        return "Runecrafting";
    }

    @Override
    public List<String> getDescription() {
        return List.of("§7Slay bosses and runic mobs, and",
                "§7fuse runes to earn Runecrafting XP!");
    }

    @Override
    public SkillReward[] getRewards() {
        return List.of(
                new SkillReward(1, 50,
                        new RuneReward() {
                            @Override
                            public int getRuneLevel() {
                                return 1;
                            }
                        }
                )
        ).toArray(new SkillReward[0]);
    }

    public static Integer getUnlockedRune(SkyBlockPlayer player) {
        int level = player.getSkills().getCurrentLevel(SkillCategories.RUNECRAFTING);
        SkillReward reward = SkillCategories.RUNECRAFTING.asCategory().getReward(level);
        for (Reward unlock : reward.unlocks()) {
            if (unlock instanceof RuneReward) {
                return ((RuneReward) unlock).getRuneLevel();
            }
        }
        return 0;
    }

    public abstract class RuneReward extends Reward {

        @Override
        public UnlockType type() {
            return UnlockType.RUNE;
        }

        @Override
        public void onUnlock(SkyBlockPlayer player) {}

        public abstract int getRuneLevel();
    }
}
