package net.swofty.commons.friend.events.response;

import lombok.Getter;
import net.swofty.commons.friend.FriendResponseEvent;
import net.swofty.commons.protocol.Serializer;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
public class FriendListResponseEvent extends FriendResponseEvent {
    private final UUID player;
    private final List<FriendListEntry> friends;
    private final int page;
    private final int totalPages;
    private final boolean bestOnly;

    public FriendListResponseEvent(UUID player, List<FriendListEntry> friends, int page, int totalPages, boolean bestOnly) {
        super();
        this.player = player;
        this.friends = friends;
        this.page = page;
        this.totalPages = totalPages;
        this.bestOnly = bestOnly;
    }

    @Override
    public List<UUID> getParticipants() {
        return List.of(player);
    }

    @Override
    public Serializer<FriendListResponseEvent> getSerializer() {
        return new Serializer<>() {
            @Override
            public String serialize(FriendListResponseEvent value) {
                JSONObject json = new JSONObject();
                json.put("player", value.player.toString());

                JSONArray friendsArray = new JSONArray();
                for (FriendListEntry entry : value.friends) {
                    JSONObject entryJson = new JSONObject();
                    entryJson.put("uuid", entry.uuid.toString());
                    entryJson.put("name", entry.name);
                    entryJson.put("nickname", entry.nickname != null ? entry.nickname : JSONObject.NULL);
                    entryJson.put("isBest", entry.isBest);
                    entryJson.put("isOnline", entry.isOnline);
                    entryJson.put("lastSeen", entry.lastSeen);
                    entryJson.put("friendSince", entry.friendSince);
                    entryJson.put("server", entry.server != null ? entry.server : JSONObject.NULL);
                    friendsArray.put(entryJson);
                }
                json.put("friends", friendsArray);

                json.put("page", value.page);
                json.put("totalPages", value.totalPages);
                json.put("bestOnly", value.bestOnly);
                return json.toString();
            }

            @Override
            public FriendListResponseEvent deserialize(String json) {
                JSONObject jsonObject = new JSONObject(json);

                List<FriendListEntry> friends = new ArrayList<>();
                JSONArray friendsArray = jsonObject.getJSONArray("friends");
                for (int i = 0; i < friendsArray.length(); i++) {
                    JSONObject entryJson = friendsArray.getJSONObject(i);
                    friends.add(new FriendListEntry(
                            UUID.fromString(entryJson.getString("uuid")),
                            entryJson.getString("name"),
                            entryJson.isNull("nickname") ? null : entryJson.getString("nickname"),
                            entryJson.getBoolean("isBest"),
                            entryJson.getBoolean("isOnline"),
                            entryJson.optLong("lastSeen", 0L),
                            entryJson.optLong("friendSince", 0L),
                            entryJson.isNull("server") ? null : entryJson.getString("server")
                    ));
                }

                return new FriendListResponseEvent(
                        UUID.fromString(jsonObject.getString("player")),
                        friends,
                        jsonObject.getInt("page"),
                        jsonObject.getInt("totalPages"),
                        jsonObject.getBoolean("bestOnly")
                );
            }

            @Override
            public FriendListResponseEvent clone(FriendListResponseEvent value) {
                return new FriendListResponseEvent(value.player, new ArrayList<>(value.friends), value.page, value.totalPages, value.bestOnly);
            }
        };
    }

    @Getter
    public static class FriendListEntry {
        private final UUID uuid;
        private final String name;
        private final String nickname;
        private final boolean isBest;
        private final boolean isOnline;
        private final long lastSeen;
        private final long friendSince;
        private final String server;

        public FriendListEntry(UUID uuid, String name, String nickname, boolean isBest, boolean isOnline, long lastSeen, long friendSince, String server) {
            this.uuid = uuid;
            this.name = name;
            this.nickname = nickname;
            this.isBest = isBest;
            this.isOnline = isOnline;
            this.lastSeen = lastSeen;
            this.friendSince = friendSince;
            this.server = server;
        }
    }
}
