package net.swofty.data.datapoints;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.swofty.collection.CollectionCategories;
import net.swofty.collection.CollectionCategory;
import net.swofty.data.Datapoint;
import net.swofty.item.ItemType;
import net.swofty.serializer.Serializer;
import net.swofty.user.SkyBlockPlayer;

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
