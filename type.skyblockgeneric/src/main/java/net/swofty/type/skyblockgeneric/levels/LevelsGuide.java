package net.swofty.type.skyblockgeneric.levels;

import lombok.Getter;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.commons.skyblock.item.ItemType;
import net.swofty.type.generic.gui.v2.View;
import net.swofty.type.generic.gui.inventory.ItemStackCreator;
import net.swofty.type.skyblockgeneric.gui.inventories.sbmenu.levels.starter.GUIStarterAccessories;
import net.swofty.type.skyblockgeneric.gui.inventories.sbmenu.levels.starter.GUIStarterSkills;
import net.swofty.type.skyblockgeneric.levels.abstr.SkyBlockLevelCauseAbstr;
import net.swofty.type.skyblockgeneric.skill.SkillCategories;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

@Getter
public enum LevelsGuide {
    STARTER("§8New Player", "§7You are starting on your journey through SkyBlock. Complete these tasks to get acquainted with the game",
            Material.LIME_STAINED_GLASS_PANE, List.of(
            TasksSet.builder(ItemStackCreator.getStack("Skills", Material.DIAMOND_SWORD, 1, ""), new GUIStarterSkills(), (player) -> List.of(
                    "§7Level up your Skills.",
                    " ",
                    player.getSkills().getCurrentLevel(SkillCategories.FARMING) >= 4 ? "§a✔ §8Farming Skill IV" : "§c✖ §fFarming Skill IV",
                    player.getSkills().getCurrentLevel(SkillCategories.MINING) >= 4 ? "§a✔ §8Mining Skill IV" : "§c✖ §fMining Skill IV",
                    player.getSkills().getCurrentLevel(SkillCategories.COMBAT) >= 4 ? "§a✔ §8Combat Skill IV" : "§c✖ §fCombat Skill IV",
                    player.getSkills().getCurrentLevel(SkillCategories.FORAGING) >= 4 ? "§a✔ §8Foraging Skill IV" : "§c✖ §fForaging Skill IV",
                    player.getSkills().getCurrentLevel(SkillCategories.FISHING) >= 4 ? "§a✔ §8Fishing Skill IV" : "§c✖ §fFishing Skill IV",
                    player.getSkills().getCurrentLevel(SkillCategories.ENCHANTING) >= 4 ? "§a✔ §8Enchanting Skill IV" : "§c✖ §fEnchanting Skill IV"
                    ))
                    .cause(SkyBlockLevelCause.getSkillCauses(SkillCategories.COMBAT, 6), null)
                    .cause(SkyBlockLevelCause.getSkillCause(SkillCategories.COMBAT, 7), "Combat Skill IV")
                    .build(),
            TasksSet.builder(ItemStackCreator.getStackHead("Accessories", "1a11a7f11bcd5784903c5201d08261c4df8379109d6e611c1cd3ededf031afed"), new GUIStarterAccessories(), (player) -> List.of(
                    "§7Obtain unique §aAccessories §7in your",
                    "§aAccessory Bag§7."
                    ))
                    .cause(SkyBlockLevelCause.getAccessoryCause(ItemType.FARMING_TALISMAN), null)
                    .cause(SkyBlockLevelCause.getAccessoryCause(ItemType.ZOMBIE_TALISMAN), null)
                    .cause(SkyBlockLevelCause.getAccessoryCause(ItemType.SKELETON_TALISMAN), null)
                    .cause(SkyBlockLevelCause.getAccessoryCause(ItemType.VILLAGE_AFFINITY_TALISMAN), null)
                    .cause(SkyBlockLevelCause.getAccessoryCause(ItemType.MINE_AFFINITY_TALISMAN), null)
                    .build()
    )),
    ;

    private final String title;
    private final String description;
    private final Material glassMaterial;
    private final List<TasksSet> tasksSets;

    LevelsGuide(String title, String description, Material glassMaterial, List<TasksSet> tasksSets) {
        this.title = title;
        this.description = description;
        this.glassMaterial = glassMaterial;
        this.tasksSets = tasksSets;
    }

    @Getter
    public static class TasksSet {
        private Map<SkyBlockLevelCauseAbstr, String> causes;
        private ItemStack.Builder material;
        private View<?> guiToOpen;
        private Function<SkyBlockPlayer, List<String>> display;

        public static Builder builder(ItemStack.Builder material, View<?> guiToOpen, Function<SkyBlockPlayer, List<String>> display) {
            return new Builder(material, guiToOpen, display);
        }

        public static class Builder {
            private final Map<SkyBlockLevelCauseAbstr, String> causes = new HashMap<>();
            private final ItemStack.Builder material;
            private final View<?> guiToOpen;
            private final Function<SkyBlockPlayer, List<String>> display;

            public Builder(ItemStack.Builder material, View<?> guiToOpen, Function<SkyBlockPlayer, List<String>> display) {
                this.material = material;
                this.guiToOpen = guiToOpen;
                this.display = display;
            }

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
                tasksSet.material = material;
                tasksSet.guiToOpen = guiToOpen;
                tasksSet.display = display;
                return tasksSet;
            }
        }
    }
}
