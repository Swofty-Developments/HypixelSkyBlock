package net.swofty.commons.party.events.response;

import lombok.Getter;
import net.swofty.commons.party.FullParty;
import net.swofty.commons.party.PartyResponseEvent;
import net.swofty.commons.protocol.Serializer;
import org.json.JSONObject;

import java.util.UUID;

@Getter
public class PartyMemberJoinResponseEvent extends PartyResponseEvent {
    private final FullParty party;
    private final UUID inviter;
    private final UUID joiner;

    public PartyMemberJoinResponseEvent(FullParty party, UUID inviter, UUID joiner) {
        super(party);

        this.party = party;
        this.inviter = inviter;
        this.joiner = joiner;
    }

    @Override
    public Serializer<PartyMemberJoinResponseEvent> getSerializer() {
        return new Serializer<PartyMemberJoinResponseEvent>() {
            @Override
            public String serialize(PartyMemberJoinResponseEvent value) {
                JSONObject json = new JSONObject();
                Serializer<FullParty> partySerializer = FullParty.getStaticSerializer();
                json.put("party", partySerializer.serialize(value.getParty()));
                json.put("inviter", value.getInviter().toString());
                json.put("joiner", value.getJoiner().toString());
                return json.toString();
            }

            @Override
            public PartyMemberJoinResponseEvent deserialize(String json) {
                JSONObject jsonObject = new JSONObject(json);
                FullParty party = FullParty.getStaticSerializer().deserialize(jsonObject.getString("party"));
                UUID inviter = UUID.fromString(jsonObject.getString("inviter"));
                UUID joiner = UUID.fromString(jsonObject.getString("joiner"));
                return new PartyMemberJoinResponseEvent(party, inviter, joiner);
            }

            @Override
            public PartyMemberJoinResponseEvent clone(PartyMemberJoinResponseEvent value) {
                return new PartyMemberJoinResponseEvent(value.getParty(), value.getInviter(), value.getJoiner());
            }
        };
    }
}