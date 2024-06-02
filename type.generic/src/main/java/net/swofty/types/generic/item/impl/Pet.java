package net.swofty.types.generic.item.impl;

import com.mongodb.lang.Nullable;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import net.swofty.types.generic.data.datapoints.DatapointPetData;
import net.swofty.types.generic.item.ItemType;
import net.swofty.types.generic.item.Rarity;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.attribute.attributes.ItemAttributePetData;
import net.swofty.types.generic.item.items.pet.PetAbility;
import net.swofty.types.generic.skill.SkillCategories;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.user.statistics.ItemStatistic;
import net.swofty.types.generic.user.statistics.ItemStatistics;
import net.swofty.types.generic.utility.ChatColor;
import net.swofty.types.generic.utility.StringUtility;

import java.util.ArrayList;
import java.util.List;

public interface Pet extends CustomSkyBlockItem, SkullHead, TrackedUniqueItem, Interactable {
    List<PetAbility> getPetAbilities(SkyBlockItem instance);
    String getPetName();
    ItemStatistics getBaseStatistics();
    ItemStatistics getPerLevelStatistics(Rarity rarity);
    int particleId();
    SkillCategories getSkillCategory();
    default ItemStatistics getStatistics(SkyBlockItem instance) {
        return ItemStatistics.empty();
    }
    default void onRightInteract(SkyBlockPlayer player, SkyBlockItem item) {
        interact(player, item);
    }

    default void onLeftInteract(SkyBlockPlayer player, SkyBlockItem item) {
        interact(player, item);
    }

    private void interact(SkyBlockPlayer player, SkyBlockItem item) {
        DatapointPetData.UserPetData petData = player.getPetData();
        ItemType type = item.getAttributeHandler().getItemTypeAsType();
        Rarity rarity = item.getAttributeHandler().getRarity();

        if (petData.getPet(type) != null) {
            player.sendMessage("§cYou already have a pet of this type.");
            return;
        }

        petData.addPet(item);
        player.setItemInHand(null);
        player.sendMessage("§aSuccessfully added " + rarity.getColor() + item.getDisplayName() + " §ato your pet menu!");
        player.playSound(Sound.sound()
                .type(Key.key("minecraft", "entity.experience_orb.pickup"))
                .volume(1f)
                .pitch(1f)
                .build());
    }

    default List<String> getAbsoluteLore(@Nullable SkyBlockPlayer player, SkyBlockItem item) {
        List<String> lore = new ArrayList<>();
        Pet pet = (Pet) item.getGenericInstance();
        ItemAttributePetData.PetData petData = item.getAttributeHandler().getPetData();
        Rarity rarity = item.getAttributeHandler().getRarity();
        int level = petData.getAsLevel(rarity);
        List<PetAbility> abilities = getPetAbilities(item);

        lore.add("§8" + pet.getSkillCategory().asCategory().getName() + " Pet");
        lore.add(" ");

        addPropertyInt("Magic Find", (getBaseStatistics().getOverall(ItemStatistic.MAGIC_FIND) +
                getPerLevelStatistics(rarity).getOverall(ItemStatistic.MAGIC_FIND) * 100.0 * level), lore);
        addPropertyPercent("Crit Damage", (getBaseStatistics().getOverall(ItemStatistic.CRIT_DAMAGE) +
                getPerLevelStatistics(rarity).getOverall(ItemStatistic.CRIT_DAMAGE) * level), lore);
        addPropertyPercent("Crit Chance", (getBaseStatistics().getOverall(ItemStatistic.CRIT_CHANCE) +
                getPerLevelStatistics(rarity).getOverall(ItemStatistic.CRIT_CHANCE) * level), lore);
        addPropertyPercent("Health", (getBaseStatistics().getOverall(ItemStatistic.HEALTH) +
                getPerLevelStatistics(rarity).getOverall(ItemStatistic.HEALTH) * level), lore);
        addPropertyInt("Strength", getBaseStatistics().getOverall(ItemStatistic.STRENGTH) +
                getPerLevelStatistics(rarity).getOverall(ItemStatistic.STRENGTH) * level, lore);
        addPropertyInt("Defense", getBaseStatistics().getOverall(ItemStatistic.DEFENSE) +
                getPerLevelStatistics(rarity).getOverall(ItemStatistic.DEFENSE) * level, lore);
        addPropertyInt("Speed", getBaseStatistics().getOverall(ItemStatistic.SPEED) +
                getPerLevelStatistics(rarity).getOverall(ItemStatistic.SPEED) * level, lore);
        addPropertyInt("Intelligence", getBaseStatistics().getOverall(ItemStatistic.INTELLIGENCE) +
                getPerLevelStatistics(rarity).getOverall(ItemStatistic.INTELLIGENCE) * level, lore);

        for (PetAbility ability : abilities) {
            lore.add(" ");
            lore.add("§6" + ability.getName());
            lore.addAll(ability.getDescription(item));
        }

        if (level < 100) {
            double experience = petData.getExperienceInCurrentLevel(rarity);
            int nextLevel = level + 1;
            long nextLevelExperience = petData.getExperienceForLevel(nextLevel, rarity);

            lore.add(" ");
            lore.add(StringUtility.createProgressText("Progress to Level " + nextLevel,
                    experience, nextLevelExperience));
            lore.add(StringUtility.createLineProgressBar(20, ChatColor.DARK_GREEN, experience,
                    nextLevelExperience));
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

    static void addPropertyInt(String name, double value, List<String> lore) {
        if (value != 0.0) {
            lore.add("§7" + name + ": §a" + (value >= 0 ? "+" : "") + value);
        }
    }

    static void addPropertyPercent(String name, double value, List<String> lore) {
        if (value != 0.0) {
            lore.add("§7" + name + ": §a" + (value >= 0 ? "+" : "") + value + "%");
        }
    }
}
