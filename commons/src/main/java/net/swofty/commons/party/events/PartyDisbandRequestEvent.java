package net.swofty.commons.party.events;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import net.swofty.commons.party.PartyEvent;
import net.swofty.commons.protocol.Serializer;
import org.json.JSONObject;

import java.util.List;
import java.util.UUID;

public class PartyDisbandRequestEvent extends PartyEvent {
    private final UUID disbander;

    @JsonCreator
    public PartyDisbandRequestEvent(@JsonProperty("disbander") UUID disbander) {
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
        return new Serializer<>() {
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
