package net.swofty.commons.friend;

import lombok.Getter;
import lombok.Setter;
import net.swofty.commons.protocol.Serializer;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
public class FriendData {
    private final UUID playerUuid;
    private final List<Friend> friends;
    @Setter
    private FriendSettings settings;

    public FriendData(UUID playerUuid, List<Friend> friends, FriendSettings settings) {
        this.playerUuid = playerUuid;
        this.friends = friends;
        this.settings = settings;
    }

    public static FriendData createEmpty(UUID playerUuid) {
        return new FriendData(playerUuid, new ArrayList<>(), FriendSettings.createDefault());
    }

    public boolean isFriendWith(UUID uuid) {
        return friends.stream().anyMatch(friend -> friend.getUuid().equals(uuid));
    }

    public Friend getFriend(UUID uuid) {
        return friends.stream()
                .filter(friend -> friend.getUuid().equals(uuid))
                .findFirst()
                .orElse(null);
    }

    public List<Friend> getBestFriends() {
        return friends.stream()
                .filter(Friend::isBestFriend)
                .toList();
    }

    public int getFriendCount() {
        return friends.size();
    }

    public int getBestFriendCount() {
        return (int) friends.stream().filter(Friend::isBestFriend).count();
    }

    public void addFriend(Friend friend) {
        friends.add(friend);
    }

    public void removeFriend(UUID uuid) {
        friends.removeIf(friend -> friend.getUuid().equals(uuid));
    }

    public void removeAllNonBestFriends() {
        friends.removeIf(friend -> !friend.isBestFriend());
    }

    public static Serializer<FriendData> getStaticSerializer() {
        return createEmpty(UUID.randomUUID()).getSerializer();
    }

    public Serializer<FriendData> getSerializer() {
        return new Serializer<>() {
            @Override
            public String serialize(FriendData value) {
                JSONObject json = new JSONObject();
                json.put("playerUuid", value.playerUuid.toString());

                JSONArray friendsArray = new JSONArray();
                for (Friend friend : value.friends) {
                    friendsArray.put(new JSONObject(friend.getSerializer().serialize(friend)));
                }
                json.put("friends", friendsArray);

                json.put("settings", new JSONObject(value.settings.getSerializer().serialize(value.settings)));
                return json.toString();
            }

            @Override
            public FriendData deserialize(String json) {
                JSONObject jsonObject = new JSONObject(json);
                UUID playerUuid = UUID.fromString(jsonObject.getString("playerUuid"));

                List<Friend> friends = new ArrayList<>();
                JSONArray friendsArray = jsonObject.getJSONArray("friends");
                Serializer<Friend> friendSerializer = Friend.getStaticSerializer();
                for (int i = 0; i < friendsArray.length(); i++) {
                    friends.add(friendSerializer.deserialize(friendsArray.getJSONObject(i).toString()));
                }

                FriendSettings settings = FriendSettings.getStaticSerializer()
                        .deserialize(jsonObject.getJSONObject("settings").toString());

                return new FriendData(playerUuid, friends, settings);
            }

            @Override
            public FriendData clone(FriendData value) {
                List<Friend> clonedFriends = new ArrayList<>();
                Serializer<Friend> friendSerializer = Friend.getStaticSerializer();
                for (Friend friend : value.friends) {
                    clonedFriends.add(friendSerializer.clone(friend));
                }
                return new FriendData(
                        value.playerUuid,
                        clonedFriends,
                        value.settings.getSerializer().clone(value.settings)
                );
            }
        };
    }
}
