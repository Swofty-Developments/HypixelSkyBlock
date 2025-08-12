package net.swofty.type.skyblockgeneric.levels;

import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.commons.item.ItemType;
import net.swofty.type.generic.gui.inventory.HypixelInventoryGUI;
import net.swofty.type.generic.gui.inventories.sbmenu.levels.starter.GUIStarterAccessories;
import net.swofty.type.generic.gui.inventories.sbmenu.levels.starter.GUIStarterSkills;
import net.swofty.type.generic.levels.abstr.SkyBlockLevelCauseAbstr;
import net.swofty.type.generic.skill.SkillCategories;
import net.swofty.type.generic.user.HypixelPlayer;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

public enum LevelsGuide {
    STARTER("§8New Player", "§7You are starting on your journey through SkyBlock. Complete these tasks to get acquainted with the game",
            Material.LIME_STAINED_GLASS_PANE, List.of(
            TasksSet.builder(ItemStack.builder(Material.DIAMOND_SWORD).build(), new GUIStarterSkills(), (player) -> List.of(
                    "§7Level up your Skills.",
                    " ",
                    player.getSkills().getCurrentLevel(SkillCategories.FARMING) >= 4 ? "§aFarming Skill IV" : "§cFarming Skill IV",
                    player.getSkills().getCurrentLevel(SkillCategories.MINING) >= 4 ? "§aMining Skill IV" : "§cMining Skill IV",
                    player.getSkills().getCurrentLevel(SkillCategories.COMBAT) >= 4 ? "§aCombat Skill IV" : "§cCombat Skill IV",
                    player.getSkills().getCurrentLevel(SkillCategories.FORAGING) >= 4 ? "§aForaging Skill IV" : "§cForaging Skill IV",
                    player.getSkills().getCurrentLevel(SkillCategories.FISHING) >= 4 ? "§aFishing Skill IV" : "§cFishing Skill IV",
                    player.getSkills().getCurrentLevel(SkillCategories.ENCHANTING) >= 4 ? "§aEnchanting Skill IV" : "§cEnchanting Skill IV"
                    ))
                    .cause(SkyBlockLevelCause.getSkillCauses(SkillCategories.COMBAT, 6), null)
                    .cause(SkyBlockLevelCause.getSkillCause(SkillCategories.COMBAT, 7), "Combat Skill IV")
                    .build(),
            TasksSet.builder(ItemStack.builder(Material.SKELETON_SKULL).build(), new GUIStarterAccessories(), (player) -> List.of(
                    "§7Obtain unique §aAccessories §7in your",
                    "§aAccessory Bag§7."
                    ))
                    .cause(SkyBlockLevelCause.getAccessoryCause(ItemType.ZOMBIE_TALISMAN), null)
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


    public static class TasksSet {
        private Map<SkyBlockLevelCauseAbstr, String> causes;
        private String name;
        private ItemStack material;
        private HypixelInventoryGUI guiToOpen;
        private Function<HypixelPlayer, List<String>> display;

        public static Builder builder(ItemStack material, HypixelInventoryGUI guiToOpen, Function<HypixelPlayer, List<String>> display) {
            return new Builder(material, guiToOpen, display);
        }

        public static class Builder {
            private Map<SkyBlockLevelCauseAbstr, String> causes;
            private ItemStack material;
            private HypixelInventoryGUI guiToOpen;
            private Function<HypixelPlayer, List<String>> display;

            public Builder(ItemStack material, HypixelInventoryGUI guiToOpen, Function<HypixelPlayer, List<String>> display) {
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
