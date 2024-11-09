package net.swofty.types.generic.levels;

import lombok.SneakyThrows;
import net.swofty.types.generic.collection.CollectionCategories;
import net.swofty.types.generic.collection.CollectionCategory;
import net.swofty.types.generic.item.impl.Accessory;
import net.swofty.types.generic.levels.abstr.SkyBlockLevelCauseAbstr;
import net.swofty.types.generic.levels.causes.*;
import net.swofty.types.generic.mission.MissionData;
import net.swofty.types.generic.museum.MuseumRewards;
import net.swofty.types.generic.skill.SkillCategories;
import net.swofty.types.generic.user.fairysouls.FairySoulExchangeLevels;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SkyBlockLevelCause {
    private static final Map<String, SkyBlockLevelCauseAbstr> CAUSES = new HashMap<>();

    @SneakyThrows
    public static void initializeCauses() {
        // Register all Skill causes
        for (SkillCategories category : SkillCategories.values()) {
            for (int i = 1; i <= category.asCategory().getHighestLevel(); i++) {
                CAUSES.put("skill-" + category.name() + "-" + i, new SkillLevelCause(category, i));
            }
        }

        // Register all accessory causes
        for (ItemTypeLinker itemTypeLinker : ItemTypeLinker.values()) {
            if (itemTypeLinker.clazz == null) continue;
            if (itemTypeLinker.clazz.newInstance() instanceof Accessory) {
                CAUSES.put("accessory-" + itemTypeLinker.name(), new NewAccessoryLevelCause(itemTypeLinker));
            }
        }

        // Register all level causes up to 500
        for (int i = 0; i <= 500; i++) {
            CAUSES.put("level-" + i, new LevelCause(i));
        }

        // Register all collection causes
        for (ItemTypeLinker itemTypeLinker : ItemTypeLinker.values()) {
            CollectionCategory category = CollectionCategories.getCategory(itemTypeLinker);
            if (category == null) continue;

            for (CollectionCategory.ItemCollection collection : category.getCollections()) {
                for (CollectionCategory.ItemCollectionReward reward : collection.rewards()) {
                    CAUSES.put("collection-" + itemTypeLinker.name() + "-" + collection.getPlacementOf(reward),
                            new CollectionLevelCause(itemTypeLinker, collection.getPlacementOf(reward)));
                }
            }
        }

        // Register all Fairy Exchange rewards
        for (int i = 0; i <= FairySoulExchangeLevels.values().length; i++) {
            CAUSES.put("fairy-soul-exchange-" + i, new FairySoulExchangeLevelCause(i));
        }

        // Register all Mission rewards
        for (String missionID : MissionData.getAllMissionIDs()) {
            CAUSES.put("mission-" + missionID, new MissionLevelCause(missionID));
        }

        // Register all Museum rewards
        for (MuseumRewards reward : MuseumRewards.values()) {
            CAUSES.put("museum-" + reward.name(), new MuseumLevelCause(reward));
        }
    }

    public static Map<String, SkyBlockLevelCauseAbstr> getCauses() {
        return CAUSES;
    }

    public static Double getTotalXP() {
        double total = 0;
        for (SkyBlockLevelCauseAbstr cause : CAUSES.values()) {
            total += cause.xpReward();
        }
        return total;
    }

    public static FairySoulExchangeLevelCause getFairySoulExchangeCause(int exchangeCount) {
        for (SkyBlockLevelCauseAbstr cause : CAUSES.values()) {
            if (cause instanceof FairySoulExchangeLevelCause exchangeCause) {
                if (exchangeCause.getExchangeCount() == exchangeCount) {
                    return exchangeCause;
                }
            }
        }
        return null;
    }

    public static MissionLevelCause getMissionCause(String missionKey) {
        for (SkyBlockLevelCauseAbstr cause : CAUSES.values()) {
            if (cause instanceof MissionLevelCause missionCause) {
                if (missionCause.getMissionKey().equals(missionKey)) {
                    return missionCause;
                }
            }
        }
        return null;
    }

    public static MuseumLevelCause getMuseumCause(MuseumRewards reward) {
        for (SkyBlockLevelCauseAbstr cause : CAUSES.values()) {
            if (cause instanceof MuseumLevelCause museumCause) {
                if (museumCause.getReward() == reward) {
                    return museumCause;
                }
            }
        }
        return null;
    }

    public static CollectionLevelCause getCollectionCause(ItemTypeLinker itemTypeLinker, int level) {
        for (SkyBlockLevelCauseAbstr cause : CAUSES.values()) {
            if (cause instanceof CollectionLevelCause collectionCause) {
                if (collectionCause.getType() == itemTypeLinker && collectionCause.getLevel() == level) {
                    return collectionCause;
                }
            }
        }
        return null;
    }

    public static int getAmountOfCauses() {
        return CAUSES.size();
    }

    public static LevelCause getLevelCause(int level) {
        for (SkyBlockLevelCauseAbstr cause : CAUSES.values()) {
            if (cause instanceof LevelCause levelCause) {
                if (levelCause.getLevel() == level) {
                    return levelCause;
                }
            }
        }
        return null;
    }

    public static NewAccessoryLevelCause getAccessoryCause(ItemTypeLinker itemTypeLinker) {
        for (SkyBlockLevelCauseAbstr cause : CAUSES.values()) {
            if (cause instanceof NewAccessoryLevelCause accessoryCause) {
                if (accessoryCause.itemTypeLinker == itemTypeLinker) {
                    return accessoryCause;
                }
            }
        }
        return null;
    }

    public static SkillLevelCause getSkillCause(SkillCategories category, int level) {
        for (SkyBlockLevelCauseAbstr cause : CAUSES.values()) {
            if (cause instanceof SkillLevelCause skillCause) {
                if (skillCause.getCategory() == category && skillCause.getLevel() == level) {
                    return skillCause;
                }
            }
        }
        return null;
    }

    public static @Nullable SkyBlockLevelCauseAbstr getCause(String key) {
        return CAUSES.get(key);
    }

    public static String getKey(SkyBlockLevelCauseAbstr cause) {
        for (Map.Entry<String, SkyBlockLevelCauseAbstr> entry : CAUSES.entrySet()) {
            if (entry.getValue() == cause) {
                return entry.getKey();
            }
        }
        throw new IllegalArgumentException("Cause not found " + cause.getClass().getSimpleName());
    }

    public static List<SkyBlockLevelCauseAbstr> getSkillCauses(SkillCategories category, int levelUpToInclusive) {
        List<SkyBlockLevelCauseAbstr> causes = new ArrayList<>();
        for (SkyBlockLevelCauseAbstr cause : CAUSES.values()) {
            if (cause instanceof SkillLevelCause skillCause) {
                if (skillCause.getCategory() == category && skillCause.getLevel() <= levelUpToInclusive) {
                    causes.add(skillCause);
                }
            }
        }
        return causes;
    }
}
