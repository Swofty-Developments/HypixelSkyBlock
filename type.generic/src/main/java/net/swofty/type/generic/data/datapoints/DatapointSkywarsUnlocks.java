package net.swofty.type.generic.data.datapoints;

import net.swofty.commons.protocol.Serializer;
import net.swofty.type.generic.data.Datapoint;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.*;

public class DatapointSkywarsUnlocks extends Datapoint<DatapointSkywarsUnlocks.SkywarsUnlocks> {

    public DatapointSkywarsUnlocks(String key, SkywarsUnlocks value) {
        super(key, value, new Serializer<>() {
            @Override
            public String serialize(SkywarsUnlocks value) {
                JSONObject json = new JSONObject();
                json.put("unlockedKits", new JSONArray(value.getUnlockedKits()));
                json.put("unlockedPerks", new JSONArray(value.getUnlockedPerks()));
                json.put("selectedKit", value.getSelectedKit());
                json.put("selectedPerks", new JSONArray(value.getSelectedPerks()));

                // New fields for per-mode selections
                JSONObject selectedKitByMode = new JSONObject();
                for (Map.Entry<String, String> entry : value.getSelectedKitByMode().entrySet()) {
                    selectedKitByMode.put(entry.getKey(), entry.getValue());
                }
                json.put("selectedKitByMode", selectedKitByMode);

                JSONObject selectedPerksByMode = new JSONObject();
                for (Map.Entry<String, List<String>> entry : value.getSelectedPerksByMode().entrySet()) {
                    selectedPerksByMode.put(entry.getKey(), new JSONArray(entry.getValue()));
                }
                json.put("selectedPerksByMode", selectedPerksByMode);

                JSONObject disabledInsanePerks = new JSONObject();
                for (Map.Entry<String, Set<String>> entry : value.getDisabledInsanePerks().entrySet()) {
                    disabledInsanePerks.put(entry.getKey(), new JSONArray(entry.getValue()));
                }
                json.put("disabledInsanePerks", disabledInsanePerks);

                json.put("favoriteKits", new JSONArray(value.getFavoriteKits()));

                JSONObject kitPrestigeXP = new JSONObject();
                for (Map.Entry<String, Integer> entry : value.getKitPrestigeXP().entrySet()) {
                    kitPrestigeXP.put(entry.getKey(), entry.getValue());
                }
                json.put("kitPrestigeXP", kitPrestigeXP);

                return json.toString();
            }

            @Override
            public SkywarsUnlocks deserialize(String json) {
                if (json == null || json.isEmpty()) {
                    return SkywarsUnlocks.empty();
                }
                JSONObject obj = new JSONObject(json);

                Set<String> unlockedKits = new HashSet<>();
                JSONArray kitsArray = obj.optJSONArray("unlockedKits");
                if (kitsArray != null) {
                    for (int i = 0; i < kitsArray.length(); i++) {
                        unlockedKits.add(kitsArray.getString(i));
                    }
                }

                Set<String> unlockedPerks = new HashSet<>();
                JSONArray perksArray = obj.optJSONArray("unlockedPerks");
                if (perksArray != null) {
                    for (int i = 0; i < perksArray.length(); i++) {
                        unlockedPerks.add(perksArray.getString(i));
                    }
                }

                String selectedKit = obj.optString("selectedKit", "default");

                List<String> selectedPerks = new ArrayList<>();
                JSONArray selectedPerksArray = obj.optJSONArray("selectedPerks");
                if (selectedPerksArray != null) {
                    for (int i = 0; i < selectedPerksArray.length(); i++) {
                        selectedPerks.add(selectedPerksArray.getString(i));
                    }
                }

                // Parse new fields
                Map<String, String> selectedKitByMode = new HashMap<>();
                JSONObject selectedKitByModeObj = obj.optJSONObject("selectedKitByMode");
                if (selectedKitByModeObj != null) {
                    for (String key : selectedKitByModeObj.keySet()) {
                        selectedKitByMode.put(key, selectedKitByModeObj.getString(key));
                    }
                }

                Map<String, List<String>> selectedPerksByMode = new HashMap<>();
                JSONObject selectedPerksByModeObj = obj.optJSONObject("selectedPerksByMode");
                if (selectedPerksByModeObj != null) {
                    for (String key : selectedPerksByModeObj.keySet()) {
                        JSONArray modePerks = selectedPerksByModeObj.getJSONArray(key);
                        List<String> perksList = new ArrayList<>();
                        for (int i = 0; i < modePerks.length(); i++) {
                            perksList.add(modePerks.getString(i));
                        }
                        selectedPerksByMode.put(key, perksList);
                    }
                }

                Map<String, Set<String>> disabledInsanePerks = new HashMap<>();
                JSONObject disabledInsanePerksObj = obj.optJSONObject("disabledInsanePerks");
                if (disabledInsanePerksObj != null) {
                    for (String key : disabledInsanePerksObj.keySet()) {
                        JSONArray disabled = disabledInsanePerksObj.getJSONArray(key);
                        Set<String> disabledSet = new HashSet<>();
                        for (int i = 0; i < disabled.length(); i++) {
                            disabledSet.add(disabled.getString(i));
                        }
                        disabledInsanePerks.put(key, disabledSet);
                    }
                }

                Set<String> favoriteKits = new HashSet<>();
                JSONArray favoriteKitsArray = obj.optJSONArray("favoriteKits");
                if (favoriteKitsArray != null) {
                    for (int i = 0; i < favoriteKitsArray.length(); i++) {
                        favoriteKits.add(favoriteKitsArray.getString(i));
                    }
                }

                Map<String, Integer> kitPrestigeXP = new HashMap<>();
                JSONObject kitPrestigeXPObj = obj.optJSONObject("kitPrestigeXP");
                if (kitPrestigeXPObj != null) {
                    for (String key : kitPrestigeXPObj.keySet()) {
                        kitPrestigeXP.put(key, kitPrestigeXPObj.getInt(key));
                    }
                }

                return new SkywarsUnlocks(unlockedKits, unlockedPerks, selectedKit, selectedPerks,
                        selectedKitByMode, selectedPerksByMode, disabledInsanePerks, favoriteKits, kitPrestigeXP);
            }

            @Override
            public SkywarsUnlocks clone(SkywarsUnlocks value) {
                return value.copy();
            }
        });
    }

