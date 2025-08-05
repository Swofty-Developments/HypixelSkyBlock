package net.swofty.commons.party.events;

import net.swofty.commons.party.PartyEvent;
import net.swofty.commons.protocol.Serializer;
import org.json.JSONObject;

import java.util.List;
import java.util.UUID;

public class PartyHijackRequestEvent extends PartyEvent {
    private final UUID hijacker;
    private final UUID target;

    public PartyHijackRequestEvent(UUID hijacker, UUID target) {
        super(null);
        this.hijacker = hijacker;
        this.target = target;
    }

    public UUID getHijacker() { return hijacker; }
    public UUID getTarget() { return target; }

    @Override
    public List<UUID> getParticipants() {
        return List.of(hijacker, target);
    }

    @Override
    public Serializer<PartyHijackRequestEvent> getSerializer() {
        return new Serializer<PartyHijackRequestEvent>() {
            @Override
            public String serialize(PartyHijackRequestEvent value) {
                JSONObject json = new JSONObject();
                json.put("hijacker", value.hijacker.toString());
                json.put("target", value.target.toString());
                return json.toString();
            }

            @Override
            public PartyHijackRequestEvent deserialize(String json) {
                JSONObject jsonObject = new JSONObject(json);
                UUID hijacker = UUID.fromString(jsonObject.getString("hijacker"));
                UUID target = UUID.fromString(jsonObject.getString("target"));
                return new PartyHijackRequestEvent(hijacker, target);
            }

            @Override
            public PartyHijackRequestEvent clone(PartyHijackRequestEvent value) {
                return new PartyHijackRequestEvent(value.hijacker, value.target);
            }
        };
    }
}