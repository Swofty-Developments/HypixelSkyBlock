package net.swofty.commons.party.events;

import net.swofty.commons.party.PartyEvent;
import net.swofty.commons.protocol.Serializer;
import org.json.JSONObject;

import java.util.List;
import java.util.UUID;

public class PartyPlayerDisconnectEvent extends PartyEvent {
    private final UUID disconnectedPlayer;

    public PartyPlayerDisconnectEvent(UUID disconnectedPlayer) {
        super(null);
        this.disconnectedPlayer = disconnectedPlayer;
    }

    public UUID getDisconnectedPlayer() { return disconnectedPlayer; }

    @Override
    public List<UUID> getParticipants() {
        return List.of(disconnectedPlayer);
    }

    @Override
    public Serializer<PartyPlayerDisconnectEvent> getSerializer() {
        return new Serializer<PartyPlayerDisconnectEvent>() {
            @Override
            public String serialize(PartyPlayerDisconnectEvent value) {
                JSONObject json = new JSONObject();
                json.put("disconnectedPlayer", value.disconnectedPlayer.toString());
                return json.toString();
            }

            @Override
            public PartyPlayerDisconnectEvent deserialize(String json) {
                JSONObject jsonObject = new JSONObject(json);
                UUID disconnectedPlayer = UUID.fromString(jsonObject.getString("disconnectedPlayer"));
                return new PartyPlayerDisconnectEvent(disconnectedPlayer);
            }

            @Override
            public PartyPlayerDisconnectEvent clone(PartyPlayerDisconnectEvent value) {
                return new PartyPlayerDisconnectEvent(value.disconnectedPlayer);
            }
        };
    }
}