    public DatapointSkywarsUnlocks(String key) {
        this(key, SkywarsUnlocks.empty());
    }

    public static class SkywarsUnlocks {
        private final Set<String> unlockedKits;
        private final Set<String> unlockedPerks;
        private String selectedKit;  // Legacy field, kept for backwards compatibility
        private List<String> selectedPerks;  // Legacy field
        private final Map<String, String> selectedKitByMode;  // Mode -> Kit ID
        private final Map<String, List<String>> selectedPerksByMode;  // Mode -> List of Perk IDs
        private final Map<String, Set<String>> disabledInsanePerks;  // Mode -> Set of disabled perk IDs
        private final Set<String> favoriteKits;
        private final Map<String, Integer> kitPrestigeXP;  // Kit ID -> XP

        public SkywarsUnlocks(Set<String> unlockedKits, Set<String> unlockedPerks,
                              String selectedKit, List<String> selectedPerks,
                              Map<String, String> selectedKitByMode, Map<String, List<String>> selectedPerksByMode,
                              Map<String, Set<String>> disabledInsanePerks, Set<String> favoriteKits,
                              Map<String, Integer> kitPrestigeXP) {
            this.unlockedKits = new HashSet<>(unlockedKits);
            this.unlockedPerks = new HashSet<>(unlockedPerks);
            this.selectedKit = selectedKit;
            this.selectedPerks = new ArrayList<>(selectedPerks);
            this.selectedKitByMode = new HashMap<>(selectedKitByMode);
            this.selectedPerksByMode = new HashMap<>(selectedPerksByMode);
            this.disabledInsanePerks = new HashMap<>(disabledInsanePerks);
            this.favoriteKits = new HashSet<>(favoriteKits);
            this.kitPrestigeXP = new HashMap<>(kitPrestigeXP);
        }

