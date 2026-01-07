package net.swofty.type.skyblockgeneric.item.components;

import lombok.Getter;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import net.minestom.server.particle.Particle;
import net.swofty.commons.ChatColor;
import net.swofty.commons.StringUtility;
import net.swofty.commons.skyblock.item.ItemType;
import net.swofty.commons.skyblock.item.Rarity;
import net.swofty.commons.skyblock.item.attribute.attributes.ItemAttributePetData;
import net.swofty.commons.skyblock.statistics.ItemStatistic;
import net.swofty.commons.skyblock.statistics.ItemStatistics;
import net.swofty.type.skyblockgeneric.data.datapoints.DatapointPetData;
import net.swofty.type.skyblockgeneric.item.SkyBlockItem;
import net.swofty.type.skyblockgeneric.item.SkyBlockItemComponent;
import net.swofty.type.skyblockgeneric.item.handlers.lore.LoreConfig;
import net.swofty.type.skyblockgeneric.item.handlers.pet.KatUpgrade;
import net.swofty.type.skyblockgeneric.item.handlers.pet.PetAbility;
import net.swofty.type.skyblockgeneric.item.handlers.pet.PetHandler;
import net.swofty.type.skyblockgeneric.item.handlers.pet.PetRegistry;
import net.swofty.type.skyblockgeneric.skill.SkillCategories;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;
import net.swofty.type.skyblockgeneric.utility.RarityValue;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Getter
public class PetComponent extends SkyBlockItemComponent {
    private final String petName;
    private final RarityValue<Integer> georgePrice;
    private final RarityValue<KatUpgrade> katUpgrades;
    private final ItemStatistics baseStatistics;
    private final Map<Rarity, ItemStatistics> perLevelStatistics;
    private final Particle particleId;
    private final SkillCategories skillCategory;
    private final String skullTexture;
    private final String handlerId;

    public PetComponent(String petName, RarityValue<Integer> georgePrice,
                        @Nullable RarityValue<KatUpgrade> katUpgrades,
                        ItemStatistics baseStatistics, Map<Rarity, ItemStatistics> perLevelStatistics,
                        Particle particleId, String skillCategory, String skullTexture,
                        String handlerId) {
        this.petName = petName;
        this.georgePrice = georgePrice;
        this.katUpgrades = katUpgrades;
        this.baseStatistics = baseStatistics;
        this.perLevelStatistics = perLevelStatistics;
        this.particleId = particleId;
        this.skillCategory = SkillCategories.valueOf(skillCategory);
        this.skullTexture = skullTexture;
        this.handlerId = handlerId;

        addInheritedComponent(new SkullHeadComponent((item) -> skullTexture));
        addInheritedComponent(new TrackedUniqueComponent());
        addInheritedComponent(new InteractableComponent(this::interact, this::interact, null));
        addInheritedComponent(new LoreUpdateComponent(
                new LoreConfig((item, player) -> getAbsoluteLore(player, item), null), true)
        );
    }

    private void interact(SkyBlockPlayer player, SkyBlockItem item) {
        DatapointPetData.UserPetData petData = player.getPetData();
        ItemType type = item.getAttributeHandler().getPotentialType();
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

    public List<String> getAbsoluteLore(@Nullable SkyBlockPlayer player, SkyBlockItem item) {
        List<String> lore = new ArrayList<>();
        ItemAttributePetData.PetData petData = item.getAttributeHandler().getPetData();
        Rarity rarity = item.getAttributeHandler().getRarity();
        int level = petData.getAsLevel(rarity);

        PetHandler handler = PetRegistry.getHandler(handlerId);
        List<PetAbility> abilities = handler.getAbilities(item);

        lore.add("§8" + skillCategory.asCategory().getName() + " Pet");
        lore.add(" ");

        addPropertyInt("Magic Find", (baseStatistics.getOverall(ItemStatistic.MAGIC_FIND) +
                getPerLevelStatistics(rarity).getOverall(ItemStatistic.MAGIC_FIND) * 100.0 * level), lore);
        addPropertyPercent("Crit Damage", (baseStatistics.getOverall(ItemStatistic.CRITICAL_DAMAGE) +
                getPerLevelStatistics(rarity).getOverall(ItemStatistic.CRITICAL_DAMAGE) * level), lore);
        addPropertyPercent("Crit Chance", (baseStatistics.getOverall(ItemStatistic.CRITICAL_CHANCE) +
                getPerLevelStatistics(rarity).getOverall(ItemStatistic.CRITICAL_CHANCE) * level), lore);
        addPropertyPercent("Health", (baseStatistics.getOverall(ItemStatistic.HEALTH) +
                getPerLevelStatistics(rarity).getOverall(ItemStatistic.HEALTH) * level), lore);
        addPropertyInt("Strength", baseStatistics.getOverall(ItemStatistic.STRENGTH) +
                getPerLevelStatistics(rarity).getOverall(ItemStatistic.STRENGTH) * level, lore);
        addPropertyInt("Defense", baseStatistics.getOverall(ItemStatistic.DEFENSE) +
                getPerLevelStatistics(rarity).getOverall(ItemStatistic.DEFENSE) * level, lore);
        addPropertyInt("Speed", baseStatistics.getOverall(ItemStatistic.SPEED) +
                getPerLevelStatistics(rarity).getOverall(ItemStatistic.SPEED) * level, lore);
        addPropertyInt("Intelligence", baseStatistics.getOverall(ItemStatistic.INTELLIGENCE) +
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

    public ItemStatistics getPerLevelStatistics(Rarity rarity) {
        return perLevelStatistics.get(rarity);
    }

    private static void addPropertyInt(String name, double value, List<String> lore) {
        if (value != 0.0) {
            lore.add("§7" + name + ": §a" + (value >= 0 ? "+" : "") + value);
        }
    }

    private static void addPropertyPercent(String name, double value, List<String> lore) {
        if (value != 0.0) {
            lore.add("§7" + name + ": §a" + (value >= 0 ? "+" : "") + value + "%");
        }
    }
}