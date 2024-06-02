package net.swofty.types.generic.item.items.pet;

import net.swofty.types.generic.item.Rarity;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.attribute.attributes.ItemAttributePetData;
import net.swofty.types.generic.item.impl.NotFinishedYet;
import net.swofty.types.generic.item.impl.Pet;
import net.swofty.types.generic.skill.SkillCategories;
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
                3, 6, 6, 10, 10, 0
        );
        RarityValue<Integer> hiveStrength = new RarityValue<>(
                3, 5, 5, 8, 8, 0
        );
        RarityValue<Integer> hiveDefense = new RarityValue<>(
                2, 3, 3, 5, 5, 0
        );

        List<PetAbility> abilities = new ArrayList<>(Collections.singletonList(new PetAbility() {
            @Override
            public String getName() {
                return "Hive";
            }

            @Override
            public List<String> getDescription(SkyBlockItem instance) {
                return Arrays.asList(
                        "§7For each player within §a25 §7 blocks:",
                        " §7Gain §b+" + hiveIntelligence.getForRarity(rarity) + "✎ Intelligence",
                        " §7Gain §c+" + hiveStrength.getForRarity(rarity) + "❁ Strength",
                        " §7Gain §a+" + hiveDefense.getForRarity(rarity) + "❈ Defense",
                        "§8Max 15 players"
                );
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
                    return Arrays.asList("§7Grants §a+20 §7of each to your pet:",
                            "§6☘ Farming Fortune",
                            "§6☘ Foraging Fortune",
                            "§6☘ Mining Fortune"
                    );
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
                    return Arrays.asList("§7Grants §a+30 §7of each to your pet:",
                            "§6☘ Farming Fortune",
                            "§6☘ Foraging Fortune",
                            "§6☘ Mining Fortune"
                    );
                }
            });
        }
        if (rarity.isAtLeast(Rarity.LEGENDARY)) {
            abilities.add(new PetAbility() {
                @Override
                public String getName() {
                    return "Weaponized Honey";
                }

                @Override
                public List<String> getDescription(SkyBlockItem instance) {
                    return Arrays.asList(
                            "§7Gain §a25% §7of received damage as §6❤",
                            "§6Absorption"
                    );
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
    public ItemStatistics getBaseStatistics() {
        return ItemStatistics.builder()
                .withBase(ItemStatistic.STRENGTH, 5D)
                .build();
    }

    @Override
    public ItemStatistics getPerLevelStatistics(Rarity rarity) {
        return ItemStatistics.builder()
                .withBase(ItemStatistic.SPEED, 0.1)
                .withBase(ItemStatistic.STRENGTH, 0.25)
                .withBase(ItemStatistic.INTELLIGENCE, 0.5)
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
