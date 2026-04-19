package net.swofty.commons.party.events.response;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import net.swofty.commons.party.FullParty;
import net.swofty.commons.party.PartyResponseEvent;
import net.swofty.commons.protocol.Serializer;
import org.json.JSONObject;

import java.util.UUID;

public class PartyMemberLeaveResponseEvent extends PartyResponseEvent {
    private final UUID leaver;

    @JsonCreator
    public PartyMemberLeaveResponseEvent(@JsonProperty("party") FullParty party, @JsonProperty("leaver") UUID leaver) {
        super(party);
        this.leaver = leaver;
    }

    public UUID getLeaver() { return leaver; }

    @Override
    public Serializer<PartyMemberLeaveResponseEvent> getSerializer() {
        return new Serializer<>() {
            @Override
            public String serialize(PartyMemberLeaveResponseEvent value) {
                JSONObject json = new JSONObject();
                Serializer<FullParty> partySerializer = FullParty.getStaticSerializer();

                json.put("party", partySerializer.serialize((FullParty) value.getParty()));
                json.put("leaver", value.leaver.toString());
                return json.toString();
            }

            @Override
            public PartyMemberLeaveResponseEvent deserialize(String json) {
                JSONObject jsonObject = new JSONObject(json);
                FullParty party = FullParty.getStaticSerializer().deserialize(jsonObject.getString("party"));
                UUID leaver = UUID.fromString(jsonObject.getString("leaver"));
                String leaverName = jsonObject.getString("leaverName");
                String reason = jsonObject.getString("reason");
                return new PartyMemberLeaveResponseEvent(party, leaver);
            }

            @Override
            public PartyMemberLeaveResponseEvent clone(PartyMemberLeaveResponseEvent value) {
                return new PartyMemberLeaveResponseEvent((FullParty) value.getParty(), value.leaver);
            }
        };
    }
}
