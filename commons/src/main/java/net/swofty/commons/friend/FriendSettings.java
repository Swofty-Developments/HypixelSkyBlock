package net.swofty.commons.friend;

import lombok.Getter;
import lombok.Setter;
import net.swofty.commons.protocol.Serializer;
import org.json.JSONObject;

@Getter
@Setter
public class FriendSettings {
    private boolean acceptingRequests;
    private boolean joinLeaveNotifications;

    public FriendSettings(boolean acceptingRequests, boolean joinLeaveNotifications) {
        this.acceptingRequests = acceptingRequests;
        this.joinLeaveNotifications = joinLeaveNotifications;
    }

    public static FriendSettings createDefault() {
        return new FriendSettings(true, true);
    }

    public static Serializer<FriendSettings> getStaticSerializer() {
        return createDefault().getSerializer();
    }

    public Serializer<FriendSettings> getSerializer() {
        return new Serializer<>() {
            @Override
            public String serialize(FriendSettings value) {
                JSONObject json = new JSONObject();
                json.put("acceptingRequests", value.acceptingRequests);
                json.put("joinLeaveNotifications", value.joinLeaveNotifications);
                return json.toString();
            }

            @Override
            public FriendSettings deserialize(String json) {
                JSONObject jsonObject = new JSONObject(json);
                return new FriendSettings(
                        jsonObject.getBoolean("acceptingRequests"),
                        jsonObject.getBoolean("joinLeaveNotifications")
                );
            }

            @Override
            public FriendSettings clone(FriendSettings value) {
                return new FriendSettings(value.acceptingRequests, value.joinLeaveNotifications);
            }
        };
    }
}
