package net.swofty.commons.party.events;

import net.swofty.commons.party.PartyEvent;
import net.swofty.commons.protocol.Serializer;
import org.json.JSONObject;

import java.util.List;
import java.util.UUID;

public class PartyChatMessageEvent extends PartyEvent {
    private final UUID player;
    private final String message;

    public PartyChatMessageEvent(UUID player, String message) {
        super(null);
        this.player = player;
        this.message = message;
    }

    public UUID getPlayer() { return player; }
    public String getMessage() { return message; }

    @Override
    public List<UUID> getParticipants() {
        return List.of(player);
    }

    @Override
    public Serializer<PartyChatMessageEvent> getSerializer() {
        return new Serializer<PartyChatMessageEvent>() {
            @Override
            public String serialize(PartyChatMessageEvent value) {
                JSONObject json = new JSONObject();
                json.put("player", value.player.toString());
                json.put("message", value.message);
                return json.toString();
            }

            @Override
            public PartyChatMessageEvent deserialize(String json) {
                JSONObject jsonObject = new JSONObject(json);
                UUID player = UUID.fromString(jsonObject.getString("player"));
                String message = jsonObject.getString("message");
                return new PartyChatMessageEvent(player, message);
            }

            @Override
            public PartyChatMessageEvent clone(PartyChatMessageEvent value) {
                return new PartyChatMessageEvent(value.player, value.message);
            }
        };
    }
}