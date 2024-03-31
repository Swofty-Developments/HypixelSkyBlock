package net.swofty.types.generic.levels;

import lombok.Getter;
import net.minestom.server.item.Material;
import net.swofty.types.generic.item.ItemType;
import net.swofty.types.generic.levels.abstr.SkyBlockLevelCauseAbstr;
import net.swofty.types.generic.skill.SkillCategories;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.utility.StringUtility;

import java.util.List;

@Getter
public enum SkyBlockEmblems {
    SKILLS(Material.DIAMOND_SWORD, List.of("§7These symbols are related to", "§7levelling up your Skills!"), List.of(
            new SkyBlockEmblem("Mining Pickaxe", Material.STONE_PICKAXE, "§7⸕",
                    SkyBlockLevelCause.getSkillCause(SkillCategories.MINING, 50)),
            new SkyBlockEmblem("Mining Master", Material.GOLDEN_PICKAXE, "§6⸕",
                    SkyBlockLevelCause.getSkillCause(SkillCategories.MINING, 60))
    )),
    LEVELING(Material.SKELETON_SKULL, List.of("§7These symbols are unlocked by", "§7leveling up your SkyBlock Level."), List.of(
            new SkyBlockEmblem("Diamond", Material.NAME_TAG, "§7◆",
                    SkyBlockLevelCause.getLevelCause(10)),
            new SkyBlockEmblem("Spade", Material.NAME_TAG, "§7♠",
                    SkyBlockLevelCause.getLevelCause(20))
    )),
    ;

    private final Material displayMaterial;
    private final List<String> description;
    private final List<SkyBlockEmblem> emblems;

    SkyBlockEmblems(Material displayMaterial, List<String> description, List<SkyBlockEmblem> emblems) {
        this.displayMaterial = displayMaterial;
        this.description = description;
        this.emblems = emblems;
    }

    public int amountUnlocked(SkyBlockPlayer player) {
        int amount = 0;
        for (SkyBlockEmblem emblem : emblems) {
            if (player.hasUnlockedXPCause(emblem.cause())) {
                amount++;
            }
        }
        return amount;
    }

    @Override
    public String toString() {
        return StringUtility.toNormalCase(name());
    }

    public static SkyBlockEmblems getCategoryFromEmblem(SkyBlockEmblem emblem) {
        for (SkyBlockEmblems emblems : values()) {
            for (SkyBlockEmblem emblem1 : emblems.emblems) {
                if (emblem1.equals(emblem)) {
                    return emblems;
                }
            }
        }
        return null;
    }

    public record SkyBlockEmblem(String displayName, Material displayMaterial, String emblem, SkyBlockLevelCauseAbstr cause) {}
}
