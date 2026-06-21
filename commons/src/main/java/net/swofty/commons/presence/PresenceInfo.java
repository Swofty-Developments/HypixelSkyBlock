package net.swofty.commons.presence;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import net.swofty.commons.protocol.JacksonSerializer;
import net.swofty.commons.protocol.Serializer;

import java.util.UUID;

@Getter
public class PresenceInfo {
    private static final Serializer<PresenceInfo> SERIALIZER = new JacksonSerializer<>(PresenceInfo.class);

    private final UUID uuid;
    private final boolean online;
    private final String serverType;
    private final String serverId;
    private final long lastSeen;

    @JsonCreator
    public PresenceInfo(
            @JsonProperty("uuid") UUID uuid,
            @JsonProperty("online") boolean online,
            @JsonProperty("serverType") String serverType,
            @JsonProperty("serverId") String serverId,
            @JsonProperty("lastSeen") long lastSeen) {
        this.uuid = uuid;
        this.online = online;
        this.serverType = serverType;
        this.serverId = serverId;
        this.lastSeen = lastSeen;
    }

    public static Serializer<PresenceInfo> getSerializer() {
        return SERIALIZER;
    }
}
