package net.swofty.type.generic.data.datapoints;

import lombok.Getter;
import net.swofty.commons.protocol.Serializer;
import net.swofty.type.generic.collectibles.CollectibleCategory;
import net.swofty.type.generic.data.Datapoint;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class DatapointCollectibles extends Datapoint<DatapointCollectibles.CollectiblesState> {


    public static final Serializer<CollectiblesState> SERIALIZER = new Serializer<>() {
        @Override
        public String serialize(CollectiblesState value) {
            JSONObject root = new JSONObject();
            JSONObject unlockedByCategory = new JSONObject();
            for (Map.Entry<String, Set<String>> entry : value.getUnlockedByCategory().entrySet()) {
                unlockedByCategory.put(entry.getKey(), new JSONArray(entry.getValue()));
            }
            root.put("unlockedByCategory", unlockedByCategory);

            JSONObject selectedByCategory = new JSONObject();
            for (Map.Entry<String, String> entry : value.getSelectedByCategory().entrySet()) {
                selectedByCategory.put(entry.getKey(), entry.getValue());
            }
            root.put("selectedByCategory", selectedByCategory);

            root.put("favoriteCollectibles", new JSONArray(value.getFavoriteCollectibles()));
            return root.toString();
        }

        @Override
        public CollectiblesState deserialize(String json) {
            if (json == null || json.isBlank() || !json.startsWith("{")) {
                return CollectiblesState.bedWarsDefaults();
            }

            try {
                JSONObject root = new JSONObject(json);

                Map<String, Set<String>> unlockedByCategory = new HashMap<>();
                JSONObject unlockedObject = root.optJSONObject("unlockedByCategory");
                if (unlockedObject != null) {
                    for (String key : unlockedObject.keySet()) {
                        JSONArray unlockedArray = unlockedObject.optJSONArray(key);
                        Set<String> unlocked = new HashSet<>();
                        if (unlockedArray != null) {
                            for (int i = 0; i < unlockedArray.length(); i++) {
                                unlocked.add(unlockedArray.getString(i));
                            }
                        }
                        unlockedByCategory.put(key, unlocked);
                    }
                }

                Map<String, String> selectedByCategory = new HashMap<>();
                JSONObject selectedObject = root.optJSONObject("selectedByCategory");
                if (selectedObject != null) {
                    for (String key : selectedObject.keySet()) {
                        selectedByCategory.put(key, selectedObject.optString(key, ""));
                    }
                }

                Set<String> favorites = new HashSet<>();
                JSONArray favoritesArray = root.optJSONArray("favoriteCollectibles");
                if (favoritesArray != null) {
                    for (int i = 0; i < favoritesArray.length(); i++) {
                        favorites.add(favoritesArray.getString(i));
                    }
                }

                CollectiblesState state = new CollectiblesState(unlockedByCategory, selectedByCategory, favorites);
                state.ensureCategoryKeys(CollectibleCategory.bedWarsCategories());
                return state;
            } catch (Exception exception) {
                return CollectiblesState.bedWarsDefaults();
            }
        }

        @Override
        public CollectiblesState clone(CollectiblesState value) {
            return value.copy();
        }
    };

    public DatapointCollectibles(String key, CollectiblesState value) {
        super(key, value, SERIALIZER);
    }

    public DatapointCollectibles(String key) {
        this(key, CollectiblesState.bedWarsDefaults());
    }

    @Getter
    public static class CollectiblesState {
        private final Map<String, Set<String>> unlockedByCategory;
        private final Map<String, String> selectedByCategory;
        private final Set<String> favoriteCollectibles;

        public CollectiblesState(Map<String, Set<String>> unlockedByCategory,
                                 Map<String, String> selectedByCategory,
                                 Set<String> favoriteCollectibles) {
            this.unlockedByCategory = new HashMap<>();
            for (Map.Entry<String, Set<String>> entry : unlockedByCategory.entrySet()) {
                this.unlockedByCategory.put(entry.getKey(), new HashSet<>(entry.getValue()));
            }
            this.selectedByCategory = new HashMap<>(selectedByCategory);
            this.favoriteCollectibles = new HashSet<>(favoriteCollectibles);
        }

        public static CollectiblesState bedWarsDefaults() {
            Map<String, Set<String>> unlockedByCategory = new HashMap<>();
            for (CollectibleCategory category : CollectibleCategory.bedWarsCategories()) {
                unlockedByCategory.put(category.name(), new HashSet<>());
            }

            Map<String, String> selectedByCategory = new HashMap<>();
            selectedByCategory.put(CollectibleCategory.WOOD_SKINS.name(), "oak_plank");
            selectedByCategory.put(CollectibleCategory.SHOPKEEPER_SKINS.name(), "blacksmith");

            return new CollectiblesState(
                unlockedByCategory,
                selectedByCategory,
                new HashSet<>()
            );
        }

        public void ensureCategoryKeys(Collection<CollectibleCategory> categories) {
            for (CollectibleCategory category : categories) {
                unlockedByCategory.computeIfAbsent(category.name(), ignored -> new HashSet<>());
            }
        }

        public void ensureCategoryKey(CollectibleCategory category) {
            unlockedByCategory.computeIfAbsent(category.name(), ignored -> new HashSet<>());
        }

        public boolean isManuallyUnlocked(CollectibleCategory category, String collectibleId) {
            Set<String> unlocked = unlockedByCategory.get(category.name());
            return unlocked != null && unlocked.contains(collectibleId);
        }

        public void unlock(CollectibleCategory category, String collectibleId) {
            unlockedByCategory.computeIfAbsent(category.name(), ignored -> new HashSet<>()).add(collectibleId);
        }

        public String getSelected(CollectibleCategory category) {
            return selectedByCategory.get(category.name());
        }

        public void setSelected(CollectibleCategory category, String collectibleId) {
            if (collectibleId == null || collectibleId.isBlank()) {
                selectedByCategory.remove(category.name());
                return;
            }
            selectedByCategory.put(category.name(), collectibleId);
        }

        public boolean isFavorite(String collectibleId) {
            return favoriteCollectibles.contains(collectibleId);
        }

        public void setFavorite(String collectibleId, boolean favorite) {
            if (favorite) {
                favoriteCollectibles.add(collectibleId);
            } else {
                favoriteCollectibles.remove(collectibleId);
            }
        }

        public boolean toggleFavorite(String collectibleId) {
            if (favoriteCollectibles.contains(collectibleId)) {
                favoriteCollectibles.remove(collectibleId);
                return false;
            }
            favoriteCollectibles.add(collectibleId);
            return true;
        }

        public CollectiblesState copy() {
            return new CollectiblesState(unlockedByCategory, selectedByCategory, favoriteCollectibles);
        }
    }
}
