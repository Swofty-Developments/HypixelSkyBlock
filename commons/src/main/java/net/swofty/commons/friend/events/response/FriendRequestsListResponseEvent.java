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
public class FriendRequestsListResponseEvent extends FriendResponseEvent {
    private final UUID player;
    private final List<FriendRequestEntry> requests;
    private final int page;
    private final int totalPages;

    public FriendRequestsListResponseEvent(UUID player, List<FriendRequestEntry> requests, int page, int totalPages) {
        super();
        this.player = player;
        this.requests = requests;
        this.page = page;
        this.totalPages = totalPages;
    }

    @Override
    public List<UUID> getParticipants() {
        return List.of(player);
    }

    @Override
    public Serializer<FriendRequestsListResponseEvent> getSerializer() {
        return new Serializer<>() {
            @Override
            public String serialize(FriendRequestsListResponseEvent value) {
                JSONObject json = new JSONObject();
                json.put("player", value.player.toString());

                JSONArray requestsArray = new JSONArray();
                for (FriendRequestEntry entry : value.requests) {
                    JSONObject entryJson = new JSONObject();
                    entryJson.put("senderUuid", entry.senderUuid.toString());
                    entryJson.put("senderName", entry.senderName);
                    entryJson.put("timestamp", entry.timestamp);
                    requestsArray.put(entryJson);
                }
                json.put("requests", requestsArray);

                json.put("page", value.page);
                json.put("totalPages", value.totalPages);
                return json.toString();
            }

            @Override
            public FriendRequestsListResponseEvent deserialize(String json) {
                JSONObject jsonObject = new JSONObject(json);

                List<FriendRequestEntry> requests = new ArrayList<>();
                JSONArray requestsArray = jsonObject.getJSONArray("requests");
                for (int i = 0; i < requestsArray.length(); i++) {
                    JSONObject entryJson = requestsArray.getJSONObject(i);
                    requests.add(new FriendRequestEntry(
                            UUID.fromString(entryJson.getString("senderUuid")),
                            entryJson.getString("senderName"),
                            entryJson.getLong("timestamp")
                    ));
                }

                return new FriendRequestsListResponseEvent(
                        UUID.fromString(jsonObject.getString("player")),
                        requests,
                        jsonObject.getInt("page"),
                        jsonObject.getInt("totalPages")
                );
            }

            @Override
            public FriendRequestsListResponseEvent clone(FriendRequestsListResponseEvent value) {
                return new FriendRequestsListResponseEvent(value.player, new ArrayList<>(value.requests), value.page, value.totalPages);
            }
        };
    }

    @Getter
    public static class FriendRequestEntry {
        private final UUID senderUuid;
        private final String senderName;
        private final long timestamp;

        public FriendRequestEntry(UUID senderUuid, String senderName, long timestamp) {
            this.senderUuid = senderUuid;
            this.senderName = senderName;
            this.timestamp = timestamp;
        }
    }
}
