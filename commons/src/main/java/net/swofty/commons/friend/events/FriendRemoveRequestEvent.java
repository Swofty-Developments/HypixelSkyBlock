package net.swofty.commons.friend.events;

import lombok.Getter;
import net.swofty.commons.friend.FriendEvent;
import net.swofty.commons.protocol.Serializer;
import org.json.JSONObject;

import java.util.List;
import java.util.UUID;

@Getter
public class FriendRemoveRequestEvent extends FriendEvent {
    private final UUID remover;
    private final UUID target;

    public FriendRemoveRequestEvent(UUID remover, UUID target) {
        super();
        this.remover = remover;
        this.target = target;
    }

    @Override
    public List<UUID> getParticipants() {
        return List.of(remover, target);
    }

    @Override
    public Serializer<FriendRemoveRequestEvent> getSerializer() {
        return new Serializer<>() {
            @Override
            public String serialize(FriendRemoveRequestEvent value) {
                JSONObject json = new JSONObject();
                json.put("remover", value.remover.toString());
                json.put("target", value.target.toString());
                return json.toString();
            }

            @Override
            public FriendRemoveRequestEvent deserialize(String json) {
                JSONObject jsonObject = new JSONObject(json);
                return new FriendRemoveRequestEvent(
                        UUID.fromString(jsonObject.getString("remover")),
                        UUID.fromString(jsonObject.getString("target"))
                );
            }

            @Override
            public FriendRemoveRequestEvent clone(FriendRemoveRequestEvent value) {
                return new FriendRemoveRequestEvent(value.remover, value.target);
            }
        };
    }
}
