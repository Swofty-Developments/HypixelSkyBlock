package net.swofty.commons.party.events;

import net.swofty.commons.party.PartyEvent;
import net.swofty.commons.protocol.Serializer;
import org.json.JSONObject;

import java.util.List;
import java.util.UUID;

public class PartyPromoteRequestEvent extends PartyEvent {
    private final UUID promoter;
    private final UUID target;

    public PartyPromoteRequestEvent(UUID promoter, UUID target) {
        super(null);
        this.promoter = promoter;
        this.target = target;
    }

    public UUID getPromoter() { return promoter; }
    public UUID getTarget() { return target; }

    @Override
    public List<UUID> getParticipants() {
        return List.of(promoter, target);
    }

    @Override
    public Serializer<PartyPromoteRequestEvent> getSerializer() {
        return new Serializer<PartyPromoteRequestEvent>() {
            @Override
            public String serialize(PartyPromoteRequestEvent value) {
                JSONObject json = new JSONObject();
                json.put("promoter", value.promoter.toString());
                json.put("target", value.target.toString());
                return json.toString();
            }

            @Override
            public PartyPromoteRequestEvent deserialize(String json) {
                JSONObject jsonObject = new JSONObject(json);
                UUID promoter = UUID.fromString(jsonObject.getString("promoter"));
                UUID target = UUID.fromString(jsonObject.getString("target"));
                return new PartyPromoteRequestEvent(promoter, target);
            }

            @Override
            public PartyPromoteRequestEvent clone(PartyPromoteRequestEvent value) {
                return new PartyPromoteRequestEvent(value.promoter, value.target);
            }
        };
    }
}
