package net.swofty.commons.party.events;

import net.swofty.commons.party.PartyEvent;
import net.swofty.commons.party.PendingParty;
import net.swofty.commons.protocol.Serializer;
import org.json.JSONObject;

import java.util.List;
import java.util.UUID;

public class PartyDemoteRequestEvent extends PartyEvent {
    private final UUID demoter;
    private final UUID target;

    public PartyDemoteRequestEvent(UUID demoter, UUID target) {
        super(null);
        this.demoter = demoter;
        this.target = target;
    }

    public UUID getDemoter() { return demoter; }
    public UUID getTarget() { return target; }

    @Override
    public List<UUID> getParticipants() {
        return List.of(demoter, target);
    }

    @Override
    public Serializer<PartyDemoteRequestEvent> getSerializer() {
        return new Serializer<PartyDemoteRequestEvent>() {
            @Override
            public String serialize(PartyDemoteRequestEvent value) {
                JSONObject json = new JSONObject();
                json.put("demoter", value.demoter.toString());
                json.put("target", value.target.toString());
                return json.toString();
            }

            @Override
            public PartyDemoteRequestEvent deserialize(String json) {
                JSONObject jsonObject = new JSONObject(json);
                UUID demoter = UUID.fromString(jsonObject.getString("demoter"));
                UUID target = UUID.fromString(jsonObject.getString("target"));
                return new PartyDemoteRequestEvent(demoter, target);
            }

            @Override
            public PartyDemoteRequestEvent clone(PartyDemoteRequestEvent value) {
                return new PartyDemoteRequestEvent(value.demoter, value.target);
            }
        };
    }
}