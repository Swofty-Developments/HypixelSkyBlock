package net.swofty.commons.friend.events.response;

import lombok.Getter;
import net.swofty.commons.friend.FriendResponseEvent;
import net.swofty.commons.protocol.Serializer;
import org.json.JSONObject;

import java.util.List;
import java.util.UUID;

@Getter
public class FriendRemovedResponseEvent extends FriendResponseEvent {
    private final UUID remover;
    private final UUID removed;
    private final String removerName;

    public FriendRemovedResponseEvent(UUID remover, UUID removed, String removerName) {
        super();
        this.remover = remover;
        this.removed = removed;
        this.removerName = removerName;
    }

    @Override
    public List<UUID> getParticipants() {
        return List.of(remover, removed);
    }

    @Override
    public Serializer<FriendRemovedResponseEvent> getSerializer() {
        return new Serializer<>() {
            @Override
            public String serialize(FriendRemovedResponseEvent value) {
                JSONObject json = new JSONObject();
                json.put("remover", value.remover.toString());
                json.put("removed", value.removed.toString());
                json.put("removerName", value.removerName);
                return json.toString();
            }

            @Override
            public FriendRemovedResponseEvent deserialize(String json) {
                JSONObject jsonObject = new JSONObject(json);
                return new FriendRemovedResponseEvent(
                        UUID.fromString(jsonObject.getString("remover")),
                        UUID.fromString(jsonObject.getString("removed")),
                        jsonObject.getString("removerName")
                );
            }

            @Override
            public FriendRemovedResponseEvent clone(FriendRemovedResponseEvent value) {
                return new FriendRemovedResponseEvent(value.remover, value.removed, value.removerName);
            }
        };
    }
}
