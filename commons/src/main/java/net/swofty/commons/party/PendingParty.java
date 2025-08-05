package net.swofty.commons.party;

import lombok.Getter;
import net.swofty.commons.protocol.Serializer;
import org.json.JSONObject;

import java.util.List;
import java.util.UUID;

@Getter
public class PendingParty implements Party {
    public final UUID resultPartyUUID;
    public final UUID invitee;
    public final UUID leader;

    public PendingParty(UUID resultPartyUUID, UUID invitee, UUID leader) {
        this.resultPartyUUID = resultPartyUUID;
        this.invitee = invitee;
        this.leader = leader;
    }

    @Override
    public List<UUID> getParticipants() {
        return List.of(invitee, leader);
    }

    public static PendingParty create(UUID invitee, UUID leader) {
        return new PendingParty(UUID.randomUUID(), invitee, leader);
    }

    public static Serializer<PendingParty> getStaticSerializer() {
        PendingParty party = create(UUID.randomUUID(), UUID.randomUUID());
        return party.getSerializer();
    }

    @Override
    public Serializer<PendingParty> getSerializer() {
        return new Serializer<PendingParty>() {
            @Override
            public String serialize(PendingParty value) {
                JSONObject json = new JSONObject();
                json.put("resultPartyUUID", value.resultPartyUUID.toString());
                json.put("invitee", value.invitee.toString());
                json.put("leader", value.leader.toString());
                return json.toString();
            }

            @Override
            public PendingParty deserialize(String json) {
                JSONObject jsonObject = new JSONObject(json);
                return new PendingParty(
                        UUID.fromString(jsonObject.getString("resultPartyUUID")),
                        UUID.fromString(jsonObject.getString("invitee")),
                        UUID.fromString(jsonObject.getString("leader"))
                );
            }

            @Override
            public PendingParty clone(PendingParty value) {
                return new PendingParty(value.resultPartyUUID, value.invitee, value.leader);
            }
        };
    }
}
