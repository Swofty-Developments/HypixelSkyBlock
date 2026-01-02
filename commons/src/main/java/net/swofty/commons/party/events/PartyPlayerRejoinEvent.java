package net.swofty.commons.party.events;

import net.swofty.commons.party.PartyEvent;
import net.swofty.commons.protocol.Serializer;
import org.json.JSONObject;

import java.util.List;
import java.util.UUID;

public class PartyPlayerRejoinEvent extends PartyEvent {
    private final UUID rejoinedPlayer;

    public PartyPlayerRejoinEvent(UUID rejoinedPlayer) {
        super(null);
        this.rejoinedPlayer = rejoinedPlayer;
    }

    public UUID getRejoinedPlayer() { return rejoinedPlayer; }

    @Override
    public List<UUID> getParticipants() {
        return List.of(rejoinedPlayer);
    }

    @Override
    public Serializer<PartyPlayerRejoinEvent> getSerializer() {
        return new Serializer<PartyPlayerRejoinEvent>() {
            @Override
            public String serialize(PartyPlayerRejoinEvent value) {
                JSONObject json = new JSONObject();
                json.put("rejoinedPlayer", value.rejoinedPlayer.toString());
                return json.toString();
            }

            @Override
            public PartyPlayerRejoinEvent deserialize(String json) {
                JSONObject jsonObject = new JSONObject(json);
                UUID rejoinedPlayer = UUID.fromString(jsonObject.getString("rejoinedPlayer"));
                return new PartyPlayerRejoinEvent(rejoinedPlayer);
            }

            @Override
            public PartyPlayerRejoinEvent clone(PartyPlayerRejoinEvent value) {
                return new PartyPlayerRejoinEvent(value.rejoinedPlayer);
            }
        };
    }
}