        public static SkywarsUnlocks empty() {
            Set<String> defaultKits = new HashSet<>();
            defaultKits.add("default");
            Map<String, String> defaultKitByMode = new HashMap<>();
            defaultKitByMode.put("NORMAL", "default");
            defaultKitByMode.put("INSANE", "default");
            return new SkywarsUnlocks(defaultKits, new HashSet<>(), "default", new ArrayList<>(),
                    defaultKitByMode, new HashMap<>(), new HashMap<>(), new HashSet<>(), new HashMap<>());
        }

        // Basic getters
        public Set<String> getUnlockedKits() { return unlockedKits; }
        public Set<String> getUnlockedPerks() { return unlockedPerks; }
        public String getSelectedKit() { return selectedKit; }
        public List<String> getSelectedPerks() { return selectedPerks; }
        public Map<String, String> getSelectedKitByMode() { return selectedKitByMode; }
        public Map<String, List<String>> getSelectedPerksByMode() { return selectedPerksByMode; }
        public Map<String, Set<String>> getDisabledInsanePerks() { return disabledInsanePerks; }
        public Set<String> getFavoriteKits() { return favoriteKits; }
        public Map<String, Integer> getKitPrestigeXP() { return kitPrestigeXP; }

        // Unlock methods
        public void unlockKit(String kitId) { unlockedKits.add(kitId); }
        public void unlockPerk(String perkId) { unlockedPerks.add(perkId); }

        // Ownership checks
        public boolean hasKit(String kitId) { return unlockedKits.contains(kitId); }
        public boolean hasPerk(String perkId) { return unlockedPerks.contains(perkId); }

        // Kit selection by mode
        public void selectKit(String kitId) {
            if (hasKit(kitId)) this.selectedKit = kitId;
        }

        public void selectKitForMode(String mode, String kitId) {
            if (hasKit(kitId)) {
                selectedKitByMode.put(mode, kitId);
                // Also update legacy field for backwards compatibility
                if ("NORMAL".equals(mode)) {
                    this.selectedKit = kitId;
                }
            }
        }

        public String getSelectedKitForMode(String mode) {
            return selectedKitByMode.getOrDefault(mode, "default");
        }

        // Perk selection - legacy method
        public void selectPerk(String perkId) {
            if (hasPerk(perkId) && selectedPerks.size() < 5 && !selectedPerks.contains(perkId)) {
                selectedPerks.add(perkId);
            }
        }

        // Legacy method - kept for backwards compatibility
        public void selectPerkForMode(String mode, String perkId) {
            if (!hasPerk(perkId)) return;

            List<String> modePerks = selectedPerksByMode.computeIfAbsent(mode, k -> new ArrayList<>());

            // Find first empty slot or replace last slot
            int emptySlot = -1;
            for (int i = 0; i < 6; i++) {
                if (i >= modePerks.size() || modePerks.get(i) == null || modePerks.get(i).isEmpty()) {
                    emptySlot = i;
                    break;
                }
            }

            if (emptySlot >= 0) {
                selectPerkForSlot(mode, emptySlot, perkId);
            }
        }

        /**
         * Select a perk for a specific slot (0-5 for Normal mode)
         */
        public void selectPerkForSlot(String mode, int slot, String perkId) {
            if (slot < 0 || slot > 5) return;
            if (!hasPerk(perkId)) return;

            List<String> modePerks = selectedPerksByMode.computeIfAbsent(mode, k -> new ArrayList<>());

            // Ensure list has enough elements
            while (modePerks.size() <= slot) {
                modePerks.add(null);
            }

            // Remove this perk from any other slot first
            for (int i = 0; i < modePerks.size(); i++) {
                if (perkId.equals(modePerks.get(i))) {
                    modePerks.set(i, null);
                }
            }

            modePerks.set(slot, perkId);
        }

