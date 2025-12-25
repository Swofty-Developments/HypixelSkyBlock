package net.swofty.commons.skyblock;

import lombok.Getter;
import lombok.Setter;

import java.util.*;

@Getter
@Setter
public class SkyBlockPlayerProfiles {
    private final static String[] PROFILE_NAMES = {
            "Zucchini", "Papaya", "Watermelon", "Pineapple",
            "Lemon", "Apple", "Banana", "Orange", "Pear",
            "Coconuts", "Cherry", "Strawberry", "Raspberry",
            "Kiwi", "Mango", "Pomegranate", "Grape",
            "Avocado", "Tomato", "Cucumber", "Carrot",
            "Potato", "Onion", "Garlic", "Celery",
            "Broccoli", "Cauliflower", "Spinach", "Asparagus"
    };
    private static final Map<UUID, SkyBlockPlayerProfiles> profilesCache = new HashMap<>();

    UUID currentlySelected = null;
    ArrayList<UUID> profiles = new ArrayList<>();

    public SkyBlockPlayerProfiles(UUID playerUuid) {
        profilesCache.put(playerUuid, this);
    }

    public SkyBlockPlayerProfiles() {
    }

    public void addProfile(UUID profile) {
        profiles.add(profile);
        profilesCache.put(profile, this);
    }

    public void removeProfile(UUID profile) {
        profiles.remove(profile);
        profilesCache.remove(profile);
    }

    public Map<String, Object> serialize() {
        return Map.of(
                "selected", currentlySelected.toString(),
                "profiles", profiles.stream().map(UUID::toString).toList()
        );
    }

    public static SkyBlockPlayerProfiles deserialize(Map<String, Object> map) {
        SkyBlockPlayerProfiles skyBlockPlayerProfiles = new SkyBlockPlayerProfiles(UUID.fromString((String) map.get("_id"))); // Player UUID
        skyBlockPlayerProfiles.currentlySelected = UUID.fromString((String) map.get("selected")); // Profile UUID
        skyBlockPlayerProfiles.profiles = new ArrayList<>();
        for (String profile : (List<String>) map.get("profiles")) {
            skyBlockPlayerProfiles.profiles.add(UUID.fromString(profile));
        }
        return skyBlockPlayerProfiles;
    }

    public static String getRandomName() {
        return PROFILE_NAMES[new Random().nextInt(PROFILE_NAMES.length)];
    }

    public static SkyBlockPlayerProfiles get(UUID uuid) {
        return profilesCache.get(uuid);
    }
}
