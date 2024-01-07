package net.swofty.commons.skyblock.data.datapoints;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.swofty.commons.skyblock.collection.CollectionCategory;
import net.swofty.commons.skyblock.serializer.Serializer;
import net.swofty.commons.skyblock.collection.CollectionCategories;
import net.swofty.commons.skyblock.data.Datapoint;
import net.swofty.commons.skyblock.item.ItemType;
import net.swofty.commons.skyblock.utility.StringUtility;

import java.util.*;

public class DatapointCollection extends Datapoint<DatapointCollection.PlayerCollection> {

    public DatapointCollection(String key, DatapointCollection.PlayerCollection value) {
        super(key, value, new Serializer<>() {
            @Override
            public String serialize(DatapointCollection.PlayerCollection value) {
                StringBuilder sb = new StringBuilder();
                sb.append("{");
                for (Map.Entry<ItemType, Integer> entry : value.getItems().entrySet()) {
                    sb.append("\"").append(entry.getKey().toString()).append("\":").append(entry.getValue()).append(",");
                }
                if (sb.charAt(sb.length() - 1) == ',') {
                    sb.deleteCharAt(sb.length() - 1);
                }
                sb.append("}");
                return sb.toString();
            }

            @Override
            public DatapointCollection.PlayerCollection deserialize(String json) {
                Map<ItemType, Integer> items = new HashMap<>();
                String[] pairs = json.substring(1, json.length() - 1).split(",");
                for (String pair : pairs) {
                    String[] keyValue = pair.split(":");
                    items.put(ItemType.valueOf(keyValue[0]), Integer.valueOf(keyValue[1]));
                }
                return new PlayerCollection(items);
            }
        });
    }

    public DatapointCollection(String key) {
        this(key, new PlayerCollection(new HashMap<>()));
    }

    @Getter
    @AllArgsConstructor
    public static class PlayerCollection {
        private Map<ItemType, Integer> items;

        public CollectionCategory.ItemCollectionReward getReward(CollectionCategory.ItemCollection collection) {
            // Reverse the array so that we can get the highest reward that the player has unlocked
            List<CollectionCategory.ItemCollectionReward> rewards = Arrays.asList(collection.rewards());
            Collections.reverse(rewards);

            for (CollectionCategory.ItemCollectionReward reward : rewards) {
                if (get(collection.type()) <= reward.requirement()) {
                    return reward;
                }
            }

            return null;
        }

        public void increase(ItemType type) {
            items.put(type, get(type) + 1);
        }

        public Integer get(ItemType type) {
            return items.getOrDefault(type, 0);
        }

        public boolean unlocked(ItemType type) {
            return get(type) > 0;
        }

        public List<String> getDisplay(List<String> lore) {
            int allCollections = CollectionCategories.getCategories().stream().mapToInt(category -> category.getCollections().length).sum();
            int unlockedCollections = (int) items.keySet().stream().filter(this::unlocked).count();

            String unlockedPercentage = String.format("%.2f", (unlockedCollections / (double) allCollections) * 100);
            lore.add("§7Collections Unlocked: §e" + unlockedPercentage + "§6%");

            String baseLoadingBar = "─────────────────";
            int maxBarLength = baseLoadingBar.length();
            int completedLength = (int) ((unlockedCollections / (double) allCollections) * maxBarLength);

            String completedLoadingBar = "§2§m" + baseLoadingBar.substring(0, Math.min(completedLength, maxBarLength));
            int formattingCodeLength = 4;  // Adjust this if you add or remove formatting codes
            String uncompletedLoadingBar = "§7§m" + baseLoadingBar.substring(Math.min(
                    completedLoadingBar.length() - formattingCodeLength,  // Adjust for added formatting codes
                    maxBarLength
            ));

            lore.add(completedLoadingBar + uncompletedLoadingBar + "§r §e" + unlockedCollections + "§6/§e" + allCollections);

            return lore;
        }

        public List<String> getDisplay(List<String> lore, CollectionCategory.ItemCollection collection) {
            CollectionCategory.ItemCollectionReward reward = getReward(collection);
            int collected = get(collection.type());
            int required = reward == null ? 0 : reward.requirement();

            String collectedPercentage = String.format("%.2f", (collected / (double) required) * 100);
            lore.add("§7Progress to Wheat " + StringUtility.getAsRomanNumeral(collection.getPlacementOf(reward) + 1) +
                    ": §e" + collectedPercentage + "§6%");

            String baseLoadingBar = "─────────────────";
            int maxBarLength = baseLoadingBar.length();
            int completedLength = (int) ((collected / (double) required) * maxBarLength);

            String completedLoadingBar = "§2§m" + baseLoadingBar.substring(0, Math.min(completedLength, maxBarLength));
            int formattingCodeLength = 4;  // Adjust this if you add or remove formatting codes
            String uncompletedLoadingBar = "§7§m" + baseLoadingBar.substring(Math.min(
                    completedLoadingBar.length() - formattingCodeLength,  // Adjust for added formatting codes
                    maxBarLength
            ));

            lore.add(completedLoadingBar + uncompletedLoadingBar + "§r §e" + collected + "§6/§e" + required);

            return lore;
        }

        public List<String> getDisplay(List<String> lore, CollectionCategory category) {
            int allCollections = category.getCollections().length;
            int unlockedCollections = (int) items.keySet().stream().filter(this::unlocked).count();

            String unlockedPercentage = String.format("%.2f", (unlockedCollections / (double) allCollections) * 100);
            lore.add("§7Collections Unlocked: §e" + unlockedPercentage + "§6%");

            String baseLoadingBar = "─────────────────";
            int maxBarLength = baseLoadingBar.length();
            int completedLength = (int) ((unlockedCollections / (double) allCollections) * maxBarLength);

            String completedLoadingBar = "§2§m" + baseLoadingBar.substring(0, Math.min(completedLength, maxBarLength));
            int formattingCodeLength = 4;  // Adjust this if you add or remove formatting codes
            String uncompletedLoadingBar = "§7§m" + baseLoadingBar.substring(Math.min(
                    completedLoadingBar.length() - formattingCodeLength,  // Adjust for added formatting codes
                    maxBarLength
            ));

            lore.add(completedLoadingBar + uncompletedLoadingBar + "§r §e" + unlockedCollections + "§6/§e" + allCollections);

            return lore;
        }
    }
}
