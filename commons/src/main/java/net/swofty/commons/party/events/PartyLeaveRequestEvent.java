package net.swofty.commons.party.events;

import net.swofty.commons.party.PartyEvent;
import net.swofty.commons.protocol.Serializer;
import org.json.JSONObject;

import java.util.List;
import java.util.UUID;

public class PartyLeaveRequestEvent extends PartyEvent {
    private final UUID leaver;

    public PartyLeaveRequestEvent(UUID leaver) {
        super(null);
        this.leaver = leaver;
    }

    public UUID getLeaver() { return leaver; }

    @Override
    public List<UUID> getParticipants() {
        return List.of(leaver);
    }

    @Override
    public Serializer<PartyLeaveRequestEvent> getSerializer() {
        return new Serializer<PartyLeaveRequestEvent>() {
            @Override
            public String serialize(PartyLeaveRequestEvent value) {
                JSONObject json = new JSONObject();
                json.put("leaver", value.leaver.toString());
                return json.toString();
            }

            @Override
            public PartyLeaveRequestEvent deserialize(String json) {
                JSONObject jsonObject = new JSONObject(json);
                UUID leaver = UUID.fromString(jsonObject.getString("leaver"));
                return new PartyLeaveRequestEvent(leaver);
            }

            @Override
            public PartyLeaveRequestEvent clone(PartyLeaveRequestEvent value) {
                return new PartyLeaveRequestEvent(value.leaver);
            }
        };
    }
}