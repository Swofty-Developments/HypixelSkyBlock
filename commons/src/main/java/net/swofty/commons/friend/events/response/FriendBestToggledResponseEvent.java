package net.swofty.commons.friend.events.response;

import lombok.Getter;
import net.swofty.commons.friend.FriendResponseEvent;
import net.swofty.commons.protocol.Serializer;
import org.json.JSONObject;

import java.util.List;
import java.util.UUID;

@Getter
public class FriendBestToggledResponseEvent extends FriendResponseEvent {
    private final UUID player;
    private final UUID target;
    private final String targetName;
    private final boolean isBest;

    public FriendBestToggledResponseEvent(UUID player, UUID target, String targetName, boolean isBest) {
        super();
        this.player = player;
        this.target = target;
        this.targetName = targetName;
        this.isBest = isBest;
    }

    @Override
    public List<UUID> getParticipants() {
        return List.of(player);
    }

    @Override
    public Serializer<FriendBestToggledResponseEvent> getSerializer() {
        return new Serializer<>() {
            @Override
            public String serialize(FriendBestToggledResponseEvent value) {
                JSONObject json = new JSONObject();
                json.put("player", value.player.toString());
                json.put("target", value.target.toString());
                json.put("targetName", value.targetName);
                json.put("isBest", value.isBest);
                return json.toString();
            }

            @Override
            public FriendBestToggledResponseEvent deserialize(String json) {
                JSONObject jsonObject = new JSONObject(json);
                return new FriendBestToggledResponseEvent(
                        UUID.fromString(jsonObject.getString("player")),
                        UUID.fromString(jsonObject.getString("target")),
                        jsonObject.getString("targetName"),
                        jsonObject.getBoolean("isBest")
                );
            }

            @Override
            public FriendBestToggledResponseEvent clone(FriendBestToggledResponseEvent value) {
                return new FriendBestToggledResponseEvent(value.player, value.target, value.targetName, value.isBest);
            }
        };
    }
}
