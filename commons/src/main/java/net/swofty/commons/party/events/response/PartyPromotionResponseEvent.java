package net.swofty.commons.party.events.response;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import net.swofty.commons.party.FullParty;
import net.swofty.commons.party.PartyResponseEvent;
import net.swofty.commons.protocol.Serializer;
import org.json.JSONObject;

import java.util.UUID;

public class PartyPromotionResponseEvent extends PartyResponseEvent {
    private final UUID promoter;
    private final UUID promoted;
    private final FullParty.Role newRole;

    @JsonCreator
    public PartyPromotionResponseEvent(@JsonProperty("party") FullParty party, @JsonProperty("promoter") UUID promoter, @JsonProperty("promoted") UUID promoted, @JsonProperty("newRole") FullParty.Role newRole) {
        super(party);
        this.promoter = promoter;
        this.promoted = promoted;
        this.newRole = newRole;
    }

    public UUID getPromoter() { return promoter; }
    public UUID getPromoted() { return promoted; }
    public FullParty.Role getNewRole() { return newRole; }

    @Override
    public Serializer<PartyPromotionResponseEvent> getSerializer() {
        return new Serializer<>() {
            @Override
            public String serialize(PartyPromotionResponseEvent value) {
                JSONObject json = new JSONObject();
                Serializer<FullParty> partySerializer = FullParty.getStaticSerializer();

                json.put("party", partySerializer.serialize((FullParty) value.getParty()));
                json.put("promoter", value.promoter.toString());
                json.put("promoted", value.promoted.toString());
                json.put("newRole", value.newRole.name());
                return json.toString();
            }

            @Override
            public PartyPromotionResponseEvent deserialize(String json) {
                JSONObject jsonObject = new JSONObject(json);
                FullParty party = FullParty.getStaticSerializer().deserialize(jsonObject.getString("party"));
                UUID promoter = UUID.fromString(jsonObject.getString("promoter"));
                UUID promoted = UUID.fromString(jsonObject.getString("promoted"));
                FullParty.Role newRole = FullParty.Role.valueOf(jsonObject.getString("newRole"));
                return new PartyPromotionResponseEvent(party, promoter, promoted, newRole);
            }

            @Override
            public PartyPromotionResponseEvent clone(PartyPromotionResponseEvent value) {
                return new PartyPromotionResponseEvent((FullParty) value.getParty(), value.promoter, value.promoted, value.newRole);
            }
        };
    }
}