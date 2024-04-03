package net.swofty.types.generic.levels;

import lombok.Getter;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.types.generic.gui.inventory.ItemStackCreator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
public enum CustomLevelAward {
    ACCESS_TO_COMMUNITY_SHOP(ItemStackCreator.getStackHead("4e495b103ddd47701449ad7a34d908e8d2f08e0bd9476653d433c3bfc7c1b055"),
            "§bAccess to Community Shop"),
    ACCESS_TO_GARDEN(ItemStackCreator.getStackHead("4778b434a258f7991825cabc965a56403c4d772e9628ce60164927e94b79d17"),
            "§2Access to the Garden"),
    ACCESS_TO_WARDROBE(ItemStack.builder(Material.LEATHER_CHESTPLATE),
            "§aAccess to Wardrobe"),
    ;
    private static final Map<Integer, List<CustomLevelAward>> CACHE = new HashMap<>();

    private final ItemStack.Builder item;
    private final String display;

    CustomLevelAward(ItemStack.Builder item, String display) {
        this.item = item;
        this.display = display;
    }

    public static int getTotalLevelAwards() {
        return CACHE.values().stream().mapToInt(List::size).sum();
    }

    public static Map<CustomLevelAward, Integer> getAwards() {
        Map<CustomLevelAward, Integer> awards = new HashMap<>();
        for (Map.Entry<Integer, List<CustomLevelAward>> entry : CACHE.entrySet()) {
            for (CustomLevelAward award : entry.getValue()) {
                awards.put(award, entry.getKey());
            }
        }
        return awards;
    }

    public static Integer getLevel(CustomLevelAward award) {
        for (Map.Entry<Integer, List<CustomLevelAward>> entry : CACHE.entrySet()) {
            if (entry.getValue().contains(award)) {
                return entry.getKey();
            }
        }
        return null;
    }

    public static Map.Entry<Integer, List<CustomLevelAward>> getNextReward(int level) {
        int nextLevel = level + 1;
        int amountToSearch = 500;
        for (int i = nextLevel; i <= amountToSearch; i++) {
            if (CACHE.containsKey(i)) {
                return Map.entry(i, CACHE.get(i));
            }
        }
        return null;
    }

    public static void addToCache(int level, CustomLevelAward award) {
        if (!CACHE.containsKey(level)) {
            CACHE.put(level, new ArrayList<>(List.of(award)));
        } else {
            CACHE.get(level).add(award);
        }
    }

    public static List<CustomLevelAward> getFromLevel(int level) {
        // Include current level and any previous levels, so we can get all awards up to the current level
        List<CustomLevelAward> awards = new ArrayList<>();
        for (int i = 1; i <= level; i++) {
            if (CACHE.containsKey(i)) {
                awards.addAll(CACHE.get(i));
            }
        }
        return awards;
    }
}
