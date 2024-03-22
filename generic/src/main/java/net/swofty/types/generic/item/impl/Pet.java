package net.swofty.types.generic.item.impl;

import com.mongodb.lang.Nullable;
import net.swofty.types.generic.item.Rarity;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.attribute.attributes.ItemAttributePetData;
import net.swofty.types.generic.item.items.pet.PetAbility;
import net.swofty.types.generic.skill.SkillCategory;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.user.statistics.ItemStatistic;
import net.swofty.types.generic.user.statistics.ItemStatistics;
import net.swofty.types.generic.utility.ChatColor;
import net.swofty.types.generic.utility.StringUtility;

import java.util.ArrayList;
import java.util.List;

public interface Pet extends CustomSkyBlockItem, SkullHead, Unstackable {
    List<PetAbility> getPetAbilities(SkyBlockItem instance);
    String getPetName();
    ItemStatistics getPerLevelStatistics();
    int particleId();
    SkillCategory getSkillCategory();
    default ItemStatistics getStatistics() {
        return ItemStatistics.EMPTY;
    }
    default List<String> getAbsoluteLore(@Nullable SkyBlockPlayer player, SkyBlockItem item) {
        List<String> lore = new ArrayList<>();
        Pet pet = (Pet) item.getGenericInstance();
        ItemAttributePetData.PetData petData = item.getAttributeHandler().getPetData();
        Rarity rarity = item.getAttributeHandler().getRarity();
        int level = petData.getAsLevel(rarity);
        List<PetAbility> abilities = getPetAbilities(item);

        lore.add("§8" + pet.getSkillCategory().getName() + " Pet");
        lore.add(" ");

        addPropertyInt("Magic Find", (getPerLevelStatistics().get(ItemStatistic.MAGIC_FIND) * 100.0), lore, level);
        addPropertyPercent("Crit Damage", (getPerLevelStatistics().get(ItemStatistic.CRIT_DAMAGE)), lore, level);
        addPropertyPercent("Crit Chance", (getPerLevelStatistics().get(ItemStatistic.CRIT_CHANCE)), lore, level);
        double health = getPerLevelStatistics().get(ItemStatistic.HEALTH);
        if (health > 0.0)
            lore.add("§7Health: §a+" + Math.round(health * level) + " HP");
        addPropertyInt("Strength", getPerLevelStatistics().get(ItemStatistic.STRENGTH), lore, level);
        addPropertyInt("Defense", getPerLevelStatistics().get(ItemStatistic.DEFENSE), lore, level);
        addPropertyPercent("Speed", getPerLevelStatistics().get(ItemStatistic.SPEED), lore, level);
        addPropertyInt("Intelligence", getPerLevelStatistics().get(ItemStatistic.INTELLIGENCE), lore, level);

        for (PetAbility ability : abilities) {
            lore.add(" ");
            lore.add("§6" + ability.getName());
            lore.addAll(ability.getDescription(item));
        }

        if (level < 100) {
            long experience = petData.experience;
            int nextLevel = level + 1;
            long nextLevelExperience = petData.getExperienceForLevel(nextLevel, rarity);

            double progress = ((double) experience / (double) nextLevelExperience) * 100.0;
            long amountLeft = nextLevelExperience - experience;

            lore.add(" ");
            lore.add(StringUtility.createProgressText("Progress to Level " + nextLevel,
                    progress, amountLeft));
            lore.add(StringUtility.createLineProgressBar(20, ChatColor.DARK_GREEN, progress,
                    amountLeft));
        }

        lore.add(" ");
        lore.add(rarity.getBoldedColor() + rarity.getDisplay());

        return lore;
    }

    default String getAbsoluteName(@Nullable SkyBlockPlayer player, SkyBlockItem item) {
        Rarity rarity = item.getAttributeHandler().getRarity();
        ItemAttributePetData.PetData petData = item.getAttributeHandler().getPetData();
        return "§7[Lvl " + petData.getAsLevel(rarity) + "] " + rarity.getColor() + getPetName() + " Pet";
    }

    static Boolean addPropertyInt(String name, double value, List<String> lore, int level) {
        long fin = Math.round(value * level);
        if (value != 0.0) {
            lore.add("§7" + name + ": §a" + (fin >= 0 ? "+" : "") + fin);
            return true;
        }
        return false;
    }

    public static Boolean addPropertyPercent(String name, double value, List<String> lore, int level) {
        long fin = Math.round((value * 100.0) * level);
        if (value != 0.0) {
            lore.add("§7" + name + ": §a" + (fin >= 0 ? "+" : "") + fin + "%");
            return true;
        }
        return false;
    }
}
