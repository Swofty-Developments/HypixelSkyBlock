package net.swofty.type.skyblockgeneric.item.handlers.pet;

import net.swofty.commons.skyblock.item.Rarity;
import net.swofty.commons.skyblock.item.attribute.attributes.ItemAttributePetData;
import net.swofty.type.skyblockgeneric.item.SkyBlockItem;
import net.swofty.type.skyblockgeneric.utility.RarityValue;

import java.util.*;

public class PetRegistry {
    private static final Map<String, PetHandler> HANDLERS = new HashMap<>();

    static {
        PetRegistry.register("BEE", PetHandler.builder()
                .abilitiesProvider(item -> {
                    ItemAttributePetData.PetData petData = item.getAttributeHandler().getPetData();
                    Rarity rarity = item.getAttributeHandler().getRarity();
                    int level = petData.getAsLevel(rarity);

                    RarityValue<Integer> hiveIntelligence = new RarityValue<>(3, 6, 6, 10, 10, 0);
                    RarityValue<Integer> hiveStrength = new RarityValue<>(3, 5, 5, 8, 8, 0);
                    RarityValue<Integer> hiveDefense = new RarityValue<>(2, 3, 3, 5, 5, 0);

                    List<PetAbility> abilities = new ArrayList<>();

                    // Add Hive ability
                    abilities.add(new PetAbility() {
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
                    });

                    // Add rare+ ability
                    if (rarity.isAtLeast(Rarity.RARE)) {
                        abilities.add(new PetAbility() {
                            @Override
                            public String getName() {
                                return "Busy Buzz Buzz";
                            }

                            @Override
                            public List<String> getDescription(SkyBlockItem instance) {
                                double bonus = rarity.isAtLeast(Rarity.EPIC) ? level * 0.3 : level * 0.2;
                                return Arrays.asList(
                                        "§7Grants §a+" + bonus + " §7of each to your pet:",
                                        "§6☘ Farming Fortune",
                                        "§6☘ Foraging Fortune",
                                        "§6☘ Mining Fortune"
                                );
                            }
                        });
                    }

                    // Add legendary ability
                    if (rarity.isAtLeast(Rarity.LEGENDARY)) {
                        abilities.add(new PetAbility() {
                            @Override
                            public String getName() {
                                return "Weaponized Honey";
                            }

                            @Override
                            public List<String> getDescription(SkyBlockItem instance) {
                                return Arrays.asList(
                                        "§7Gain §a" + level * 0.2 + "% §7of received damage as §6❤",
                                        "§6Absorption"
                                );
                            }
                        });
                    }

                    return abilities;
                })
                .build());
    }

    public static void register(String id, PetHandler handler) {
        HANDLERS.put(id, handler);
    }

    public static PetHandler getHandler(String id) {
        return HANDLERS.get(id);
    }
}