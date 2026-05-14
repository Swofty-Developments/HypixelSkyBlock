package net.swofty.commons.friend;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import net.swofty.commons.protocol.JacksonSerializer;
import net.swofty.commons.protocol.Serializer;

@Getter
@Setter
public class FriendSettings {
    private static final Serializer<FriendSettings> SERIALIZER = new JacksonSerializer<>(FriendSettings.class);

    private boolean acceptingRequests;
    private boolean joinLeaveNotifications;

    @JsonCreator
    public FriendSettings(
            @JsonProperty("acceptingRequests") boolean acceptingRequests,
            @JsonProperty("joinLeaveNotifications") boolean joinLeaveNotifications) {
        this.acceptingRequests = acceptingRequests;
        this.joinLeaveNotifications = joinLeaveNotifications;
    }

    public static FriendSettings createDefault() {
        return new FriendSettings(true, true);
    }

    public static Serializer<FriendSettings> getStaticSerializer() {
        return SERIALIZER;
    }

    public Serializer<FriendSettings> getSerializer() {
        return SERIALIZER;
    }
}
