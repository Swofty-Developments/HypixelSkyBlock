package net.swofty.commons.friend;

import lombok.Getter;
import lombok.Setter;
import net.swofty.commons.protocol.Serializer;
import org.json.JSONObject;

import java.util.UUID;

@Getter
public class Friend {
    private final UUID uuid;
    @Setter
    private String nickname;
    @Setter
    private boolean bestFriend;
    private final long addedTimestamp;

    public Friend(UUID uuid, String nickname, boolean bestFriend, long addedTimestamp) {
        this.uuid = uuid;
        this.nickname = nickname;
        this.bestFriend = bestFriend;
        this.addedTimestamp = addedTimestamp;
    }

    public static Friend create(UUID uuid) {
        return new Friend(uuid, null, false, System.currentTimeMillis());
    }

    public static Serializer<Friend> getStaticSerializer() {
        return new Friend(UUID.randomUUID(), null, false, 0).getSerializer();
    }

    public Serializer<Friend> getSerializer() {
        return new Serializer<>() {
            @Override
            public String serialize(Friend value) {
                JSONObject json = new JSONObject();
                json.put("uuid", value.uuid.toString());
                json.put("nickname", value.nickname != null ? value.nickname : JSONObject.NULL);
                json.put("bestFriend", value.bestFriend);
                json.put("addedTimestamp", value.addedTimestamp);
                return json.toString();
            }

            @Override
            public Friend deserialize(String json) {
                JSONObject jsonObject = new JSONObject(json);
                return new Friend(
                        UUID.fromString(jsonObject.getString("uuid")),
                        jsonObject.isNull("nickname") ? null : jsonObject.getString("nickname"),
                        jsonObject.getBoolean("bestFriend"),
                        jsonObject.getLong("addedTimestamp")
                );
            }

            @Override
            public Friend clone(Friend value) {
                return new Friend(value.uuid, value.nickname, value.bestFriend, value.addedTimestamp);
            }
        };
    }
}
