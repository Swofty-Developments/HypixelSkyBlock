package net.swofty.commons.party.events;

import net.swofty.commons.party.PartyEvent;
import net.swofty.commons.party.PendingParty;
import net.swofty.commons.protocol.Serializer;
import org.json.JSONObject;

import java.util.List;
import java.util.UUID;

public class PartyKickRequestEvent extends PartyEvent {
    private final UUID kicker;
    private final UUID target;

    public PartyKickRequestEvent(UUID kicker, UUID target) {
        super(null);
        this.kicker = kicker;
        this.target = target;
    }

    public UUID getKicker() { return kicker; }
    public UUID getTarget() { return target; }

    @Override
    public List<UUID> getParticipants() {
        return List.of(kicker, target);
    }

    @Override
    public Serializer<PartyKickRequestEvent> getSerializer() {
        return new Serializer<PartyKickRequestEvent>() {
            @Override
            public String serialize(PartyKickRequestEvent value) {
                JSONObject json = new JSONObject();
                json.put("kicker", value.kicker.toString());
                json.put("target", value.target.toString());
                return json.toString();
            }

            @Override
            public PartyKickRequestEvent deserialize(String json) {
                JSONObject jsonObject = new JSONObject(json);
                UUID kicker = UUID.fromString(jsonObject.getString("kicker"));
                UUID target = UUID.fromString(jsonObject.getString("target"));
                return new PartyKickRequestEvent(kicker, target);
            }

            @Override
            public PartyKickRequestEvent clone(PartyKickRequestEvent value) {
                return new PartyKickRequestEvent(value.kicker, value.target);
            }
        };
    }
}