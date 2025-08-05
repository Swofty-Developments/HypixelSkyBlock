package net.swofty.commons.protocol.objects.party;

import net.swofty.commons.party.PartyEvent;
import net.swofty.commons.protocol.ProtocolObject;
import net.swofty.commons.protocol.Serializer;
import org.json.JSONObject;

import java.util.UUID;

public class IsPlayerInPartyProtocolObject extends ProtocolObject
        <IsPlayerInPartyProtocolObject.IsPlayerInPartyMessage,
                IsPlayerInPartyProtocolObject.IsPlayerInPartyResponse> {


    @Override
    public Serializer<IsPlayerInPartyMessage> getSerializer() {
        return new Serializer<IsPlayerInPartyMessage>() {
            @Override
            public String serialize(IsPlayerInPartyMessage value) {
                JSONObject json = new JSONObject();
                json.put("playerUUID", value.playerUUID.toString());
                return json.toString();
            }

            @Override
            public IsPlayerInPartyMessage deserialize(String json) {
                JSONObject jsonObject = new JSONObject(json);
                UUID playerUUID = UUID.fromString(jsonObject.getString("playerUUID"));
                return new IsPlayerInPartyMessage(playerUUID);
            }

            @Override
            public IsPlayerInPartyMessage clone(IsPlayerInPartyMessage value) {
                return new IsPlayerInPartyMessage(value.playerUUID);
            }
        };
    }

    @Override
    public Serializer<IsPlayerInPartyResponse> getReturnSerializer() {
        return new Serializer<IsPlayerInPartyResponse>() {
            @Override
            public String serialize(IsPlayerInPartyResponse value) {
                return value.isInParty ? "true" : "false";
            }

            @Override
            public IsPlayerInPartyResponse deserialize(String json) {
                return new IsPlayerInPartyResponse(json.equals("true"));
            }

            @Override
            public IsPlayerInPartyResponse clone(IsPlayerInPartyResponse value) {
                return new IsPlayerInPartyResponse(value.isInParty);
            }
        };
    }

    public record IsPlayerInPartyMessage(UUID playerUUID) {
    }

    public record IsPlayerInPartyResponse(boolean isInParty) {
    }
}
