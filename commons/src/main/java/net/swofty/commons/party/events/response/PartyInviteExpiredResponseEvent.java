package net.swofty.commons.party.events.response;

import net.swofty.commons.party.Party;
import net.swofty.commons.party.PartyResponseEvent;
import net.swofty.commons.party.PendingParty;
import net.swofty.commons.protocol.Serializer;
import org.json.JSONObject;

import java.util.UUID;

public class PartyInviteExpiredResponseEvent extends PartyResponseEvent {
    private final UUID inviter;
    private final UUID invitee;

    public PartyInviteExpiredResponseEvent(Party party, UUID inviter, UUID invitee) {
        super(party);
        this.inviter = inviter;
        this.invitee = invitee;
    }

    public UUID getInviter() { return inviter; }
    public UUID getInvitee() { return invitee; }

    @Override
    public Serializer<PartyInviteExpiredResponseEvent> getSerializer() {
        return new Serializer<PartyInviteExpiredResponseEvent>() {
            @Override
            public String serialize(PartyInviteExpiredResponseEvent value) {
                JSONObject json = new JSONObject();
                Serializer<PendingParty> partySerializer = PendingParty.getStaticSerializer();

                json.put("party", partySerializer.serialize((PendingParty) value.getParty()));
                json.put("inviter", value.getInviter().toString());
                json.put("invitee", value.getInvitee().toString());
                return json.toString();
            }

            @Override
            public PartyInviteExpiredResponseEvent deserialize(String json) {
                JSONObject jsonObject = new JSONObject(json);
                PendingParty party = PendingParty.getStaticSerializer().deserialize(jsonObject.getString("party"));
                UUID inviter = UUID.fromString(jsonObject.getString("inviter"));
                UUID invitee = UUID.fromString(jsonObject.getString("invitee"));
                return new PartyInviteExpiredResponseEvent(party, inviter, invitee);
            }

            @Override
            public PartyInviteExpiredResponseEvent clone(PartyInviteExpiredResponseEvent value) {
                return new PartyInviteExpiredResponseEvent(value.getParty(), value.getInviter(), value.getInvitee());
            }
        };
    }
}