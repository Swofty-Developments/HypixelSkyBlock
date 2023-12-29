package net.swofty.user;

import lombok.Getter;
import lombok.Setter;

import java.util.*;

@Getter
public class UserProfiles {
    public final static String[] PROFILE_NAMES = {
            "Zucchini", "Papaya", "Watermelon", "Pineapple",
            "Lemon", "Apple", "Banana", "Orange", "Pear",
            "Coconuts", "Cherry", "Strawberry", "Raspberry",
            "Kiwi", "Mango", "Pomegranate", "Grape",
            "Avocado", "Tomato", "Cucumber", "Carrot",
            "Potato", "Onion", "Garlic", "Celery",
            "Broccoli", "Cauliflower", "Spinach", "Asparagus"
    };
    private static final Map<UUID, UserProfiles> profilesCache = new HashMap<>();

    @Setter
    UUID currentlySelected = null;
    List<UUID> profiles = new ArrayList<>();

    public UserProfiles(UUID playerUuid) {
        profilesCache.put(playerUuid, this);
    }

    public void addProfile(UUID profile) {
        profiles.add(profile);
        profilesCache.put(profile, this);
    }

    public Map<String, Object> serialize() {
        return Map.of(
                "selected", currentlySelected.toString(),
                "profiles", profiles.stream().map(UUID::toString).toList()
        );
    }

    public static UserProfiles deserialize(Map<String, Object> map) {
        UserProfiles userProfiles = new UserProfiles(UUID.fromString((String) map.get("_id"))); // Player UUID
        userProfiles.currentlySelected = UUID.fromString((String) map.get("selected")); // Profile UUID
        userProfiles.profiles = ((List<String>) map.get("profiles")).stream().map(UUID::fromString).toList();
        return userProfiles;
    }

    public static String getRandomName() {
        return PROFILE_NAMES[new Random().nextInt(PROFILE_NAMES.length)];
    }

    public static UserProfiles get(UUID uuid) {
        return profilesCache.get(uuid);
    }
}
