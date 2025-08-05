package net.swofty.commons.party.events.response;

import net.swofty.commons.party.FullParty;
import net.swofty.commons.party.PartyEvent;
import net.swofty.commons.party.PartyResponseEvent;
import net.swofty.commons.party.PendingParty;
import net.swofty.commons.protocol.Serializer;
import org.json.JSONObject;

import java.util.UUID;

public class PartyInviteResponseEvent extends PartyResponseEvent {
    private final UUID inviter;
    private final UUID invitee;

    public PartyInviteResponseEvent(PendingParty party) {
        super(party);
        this.inviter = party.getLeader();
        this.invitee = party.getInvitee();
    }

    public UUID getInviter() { return inviter; }
    public UUID getInvitee() { return invitee; }

    @Override
    public Serializer<PartyInviteResponseEvent> getSerializer() {
        return new Serializer<PartyInviteResponseEvent>() {
            @Override
            public String serialize(PartyInviteResponseEvent value) {
                JSONObject json = new JSONObject();
                Serializer<PendingParty> partySerializer = PendingParty.getStaticSerializer();

                json.put("party", partySerializer.serialize((PendingParty) value.getParty()));
                json.put("inviter", value.inviter.toString());
                json.put("invitee", value.invitee.toString());
                return json.toString();
            }

            @Override
            public PartyInviteResponseEvent deserialize(String json) {
                JSONObject jsonObject = new JSONObject(json);
                PendingParty party = PendingParty.getStaticSerializer().deserialize(jsonObject.getString("party"));
                return new PartyInviteResponseEvent(party);
            }

            @Override
            public PartyInviteResponseEvent clone(PartyInviteResponseEvent value) {
                return new PartyInviteResponseEvent((PendingParty) value.getParty());
            }
        };
    }
}