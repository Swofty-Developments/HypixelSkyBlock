package net.swofty.types.generic.item.items.pet;

import net.swofty.types.generic.item.Rarity;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.attribute.attributes.ItemAttributePetData;
import net.swofty.types.generic.item.impl.NotFinishedYet;
import net.swofty.types.generic.item.impl.Pet;
import net.swofty.types.generic.skill.SkillCategories;
import net.swofty.types.generic.skill.SkillCategory;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.user.statistics.ItemStatistic;
import net.swofty.types.generic.user.statistics.ItemStatistics;
import net.swofty.types.generic.utility.RarityValue;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class BeePet implements Pet, NotFinishedYet {
    @Override
    public String getSkullTexture(@Nullable SkyBlockPlayer player, SkyBlockItem item) {
        return "12724a9a4cdd68ba49415560e5be40b4a1c47cb5be1d66aedb52a30e62ef2d47";
    }

    @Override
    public List<PetAbility> getPetAbilities(SkyBlockItem instance) {
        ItemAttributePetData.PetData petData = instance.getAttributeHandler().getPetData();
        Rarity rarity = instance.getAttributeHandler().getRarity();
        int level = petData.getAsLevel(rarity);

        RarityValue<Integer> hiveIntelligence = new RarityValue<>(
                3, 5, 10, 15, 20, 5
        );
        RarityValue<Integer> hiveStrength = new RarityValue<>(
                3, 5, 8, 12, 15, 5
        );

        List<PetAbility> abilities = new ArrayList<>(Collections.singletonList(new PetAbility() {
            @Override
            public String getName() {
                return "Hive";
            }

            @Override
            public List<String> getDescription(SkyBlockItem instance) {
                return Arrays.asList("§7Gain §b+" + hiveIntelligence.getForRarity(rarity) + " Intelligence §7and",
                        "§c+" + hiveStrength.getForRarity(rarity) + " Strength §7for each nearby",
                        "§7bee.", "§8Max 15 bees");
            }
        }));

        if (rarity == Rarity.RARE) {
            abilities.add(new PetAbility() {
                @Override
                public String getName() {
                    return "Busy Buzz Buzz";
                }

                @Override
                public List<String> getDescription(SkyBlockItem instance) {
                    return Arrays.asList("§7Has §a" + (level * 0.5) + "% §7chance for", "§7flowers to drop an extra one.");
                }
            });
        } else if (rarity.isAtLeast(Rarity.EPIC)) {
            abilities.add(new PetAbility() {
                @Override
                public String getName() {
                    return "Busy Buzz Buzz";
                }

                @Override
                public List<String> getDescription(SkyBlockItem instance) {
                    return Arrays.asList("§7Has §a" + (level) + "% §7chance for",
                            "§7flowers to drop an extra one.");
                }
            });
        }
        if (rarity.isAtLeast(Rarity.LEGENDARY)) {
            abilities.add(new PetAbility() {
                @Override
                public String getName() {
                    return "Honeycomb";
                }

                @Override
                public List<String> getDescription(SkyBlockItem instance) {
                    return Arrays.asList("§7Gain §b+" + (level * 0.5) + " Intelligence §7and",
                            "§c+" + (level * 0.5) + " Strength §7for each", "§7bee in your hive.");
                }
            });
        }

        return abilities;
    }

    @Override
    public String getPetName() {
        return "Bee";
    }

    @Override
    public ItemStatistics getPerLevelStatistics(Rarity rarity) {
        return ItemStatistics.builder()
                .withAdditive(ItemStatistic.INTELLIGENCE, 0.5)
                .withAdditive(ItemStatistic.SPEED, 0.1)
                .withAdditive(ItemStatistic.STRENGTH, 0.333)
                .build();
    }

    @Override
    public int particleId() {
        return 38;
    }

    @Override
    public SkillCategories getSkillCategory() {
        return SkillCategories.FARMING;
    }
}
