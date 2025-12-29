package net.swofty.commons.friend.events.response;

import lombok.Getter;
import net.swofty.commons.friend.FriendResponseEvent;
import net.swofty.commons.friend.FriendSettingType;
import net.swofty.commons.protocol.Serializer;
import org.json.JSONObject;

import java.util.List;
import java.util.UUID;

@Getter
public class FriendSettingToggledResponseEvent extends FriendResponseEvent {
    private final UUID player;
    private final FriendSettingType settingType;
    private final boolean newValue;

    public FriendSettingToggledResponseEvent(UUID player, FriendSettingType settingType, boolean newValue) {
        super();
        this.player = player;
        this.settingType = settingType;
        this.newValue = newValue;
    }

    @Override
    public List<UUID> getParticipants() {
        return List.of(player);
    }

    @Override
    public Serializer<FriendSettingToggledResponseEvent> getSerializer() {
        return new Serializer<>() {
            @Override
            public String serialize(FriendSettingToggledResponseEvent value) {
                JSONObject json = new JSONObject();
                json.put("player", value.player.toString());
                json.put("settingType", value.settingType.name());
                json.put("newValue", value.newValue);
                return json.toString();
            }

            @Override
            public FriendSettingToggledResponseEvent deserialize(String json) {
                JSONObject jsonObject = new JSONObject(json);
                return new FriendSettingToggledResponseEvent(
                        UUID.fromString(jsonObject.getString("player")),
                        FriendSettingType.valueOf(jsonObject.getString("settingType")),
                        jsonObject.getBoolean("newValue")
                );
            }

            @Override
            public FriendSettingToggledResponseEvent clone(FriendSettingToggledResponseEvent value) {
                return new FriendSettingToggledResponseEvent(value.player, value.settingType, value.newValue);
            }
        };
    }
}
