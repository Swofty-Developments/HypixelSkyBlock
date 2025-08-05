package net.swofty.commons.protocol.objects.party;

import net.swofty.commons.party.FullParty;
import net.swofty.commons.party.Party;
import net.swofty.commons.protocol.ProtocolObject;
import net.swofty.commons.protocol.Serializer;
import org.json.JSONObject;

import java.util.UUID;

public class GetPartyProtocolObject extends ProtocolObject
        <GetPartyProtocolObject.GetPartyMessage,
                GetPartyProtocolObject.GetPartyResponse> {

    @Override
    public Serializer<GetPartyMessage> getSerializer() {
        return new Serializer<GetPartyMessage>() {
            @Override
            public String serialize(GetPartyMessage value) {
                JSONObject json = new JSONObject();
                json.put("memberUUID", value.memberUuid.toString());
                return json.toString();
            }

            @Override
            public GetPartyMessage deserialize(String json) {
                JSONObject jsonObject = new JSONObject(json);
                UUID memberUUID = UUID.fromString(jsonObject.getString("memberUUID"));
                return new GetPartyMessage(memberUUID);
            }

            @Override
            public GetPartyMessage clone(GetPartyMessage value) {
                return new GetPartyMessage(value.memberUuid);
            }
        };
    }

    @Override
    public Serializer<GetPartyResponse> getReturnSerializer() {
        return new Serializer<GetPartyResponse>() {
            @Override
            public String serialize(GetPartyResponse value) {
                JSONObject json = new JSONObject();
                Serializer<FullParty> partySerializer = FullParty.getStaticSerializer();
                json.put("party", partySerializer.serialize((FullParty) value.party));
                return json.toString();
            }

            @Override
            public GetPartyResponse deserialize(String json) {
                JSONObject jsonObject = new JSONObject(json);
                FullParty party = FullParty.getStaticSerializer().deserialize(jsonObject.getString("party"));
                return new GetPartyResponse(party);
            }

            @Override
            public GetPartyResponse clone(GetPartyResponse value) {
                return new GetPartyResponse(value.party);
            }
        };
    }

    public record GetPartyMessage(UUID memberUuid) { }

    public record GetPartyResponse(Party party) { }
}
