package net.swofty.commons.party.events;

import net.swofty.commons.party.PartyEvent;
import net.swofty.commons.protocol.Serializer;
import org.json.JSONObject;

import java.util.List;
import java.util.UUID;

public class PartyWarpRequestEvent extends PartyEvent {
    private final UUID warper;

    public PartyWarpRequestEvent(UUID warper) {
        super(null);
        this.warper = warper;
    }

    public UUID getWarper() { return warper; }

    @Override
    public List<UUID> getParticipants() {
        return List.of(warper);
    }

    @Override
    public Serializer<PartyWarpRequestEvent> getSerializer() {
        return new Serializer<PartyWarpRequestEvent>() {
            @Override
            public String serialize(PartyWarpRequestEvent value) {
                JSONObject json = new JSONObject();
                json.put("warper", value.warper.toString());
                return json.toString();
            }

            @Override
            public PartyWarpRequestEvent deserialize(String json) {
                JSONObject jsonObject = new JSONObject(json);
                UUID warper = UUID.fromString(jsonObject.getString("warper"));
                return new PartyWarpRequestEvent(warper);
            }

            @Override
            public PartyWarpRequestEvent clone(PartyWarpRequestEvent value) {
                return new PartyWarpRequestEvent(value.warper);
            }
        };
    }
}