package net.swofty.commons.friend.events;

import lombok.Getter;
import net.swofty.commons.friend.FriendEvent;
import net.swofty.commons.friend.FriendSettingType;
import net.swofty.commons.protocol.Serializer;
import org.json.JSONObject;

import java.util.List;
import java.util.UUID;

@Getter
public class FriendToggleSettingRequestEvent extends FriendEvent {
    private final UUID player;
    private final FriendSettingType settingType;

    public FriendToggleSettingRequestEvent(UUID player, FriendSettingType settingType) {
        super();
        this.player = player;
        this.settingType = settingType;
    }

    @Override
    public List<UUID> getParticipants() {
        return List.of(player);
    }

    @Override
    public Serializer<FriendToggleSettingRequestEvent> getSerializer() {
        return new Serializer<>() {
            @Override
            public String serialize(FriendToggleSettingRequestEvent value) {
                JSONObject json = new JSONObject();
                json.put("player", value.player.toString());
                json.put("settingType", value.settingType.name());
                return json.toString();
            }

            @Override
            public FriendToggleSettingRequestEvent deserialize(String json) {
                JSONObject jsonObject = new JSONObject(json);
                return new FriendToggleSettingRequestEvent(
                        UUID.fromString(jsonObject.getString("player")),
                        FriendSettingType.valueOf(jsonObject.getString("settingType"))
                );
            }

            @Override
            public FriendToggleSettingRequestEvent clone(FriendToggleSettingRequestEvent value) {
                return new FriendToggleSettingRequestEvent(value.player, value.settingType);
            }
        };
    }
}
