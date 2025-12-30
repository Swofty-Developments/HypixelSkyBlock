package net.swofty.commons.presence;

import lombok.Getter;
import net.swofty.commons.protocol.Serializer;
import org.json.JSONObject;

import java.util.UUID;

@Getter
public class PresenceInfo {
    private final UUID uuid;
    private final boolean online;
    private final String serverType;
    private final String serverId;
    private final long lastSeen;

    public PresenceInfo(UUID uuid, boolean online, String serverType, String serverId, long lastSeen) {
        this.uuid = uuid;
        this.online = online;
        this.serverType = serverType;
        this.serverId = serverId;
        this.lastSeen = lastSeen;
    }

    public static Serializer<PresenceInfo> getSerializer() {
        return new Serializer<>() {
            @Override
            public String serialize(PresenceInfo value) {
                JSONObject json = new JSONObject();
                json.put("uuid", value.uuid.toString());
                json.put("online", value.online);
                json.put("serverType", value.serverType != null ? value.serverType : JSONObject.NULL);
                json.put("serverId", value.serverId != null ? value.serverId : JSONObject.NULL);
                json.put("lastSeen", value.lastSeen);
                return json.toString();
            }

            @Override
            public PresenceInfo deserialize(String json) {
                JSONObject jsonObject = new JSONObject(json);
                return new PresenceInfo(
                        UUID.fromString(jsonObject.getString("uuid")),
                        jsonObject.getBoolean("online"),
                        jsonObject.isNull("serverType") ? null : jsonObject.getString("serverType"),
                        jsonObject.isNull("serverId") ? null : jsonObject.getString("serverId"),
                        jsonObject.getLong("lastSeen")
                );
            }

            @Override
            public PresenceInfo clone(PresenceInfo value) {
                return new PresenceInfo(value.uuid, value.online, value.serverType, value.serverId, value.lastSeen);
            }
        };
    }
}

