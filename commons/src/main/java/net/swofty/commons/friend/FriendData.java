package net.swofty.commons.friend;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import net.swofty.commons.protocol.JacksonSerializer;
import net.swofty.commons.protocol.Serializer;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
public class FriendData {
    private static final Serializer<FriendData> SERIALIZER = new JacksonSerializer<>(FriendData.class);

    private final UUID playerUuid;
    private final List<Friend> friends;
    @Setter
    private FriendSettings settings;

    @JsonCreator
    public FriendData(
            @JsonProperty("playerUuid") UUID playerUuid,
            @JsonProperty("friends") List<Friend> friends,
            @JsonProperty("settings") FriendSettings settings) {
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
        return SERIALIZER;
    }

    public Serializer<FriendData> getSerializer() {
        return SERIALIZER;
    }
}
