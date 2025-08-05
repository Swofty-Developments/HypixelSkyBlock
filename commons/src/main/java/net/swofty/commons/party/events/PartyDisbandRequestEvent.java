package net.swofty.commons.party.events;

import net.swofty.commons.party.PartyEvent;
import net.swofty.commons.protocol.Serializer;
import org.json.JSONObject;

import java.util.List;
import java.util.UUID;

public class PartyDisbandRequestEvent extends PartyEvent {
    private final UUID disbander;

    public PartyDisbandRequestEvent(UUID disbander) {
        super(null);
        this.disbander = disbander;
    }

    public UUID getDisbander() { return disbander; }

    @Override
    public List<UUID> getParticipants() {
        return List.of(disbander);
    }

    @Override
    public Serializer<PartyDisbandRequestEvent> getSerializer() {
        return new Serializer<PartyDisbandRequestEvent>() {
            @Override
            public String serialize(PartyDisbandRequestEvent value) {
                JSONObject json = new JSONObject();
                json.put("disbander", value.disbander.toString());
                return json.toString();
            }

            @Override
            public PartyDisbandRequestEvent deserialize(String json) {
                JSONObject jsonObject = new JSONObject(json);
                UUID disbander = UUID.fromString(jsonObject.getString("disbander"));
                return new PartyDisbandRequestEvent(disbander);
            }

            @Override
            public PartyDisbandRequestEvent clone(PartyDisbandRequestEvent value) {
                return new PartyDisbandRequestEvent(value.disbander);
            }
        };
    }
}