        /**
         * Clear a specific perk slot
         */
        public void clearPerkSlot(String mode, int slot) {
            if (slot < 0 || slot > 5) return;
            List<String> modePerks = selectedPerksByMode.get(mode);
            if (modePerks != null && slot < modePerks.size()) {
                modePerks.set(slot, null);
            }
        }

        /**
         * Get perk ID at a specific slot (or null if empty)
         */
        public String getPerkAtSlot(String mode, int slot) {
            if (slot < 0 || slot > 5) return null;
            List<String> modePerks = selectedPerksByMode.get(mode);
            if (modePerks == null || slot >= modePerks.size()) {
                return null;
            }
            return modePerks.get(slot);
        }

        /**
         * Check if a perk is currently selected in any slot
         */
        public boolean isPerkSelectedForMode(String mode, String perkId) {
            List<String> modePerks = selectedPerksByMode.get(mode);
            if (modePerks == null) return false;
            return modePerks.contains(perkId);
        }

        public void deselectPerk(String perkId) {
            selectedPerks.remove(perkId);
        }

        public void deselectPerkForMode(String mode, String perkId) {
            List<String> modePerks = selectedPerksByMode.get(mode);
            if (modePerks != null) {
                for (int i = 0; i < modePerks.size(); i++) {
                    if (perkId.equals(modePerks.get(i))) {
                        modePerks.set(i, null);
                    }
                }
            }
        }

        public List<String> getSelectedPerksForMode(String mode) {
            return selectedPerksByMode.getOrDefault(mode, new ArrayList<>());
        }

        /**
         * Get count of active (non-null) perks for a mode
         */
        public int getActivePerksCount(String mode) {
            List<String> modePerks = selectedPerksByMode.get(mode);
            if (modePerks == null) return 0;
            return (int) modePerks.stream().filter(p -> p != null && !p.isEmpty()).count();
        }

        // Insane mode perk toggles
        public void toggleInsanePerk(String mode, String perkId) {
            Set<String> disabled = disabledInsanePerks.computeIfAbsent(mode, k -> new HashSet<>());
            if (disabled.contains(perkId)) {
                disabled.remove(perkId);
            } else {
                disabled.add(perkId);
            }
        }

        public boolean isInsanePerkEnabled(String mode, String perkId) {
            Set<String> disabled = disabledInsanePerks.get(mode);
            if (disabled == null) return true;
            return !disabled.contains(perkId);
        }

        public Set<String> getEnabledInsanePerks(String mode) {
            Set<String> disabled = disabledInsanePerks.getOrDefault(mode, new HashSet<>());
            Set<String> enabled = new HashSet<>();
            for (String perkId : unlockedPerks) {
                if (!disabled.contains(perkId)) {
                    enabled.add(perkId);
                }
            }
            return enabled;
        }

        // Favorites
        public void toggleFavorite(String kitId) {
            if (favoriteKits.contains(kitId)) {
                favoriteKits.remove(kitId);
            } else {
                favoriteKits.add(kitId);
            }
        }

        public boolean isFavorite(String kitId) {
            return favoriteKits.contains(kitId);
        }

        // Prestige XP
        public int getKitXP(String kitId) {
            return kitPrestigeXP.getOrDefault(kitId, 0);
        }

        public void addKitXP(String kitId, int xp) {
            int current = getKitXP(kitId);
            kitPrestigeXP.put(kitId, current + xp);
        }

        public int getKitPrestigeLevel(String kitId) {
            int xp = getKitXP(kitId);
            // Prestige thresholds: 1000, 2500, 5000, 10000, 15000, 20000, 30000
            int[] thresholds = {0, 1000, 2500, 5000, 10000, 15000, 20000, 30000};
            for (int i = thresholds.length - 1; i >= 0; i--) {
                if (xp >= thresholds[i]) {
                    return i;
                }
            }
            return 0;
        }

        public SkywarsUnlocks copy() {
            return new SkywarsUnlocks(unlockedKits, unlockedPerks, selectedKit, selectedPerks,
                    selectedKitByMode, selectedPerksByMode, disabledInsanePerks, favoriteKits, kitPrestigeXP);
        }
    }
}
